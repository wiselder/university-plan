package ru.wiselder.plan.business.faculty;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wiselder.plan.model.Faculty;
import ru.wiselder.plan.model.Group;
import ru.wiselder.plan.request.CreateFacultyRequest;
import ru.wiselder.plan.request.GroupRequest;

@RestController
@RequestMapping("/faculty")
@RequiredArgsConstructor
public class FacultyController {
    private final FacultyService facultyService;

    @GetMapping
    public List<Faculty> getFaculties() {
        return facultyService.getAll();
    }

    @PostMapping
    public Faculty getOrCreate(@RequestBody @Valid CreateFacultyRequest request) {
        return facultyService.getOrCreate(request);
    }

    @GetMapping("/{faculty}/groups")
    public List<Group> getGroups(@PathVariable("faculty") int faculty) {
        return facultyService.getGroups(faculty);
    }

    @PostMapping("/groups")
    public Group getOrCreate(@RequestBody @Valid GroupRequest request) {
        return facultyService.getOrCreate(request);
    }
}
