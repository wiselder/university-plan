package ru.wiselder.plan.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import ru.wiselder.plan.model.Group;

public record GroupWeekPlanRequest(@NotNull @PositiveOrZero Integer faculty,
                                   @NotNull @PositiveOrZero Integer course,
                                   @NotNull @PositiveOrZero Integer number,
                                   @NotNull @PositiveOrZero Integer subNumber) {
    public boolean isSame(Group group) {
        return group.faculty() == faculty &&
                group.course() == course &&
                group.number() == number &&
                group.subNumber() == subNumber;
    }
}
