package com.gie.fleet.dto.chauffeur;

import jakarta.validation.constraints.NotNull;

public record AffectationRequest(
        @NotNull Long vehiculeId
) {
}
