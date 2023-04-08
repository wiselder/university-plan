package ru.wiselder.plan.request;

import jakarta.validation.constraints.NotBlank;

public record CreateFacultyRequest(@NotBlank String name) {
}
