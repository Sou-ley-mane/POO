package com.gie.fleet.dto.auth;

public record LoginResponse(
        String token,
        Long userId,
        Long gieId,
        String nom,
        String prenom,
        String role,
        boolean doitChangerPin
) {
}
