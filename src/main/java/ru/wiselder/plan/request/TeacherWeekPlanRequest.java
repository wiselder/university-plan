package ru.wiselder.plan.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record TeacherWeekPlanRequest(@NotNull @PositiveOrZero Integer teacherId) {
}
