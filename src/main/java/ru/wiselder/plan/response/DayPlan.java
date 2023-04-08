package ru.wiselder.plan.response;

import java.util.List;

import ru.wiselder.plan.model.Lesson;

public record DayPlan(List<Lesson> lessons) {
}
