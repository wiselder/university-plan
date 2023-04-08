package ru.wiselder.plan.model;

import java.time.DayOfWeek;

public record LessonInfo(int disciplineId,
                         int auditoriumId,
                         int teacherId,
                         Week week,
                         DayOfWeek day,
                         int bellOrdinal) {
}
