package ru.wiselder.plan.request;

import java.time.DayOfWeek;
import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LessonRequest(@NotNull Integer disciplineId,
                            @NotNull Integer auditoriumId,
                            @NotNull Integer teacherId,
                            @NotNull RequestWeek week,
                            @NotNull DayOfWeek day,
                            @NotNull Integer bellOrdinal,
                            @NotEmpty Set<Integer> groupIds) {
}
