package me.alextur.matlab.service.document;

import me.alextur.matlab.model.Document;
import me.alextur.matlab.model.Folder;
import me.alextur.matlab.model.TreeEntity;
import me.alextur.matlab.repository.document.DocumentRepository;
import me.alextur.matlab.repository.document.FolderRepository;
import me.alextur.matlab.repository.document.TreeRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.List;


/**
 * @author Alex Turchynovich
 */
@Component
public class DocumentsService {

    private final DocumentRepository documentRepository;
    private final TreeRepository treeRepository;
    private final FolderRepository folderRepository;

    public DocumentsService(@Autowired DocumentRepository pDocumentRepository,
                            @Autowired TreeRepository pTreeRepository,
                            @Autowired FolderRepository pFolderRepository) {
        documentRepository = pDocumentRepository;
        treeRepository = pTreeRepository;
        folderRepository = pFolderRepository;
    }

    public List<TreeEntity> expandTree(List<TreeEntity> rootDocuments){
        for(TreeEntity treeEntity : rootDocuments){
            List<TreeEntity> children = treeRepository.getChildren(treeEntity.getId());
            if(children != null){
                treeEntity.setChildren(children);
                expandTree(children);
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
}
