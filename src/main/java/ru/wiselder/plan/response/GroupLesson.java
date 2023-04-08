package ru.wiselder.plan.response;

import java.util.List;

import ru.wiselder.plan.model.Group;
import ru.wiselder.plan.model.Lesson;

public record GroupLesson(Lesson lesson, List<Group> groups) {
}
