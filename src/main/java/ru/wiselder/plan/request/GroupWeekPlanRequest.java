package ru.wiselder.plan.request;

import jakarta.validation.constraints.NotNull;
import ru.wiselder.plan.model.Group;

public record GroupWeekPlanRequest(@NotNull Integer faculty,
                                   @NotNull Integer course,
                                   @NotNull Integer number,
                                   @NotNull Integer subNumber) {
    public boolean isSame(Group group) {
        return group.faculty() == faculty &&
                group.course() == course &&
                group.number() == number &&
                group.subNumber() == subNumber;
    }
}
