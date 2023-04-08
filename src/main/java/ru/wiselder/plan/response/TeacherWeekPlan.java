package ru.wiselder.plan.response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record TeacherWeekPlan(List<TeacherDayPlan> dayPlans) {
    public static TeacherWeekPlan of(List<GroupLesson> lessons) {
        var list = lessons.stream()
                .collect(Collectors.groupingBy(l -> l.lesson().day()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> TeacherDayPlan.of(e.getKey(), e.getValue()))
                .toList();
        return new TeacherWeekPlan(list);
    }
}
