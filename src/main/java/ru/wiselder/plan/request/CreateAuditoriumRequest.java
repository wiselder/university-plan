package ru.wiselder.plan.request;

import jakarta.validation.constraints.NotBlank;

public record CreateAuditoriumRequest(@NotBlank String name, @NotBlank String address) {
}
