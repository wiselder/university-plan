package ru.wiselder.plan.request;

import java.time.DayOfWeek;

import jakarta.validation.constraints.NotNull;
import ru.wiselder.plan.model.Week;

public record TeacherDayPlanRequest(@NotNull Integer teacherId, @NotNull DayOfWeek day, @NotNull Week week) {
}
