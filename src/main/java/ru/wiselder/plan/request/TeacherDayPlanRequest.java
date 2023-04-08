package ru.wiselder.plan.request;

import java.time.DayOfWeek;

import jakarta.validation.constraints.NotNull;

public record TeacherDayPlanRequest(@NotNull Integer teacherId, @NotNull DayOfWeek day) {
}
