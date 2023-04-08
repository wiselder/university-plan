package ru.wiselder.plan.model;

import java.time.DayOfWeek;

public record LessonInfo(int disciplineId,
                         int auditoriumId,
                         int teacherId,
                         DayOfWeek day,
                         int bellOrdinal) {
}
