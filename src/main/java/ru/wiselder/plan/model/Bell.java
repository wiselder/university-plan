package ru.wiselder.plan.model;

import java.time.Instant;

public record Bell(int ordinal, Instant start, Instant finish) {
}
