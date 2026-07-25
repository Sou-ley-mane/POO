package com.gie.fleet.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @NotBlank String telephone,
        @NotBlank @Pattern(regexp = "\\d{4}", message = "Le PIN doit contenir exactement 4 chiffres") String pin
) {
}
