package ru.wiselder.plan.business.plan;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import ru.wiselder.plan.model.Bell;
import ru.wiselder.plan.model.Lesson;
import ru.wiselder.plan.request.GroupWeekPlanRequest;
import ru.wiselder.plan.response.DayPlan;
import ru.wiselder.plan.response.GroupLesson;
import ru.wiselder.plan.response.WeekPlan;

@Service
public class PlanFormer {
    public DayPlan form(DayOfWeek day, List<Bell> bells, List<GroupLesson> lessons, GroupWeekPlanRequest forGroup) {
        return form(day, new SortedBells(bells), filter(lessons, forGroup));
    }

    public DayPlan form(DayOfWeek day, List<Bell> bells, List<GroupLesson> lessons, int teacherId) {
        return form(day, new SortedBells(bells), filter(lessons, teacherId));
    }

    public WeekPlan form(List<Bell> bells, List<GroupLesson> lessons, GroupWeekPlanRequest target) {
        return form(new SortedBells(bells), filter(lessons, target));
    }

    public WeekPlan form(List<Bell> bells, List<GroupLesson> lessons, int teacherId) {
        return form(new SortedBells(bells), filter(lessons, teacherId));
    }

    private WeekPlan form(SortedBells sortedBells, List<GroupLesson> lessons) {
        var lessonsByDay = lessons.stream()
                .collect(Collectors.groupingBy(l -> l.lesson().day()));

        var days = DayOfWeek.values();
        var result = new ArrayList<DayPlan>(days.length);

        for (var day: days) {
            var dayLessons = lessonsByDay.get(day);
            DayPlan dayPlan;
            if (dayLessons == null) {
                dayPlan = emptyDayPlan(day, sortedBells);
            } else {
                dayPlan = form(day, sortedBells, lessons);
            }
            result.add(dayPlan);
        }
        return new WeekPlan(result);
    }

    private DayPlan form(DayOfWeek day, SortedBells sortedBells, List<GroupLesson> lessons) {
        var lessonByBell = lessons.stream()
                .filter(gl -> gl.lesson().day() == day)
                .collect(Collectors.toMap(o -> o.lesson().bell().start(), Function.identity()));

        var result = new ArrayList<GroupLesson>(sortedBells.bells.size());

        for (var bell : sortedBells.bells) {
            var lesson = lessonByBell.get(bell.start());
            if (lesson == null) {
                lesson = emptyLesson(day, bell);
            }
            result.add(lesson);
        }

        return new DayPlan(day, result);
    }

    private DayPlan emptyDayPlan(DayOfWeek day, SortedBells sortedBells) {
        var lessons = sortedBells.bells.stream().map(b -> emptyLesson(day, b)).toList();
        return new DayPlan(day, lessons);
    }

    private GroupLesson emptyLesson(DayOfWeek day, Bell bell) {
        return new GroupLesson(
                new Lesson(0, null, null, null, day,  bell),
                Collections.emptyList()
        );
    }

    private List<GroupLesson> filter(List<GroupLesson> lessons, GroupWeekPlanRequest target) {
        return lessons
                .stream()
                .filter(gl -> gl.groups()
                        .stream()
                        .anyMatch(target::isSame)
                ).toList();
    }

    private List<GroupLesson> filter(List<GroupLesson> lessons, int teacherId) {
        return lessons
                .stream()
                .filter(gl -> gl.lesson().teacher().id() == teacherId)
                .toList();
    }

    private record SortedBells(List<Bell> bells) {
        private SortedBells(List<Bell> bells) {
            this.bells = bells.stream().sorted(Comparator.comparing(Bell::start)).toList();
        }
    }
}
