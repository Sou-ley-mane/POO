package com.gie.fleet.controller;

import com.gie.fleet.dto.gie.CreateGieRequest;
import com.gie.fleet.dto.gie.GieDto;
import com.gie.fleet.dto.gie.UpdateThemeRequest;
import com.gie.fleet.service.GieService;
import com.gie.fleet.tenant.TenantContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "GIE")
public class GieController {

    private final GieService gieService;

    @PostMapping("/api/v1/super-admin/gie")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<GieDto> creerGie(@Valid @RequestBody CreateGieRequest request) {
        return ResponseEntity.ok(gieService.creerGie(request));
    }

    @GetMapping("/api/v1/super-admin/gie")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<GieDto>> listerTous() {
        return ResponseEntity.ok(gieService.listerTous());
    }

    @PostMapping("/api/v1/super-admin/gie/{gieId}/suspendre")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> suspendre(@PathVariable Long gieId) {
        gieService.suspendre(gieId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/super-admin/gie/{gieId}/reactiver")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> reactiver(@PathVariable Long gieId) {
        gieService.reactiver(gieId);
        return ResponseEntity.noContent().build();
    }

    /** GIE de l'utilisateur actuellement connecté (thème, infos générales). */
    @GetMapping("/api/v1/gie/me")
    public ResponseEntity<GieDto> monGie() {
        return ResponseEntity.ok(gieService.obtenir(TenantContext.getGieId()));
    }

    @PutMapping("/api/v1/gie/theme")
    @PreAuthorize("hasAnyRole('ADMIN_GIE', 'SUPER_ADMIN')")
    public ResponseEntity<GieDto> mettreAJourTheme(@Valid @RequestBody UpdateThemeRequest request) {
        return ResponseEntity.ok(gieService.mettreAJourTheme(TenantContext.getGieId(), request));
    }
}
