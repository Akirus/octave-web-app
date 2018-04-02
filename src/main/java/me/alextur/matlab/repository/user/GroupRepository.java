package me.alextur.matlab.repository.user;

import me.alextur.matlab.model.user.StudentGroup;
import me.alextur.matlab.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<StudentGroup, Long> {
    StudentGroup findById(long groupId);
}
