package ru.wiselder.plan.request;

import java.time.DayOfWeek;
import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record GroupLessonRequest(@NotNull @PositiveOrZero Integer disciplineId,
                                 @NotNull @PositiveOrZero Integer auditoriumId,
                                 @NotNull @PositiveOrZero Integer teacherId,
                                 @NotNull DayOfWeek day,
                                 @NotNull @PositiveOrZero Integer bellOrdinal,
                                 @NotEmpty Set<Integer> groupIds) {
}
