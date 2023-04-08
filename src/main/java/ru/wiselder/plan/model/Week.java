package ru.wiselder.plan.model;

import lombok.Getter;

public enum Week {
    FIRST(1),
    SECOND(2);
    @Getter
    private final int value;

    Week(int value) {
        this.value = value;
    }

    public static Week of(int weekNumber) {
        return switch (weekNumber) {
            case 1 -> FIRST;
            case 2 -> SECOND;
            default -> throw new IllegalArgumentException();
        };
    }
}
