package ru.wiselder.plan.request;

import jakarta.validation.constraints.NotBlank;

public record CreateDisciplineRequest(@NotBlank String name) {
}
