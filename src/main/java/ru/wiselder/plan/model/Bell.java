package ru.wiselder.plan.model;

import java.time.LocalTime;

public record Bell(int ordinal, LocalTime start, LocalTime finish) {
}
