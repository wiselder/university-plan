package ru.wiselder.plan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.wiselder.plan.model.Lesson;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class LessonIntersectionException extends RuntimeException {
    public final Type type;
    public final Lesson lesson;

    public LessonIntersectionException(Type type, Lesson lesson) {
        this.type = type;
        this.lesson = lesson;
    }

    public enum Type {
        TEACHER,
        AUDITORIUM,
        GROUP
    }
}
