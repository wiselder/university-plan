package ru.wiselder.plan.request;

import java.time.DayOfWeek;

import jakarta.validation.constraints.NotNull;

public record GroupDayPlanRequest(@NotNull GroupWeekPlanRequest group,
                                  @NotNull DayOfWeek day) {
}
