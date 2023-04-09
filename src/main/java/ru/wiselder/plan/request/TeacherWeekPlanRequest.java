package ru.wiselder.plan.request;

import jakarta.validation.constraints.NotNull;

public record TeacherWeekPlanRequest(@NotNull Integer teacherId) {
}
