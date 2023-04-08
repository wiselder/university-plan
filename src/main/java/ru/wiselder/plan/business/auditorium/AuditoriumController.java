package ru.wiselder.plan.business.auditorium;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.wiselder.plan.request.CreateAuditoriumRequest;
import ru.wiselder.plan.model.Auditorium;

@RestController
@RequestMapping("/auditorium")
@RequiredArgsConstructor
public class AuditoriumController {
    private final AuditoriumService auditoriumService;

    @GetMapping
    public List<Auditorium> getDisciplines(@RequestParam(value = "pattern", required = false) String namePattern) {
        return auditoriumService.getAuditoriumLikeName(namePattern);
    }

    @GetMapping("/{id}")
    public Optional<Auditorium> getById(@PathVariable("id") int id) {
        return auditoriumService.getById(id);
    }

    @PostMapping
    public Auditorium getOrCreate(@RequestBody @Valid CreateAuditoriumRequest auditoriumRequest) {
        return auditoriumService.getOrCreate(auditoriumRequest);
    }
}
