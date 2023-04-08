package ru.wiselder.plan.business.teacher;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.wiselder.plan.model.Teacher;
import ru.wiselder.plan.request.CreateTeacherRequest;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherDao teacherDao;
    public List<Teacher> getTeacherLikeName(String namePattern) {
        return teacherDao.findLikeName(namePattern);
    }

    @Transactional
    public Teacher getOrCreate(CreateTeacherRequest request) {
        var teacher = teacherDao.findByName(request.name());
        if (teacher.isEmpty()) {
            teacherDao.create(request);
            teacher = teacherDao.findByName(request.name());
        }
        return teacher.orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public Optional<Teacher> getTeacherById(int id) {
        return teacherDao.findById(id);
    }
}
