package ru.wiselder.plan.model;

import java.time.DayOfWeek;

public record Lesson(int id,
                     Discipline discipline,
                     Auditorium auditorium,
                     Teacher teacher,
                     DayOfWeek day,
                     Bell bell) {
}
