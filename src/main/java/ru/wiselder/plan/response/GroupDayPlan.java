package ru.wiselder.plan.response;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;

import ru.wiselder.plan.model.Lesson;

public record GroupDayPlan(DayOfWeek day, List<Lesson> lessons) {
    public static GroupDayPlan of(DayOfWeek day, List<Lesson> lessons) {
        var sorted = lessons.stream()
                .sorted(Comparator.comparingLong(o -> o.bell().start().toEpochMilli()))
                .toList();
        return new GroupDayPlan(day, sorted);
    }
}
