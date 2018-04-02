package me.alextur.matlab.service.group;

import me.alextur.matlab.model.user.StudentGroup;
import me.alextur.matlab.repository.user.GroupRepository;
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
}
