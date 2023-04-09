package ru.wiselder.plan.business.lesson;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wiselder.plan.response.GroupLesson;
import ru.wiselder.plan.request.GroupLessonRequest;

@RestController
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @PostMapping
    public GroupLesson addLesson(@RequestBody @Valid GroupLessonRequest lesson) {
        return lessonService.addLesson(lesson);
    }

    @GetMapping(path = "/{id}")
    public GroupLesson getLesson(@PathVariable("id") int lessonId) {
        return lessonService.getLesson(lessonId);
    }

    @PostMapping(path = "/{id}")
    public GroupLesson editLesson(@PathVariable("id") int lessonId, @RequestBody @Valid  GroupLessonRequest lesson) {
        return lessonService.editLesson(lessonId, lesson);
    }

    @DeleteMapping(path = "/{id}")
    public void removeLesson(@PathVariable("id") int lessonId) {
        lessonService.removeLesson(lessonId);
    }
}
