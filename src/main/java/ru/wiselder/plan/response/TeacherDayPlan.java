package ru.wiselder.plan.response;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;

public record TeacherDayPlan(DayOfWeek day, List<GroupLesson> lessons) {
    public static TeacherDayPlan of(DayOfWeek day, List<GroupLesson> lessons) {
        var sorted = lessons.stream()
                .sorted(Comparator.comparingLong(o -> o.lesson().bell().start().toEpochMilli()))
                .toList();
        return new TeacherDayPlan(day, sorted);
    }
}
