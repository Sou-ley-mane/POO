package com.gie.fleet.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePinRequest(
        @NotBlank String ancienPin,
        @NotBlank @Pattern(regexp = "\\d{4}", message = "Le nouveau PIN doit contenir exactement 4 chiffres") String nouveauPin
) {
}
