package ru.wiselder.plan.request;

import jakarta.validation.constraints.NotNull;

public record GroupWeekPlanRequest(@NotNull Integer faculty,
                                   @NotNull Integer course,
                                   @NotNull Integer number,
                                   @NotNull Integer subNumber) {
}
