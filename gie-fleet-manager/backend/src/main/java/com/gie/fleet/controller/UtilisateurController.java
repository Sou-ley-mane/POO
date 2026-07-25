package com.gie.fleet.controller;

import com.gie.fleet.dto.utilisateur.CreateGestionnaireRequest;
import com.gie.fleet.dto.utilisateur.UtilisateurDto;
import com.gie.fleet.service.UtilisateurService;
import com.gie.fleet.tenant.TenantContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/utilisateurs")
@RequiredArgsConstructor
@Tag(name = "Équipe du GIE")
@PreAuthorize("hasRole('ADMIN_GIE')")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @PostMapping
    public ResponseEntity<UtilisateurDto> creerGestionnaire(@Valid @RequestBody CreateGestionnaireRequest request) {
        return ResponseEntity.ok(utilisateurService.creerGestionnaire(TenantContext.getGieId(), request));
    }

    @GetMapping
    public ResponseEntity<List<UtilisateurDto>> lister() {
        return ResponseEntity.ok(utilisateurService.listerEquipe(TenantContext.getGieId()));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<UtilisateurDto> changerStatut(@PathVariable Long id, @RequestParam String statut) {
        return ResponseEntity.ok(utilisateurService.changerStatut(TenantContext.getGieId(), id, statut));
    }
}
