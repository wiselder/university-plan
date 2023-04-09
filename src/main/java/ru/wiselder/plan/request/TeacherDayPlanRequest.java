package ru.wiselder.plan.request;

import java.time.DayOfWeek;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record TeacherDayPlanRequest(@NotNull @PositiveOrZero Integer teacherId, @NotNull DayOfWeek day) {
}
