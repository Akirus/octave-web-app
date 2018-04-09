package me.alextur.matlab.service.document;

import me.alextur.matlab.model.Document;
import me.alextur.matlab.model.Folder;
import me.alextur.matlab.model.TreeEntity;
import me.alextur.matlab.model.user.Role;
import me.alextur.matlab.model.user.StudentGroup;
import me.alextur.matlab.model.user.User;
import me.alextur.matlab.repository.document.DocumentRepository;
import me.alextur.matlab.repository.document.FolderRepository;
import me.alextur.matlab.repository.document.TreeRepository;
import me.alextur.matlab.repository.user.GroupRepository;
import me.alextur.matlab.repository.user.RoleRepository;
import me.alextur.matlab.rest.endpoints.documents.DocumentLinkManager;
import me.alextur.matlab.rest.endpoints.documents.model.ChangeDocumentRequest;
import me.alextur.matlab.rest.endpoints.documents.model.ChangeTreeEntityRequest;
import me.alextur.matlab.service.user.UserService;
import me.alextur.matlab.utils.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Alex Turchynovich
 */
@Component
public class DocumentsService {

    private final DocumentRepository documentRepository;
    private final TreeRepository treeRepository;
    private final FolderRepository folderRepository;
    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;
    private final DocumentLinkManager documentLinkManager;

    public DocumentsService(@Autowired DocumentRepository pDocumentRepository,
                            @Autowired TreeRepository pTreeRepository,
                            @Autowired FolderRepository pFolderRepository,
                            @Autowired GroupRepository pGroupRepository,
                            @Autowired RoleRepository pRoleRepository,
                            @Autowired DocumentLinkManager pDocumentLinkManager) {
        documentRepository = pDocumentRepository;
        treeRepository = pTreeRepository;
        folderRepository = pFolderRepository;
        groupRepository = pGroupRepository;
        roleRepository = pRoleRepository;
        documentLinkManager = pDocumentLinkManager;
    }

    public List<TreeEntity> expandTree(final User user, List<TreeEntity> rootDocuments){
        for(TreeEntity treeEntity : rootDocuments){
            List<TreeEntity> children = treeRepository.getChildren(treeEntity.getId());

            children = accessFilterEntries(user, children);

            if(children != null){
                treeEntity.setChildren(children);
                expandTree(user, children);
            }
        }

        return rootDocuments;
    }

    public Document updateDocument(Document pDocument){
        return documentRepository.saveAndFlush(pDocument);
    }

    public Document createDocument(Document pResult, @Nullable TreeEntity parent) {
        pResult.setCreationDate(DateTime.now());
        pResult.setParent(parent);

        return documentRepository.saveAndFlush(pResult);
    }

    public Document createDocument(Document pResult, Long parentId){
        TreeEntity parent = treeRepository.findById(parentId);

        return createDocument(pResult, parent);
    }

    public Document createDocument(Document pResult) {
        return createDocument(pResult, (TreeEntity) null);
    }

    public Folder createFolder(Folder result, Long parentId) {
        if(parentId != null && parentId != -1) {
            result.setParent(treeRepository.findById(parentId));
        }

        return folderRepository.saveAndFlush(result);
    }

    public boolean hasAccessTo(User user, TreeEntity treeEntity){
        if(UserService.isUserInRole(user, "Teacher")){
            return true;
        }

        String[] roleNames = treeEntity.getAllowedRolesNames();

        if(roleNames.length > 0){
            if(!UserService.isUserInRole(user, roleNames)){
                return false;
            }
        }


        if(treeEntity.getAllowedGroups() != null){
            Set<Long> allowedGroupsIds = treeEntity.getAllowedGroups().stream()
                    .map(StudentGroup::getId)
                    .collect(Collectors.toSet());
            if(!allowedGroupsIds.isEmpty()) {
                return allowedGroupsIds.contains(user.getStudentGroup().getId());
            }
        }

        return true;
    }

    public List<TreeEntity> accessFilterEntries(final User user, final List<TreeEntity> documents) {
        return documents.stream()
                .filter(document -> this.hasAccessTo(user, document))
                .collect(Collectors.toList());
    }

    public Map<String, Object> getVisibility(TreeEntity document){
        Map<String, Object> result = new HashMap<>();

        result.put("groups", CollectionUtils.copySet(document.getAllowedGroups()));
        result.put("roles", CollectionUtils.copySet(document.getAllowedRoles()));

        return result;
    }

    public void populateVisibility(ChangeTreeEntityRequest pUpdateDocumentRequest, TreeEntity pResult) {
        if(pUpdateDocumentRequest.getAllowedGroupIds() != null){
            Set<Long> allowedGroupIds = pUpdateDocumentRequest.getAllowedGroupIds();
            Set<StudentGroup> groups = allowedGroupIds.stream()
                    .map(groupRepository::findById)
                    .collect(Collectors.toSet());

            pResult.setAllowedGroups(groups);
        }
        if(pUpdateDocumentRequest.getAllowedRoles() != null){
            Set<String> roleNames = pUpdateDocumentRequest.getAllowedRoles();
            Set<Role> roles = roleNames.stream()
                    .map(roleRepository::findByName)
                    .collect(Collectors.toSet());

            pResult.setAllowedRoles(roles);
        }
    }

    public void populateAdditionalData(TreeEntity pDocument) {
        pDocument.getAdditionalData().put("link",
                documentLinkManager.getLink(pDocument.getId()));
        pDocument.getAdditionalData().put("visibility", this.getVisibility(pDocument));
    }

}
