package com.gie.fleet.controller;

import com.gie.fleet.dto.auth.ChangePinRequest;
import com.gie.fleet.dto.auth.LoginRequest;
import com.gie.fleet.dto.auth.LoginResponse;
import com.gie.fleet.service.AuthService;
import com.gie.fleet.tenant.TenantContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/changer-pin")
    public ResponseEntity<Void> changerPin(@Valid @RequestBody ChangePinRequest request) {
        authService.changerPin(TenantContext.getUserId(), request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deverrouiller/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN_GIE', 'SUPER_ADMIN')")
    public ResponseEntity<Void> deverrouillerCompte(@PathVariable Long userId) {
        authService.debloquerCompte(userId);
        return ResponseEntity.noContent().build();
    }
}
