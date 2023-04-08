package ru.wiselder.plan.response;

import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;

import ru.wiselder.plan.model.Lesson;

public record GroupWeekPlan(List<GroupDayPlan> dayPlans) {
    public static GroupWeekPlan of(List<Lesson> lessons) {
        var list = lessons.stream()
                .collect(Collectors.groupingBy(Lesson::day))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> GroupDayPlan.of(e.getKey(), e.getValue()))
                .toList();
        return new GroupWeekPlan(list);
    }
}
