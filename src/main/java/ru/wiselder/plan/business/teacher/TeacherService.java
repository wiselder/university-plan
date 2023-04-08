package ru.wiselder.plan.business.teacher;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.wiselder.plan.model.Teacher;
import ru.wiselder.plan.request.CreateTeacherRequest;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherDao teacherDao;
    public List<Teacher> getTeacherLikeName(String namePattern) {
        return teacherDao.findLikeName(namePattern);
    }

    public void addTeacher(CreateTeacherRequest request) {
        teacherDao.create(request);
    }

    public Optional<Teacher> getTeacherById(int id) {
        return teacherDao.findById(id);
    }
}
