package ru.wiselder.plan.business.teacher;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wiselder.plan.model.Teacher;
import ru.wiselder.plan.request.CreateTeacherRequest;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping
    public List<Teacher> getTeachers(String namePattern) {
        return teacherService.getTeacherLikeName(namePattern);
    }

    @GetMapping("/{id}")
    public Optional<Teacher> getTeacherById(@PathVariable int id) {
        return teacherService.getTeacherById(id);
    }

    @PostMapping
    public void addTeacher(@RequestBody @Valid CreateTeacherRequest request) {
        teacherService.addTeacher(request);
    }
}
