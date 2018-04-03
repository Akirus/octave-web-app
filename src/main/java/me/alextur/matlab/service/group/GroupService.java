package me.alextur.matlab.service.group;

import me.alextur.matlab.model.user.StudentGroup;
import me.alextur.matlab.model.user.User;
import me.alextur.matlab.repository.user.GroupRepository;
import me.alextur.matlab.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupService {

    private GroupRepository groupRepository;

    public GroupService(@Autowired GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public StudentGroup createGroup(StudentGroup result) {
        return groupRepository.saveAndFlush(result);
    }

    public boolean hasAccessToGroup(User user, long groupId){
        if(user.getStudentGroup() != null && user.getStudentGroup().getId() == groupId){
            return true;
        }

        if(UserService.isUserInRole("Teacher")){
            return true;
        }

        return false;
    }

    public boolean hasAccessToGroup(User user, StudentGroup group){
        return hasAccessToGroup(user, group.getId());
    }
}
