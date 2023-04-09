package ru.wiselder.plan.business.teacher;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.wiselder.plan.model.Teacher;
import ru.wiselder.plan.request.CreateTeacherRequest;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping
    public List<Teacher> getTeachers(
            @RequestParam(value = "pattern", required = false, defaultValue = "") String namePattern
    ) {
        return teacherService.getTeacherLikeName(namePattern);
    }

    @PostMapping
    public Teacher getOrCreate(@RequestBody @Valid CreateTeacherRequest request) {
        return teacherService.getOrCreate(request);
    }

    @GetMapping("/{id}")
    public Teacher getTeacherById(@PathVariable int id) {
        return teacherService.getTeacherById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
