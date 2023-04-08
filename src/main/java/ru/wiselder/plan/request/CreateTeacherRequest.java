package ru.wiselder.plan.request;

import jakarta.validation.constraints.NotBlank;

public record CreateTeacherRequest(@NotBlank String fullName) {
}
