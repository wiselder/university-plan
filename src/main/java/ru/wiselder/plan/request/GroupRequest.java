package ru.wiselder.plan.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record GroupRequest(@NotNull @PositiveOrZero Integer course,
                           @NotNull @PositiveOrZero Integer number,
                           @NotNull @PositiveOrZero Integer subNumber) {
}
