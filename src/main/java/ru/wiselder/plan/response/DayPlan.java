package ru.wiselder.plan.response;

import java.time.DayOfWeek;
import java.util.List;

public record DayPlan(DayOfWeek day, List<GroupLesson> lessons) {
}
