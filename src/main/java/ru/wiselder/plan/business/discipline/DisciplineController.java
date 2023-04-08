package ru.wiselder.plan.business.discipline;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.wiselder.plan.model.Discipline;
import ru.wiselder.plan.request.CreateDisciplineRequest;

@RestController
@RequestMapping("/discipline")
@RequiredArgsConstructor
public class DisciplineController {
    private final DisciplineService disciplineService;

    @GetMapping
    public List<Discipline> getDisciplines(@RequestParam(value = "pattern", required = false) String namePattern) {
        return disciplineService.getDisciplineLikeName(namePattern);
    }


    @PostMapping
    public Discipline getOrCreate(@RequestBody @Valid CreateDisciplineRequest request) {
        return disciplineService.getOrCreate(request);
    }
}
