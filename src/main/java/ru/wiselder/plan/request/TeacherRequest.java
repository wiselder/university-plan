package ru.wiselder.plan.request;

import jakarta.validation.constraints.NotNull;

public record TeacherRequest(@NotNull Integer id) {
}
