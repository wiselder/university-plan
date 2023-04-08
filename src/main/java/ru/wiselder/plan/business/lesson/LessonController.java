package ru.wiselder.plan.business.lesson;

import java.util.Optional;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wiselder.plan.model.Lesson;
import ru.wiselder.plan.request.LessonRequest;

@RestController
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @PostMapping
    public void addLesson(@RequestBody @Valid LessonRequest lesson) {
        lessonService.addLesson(lesson);
    }

    @PostMapping(path = "/{id}")
    public void editLesson(@PathVariable("id") int lessonId, LessonRequest lesson) {
        lessonService.editLesson(lessonId, lesson);
    }

    @GetMapping(path = "/{id}")
    public Optional<Lesson> editLesson(@PathVariable("id") int lessonId) {
        return lessonService.getLesson(lessonId);
    }

    @DeleteMapping(path = "/{id}")
    public void removeLesson(@PathVariable("id") int lessonId) {
        lessonService.removeLesson(lessonId);
    }
}
