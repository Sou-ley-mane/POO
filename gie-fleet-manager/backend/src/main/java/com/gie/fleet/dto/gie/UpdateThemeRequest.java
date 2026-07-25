package com.gie.fleet.dto.gie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateThemeRequest(
        @NotBlank @Pattern(regexp = "^#[0-9a-fA-F]{6}$") String couleurPrimaire,
        @NotBlank @Pattern(regexp = "^#[0-9a-fA-F]{6}$") String couleurSecondaire,
        @NotBlank @Pattern(regexp = "^#[0-9a-fA-F]{6}$") String couleurAccent,
        String logoUrl
) {
}
