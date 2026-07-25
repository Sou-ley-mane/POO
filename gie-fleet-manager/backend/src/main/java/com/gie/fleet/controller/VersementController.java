package com.gie.fleet.controller;

import com.gie.fleet.dto.versement.SaisieVersementRequest;
import com.gie.fleet.dto.versement.VersementDto;
import com.gie.fleet.dto.versement.VersementGrilleLigneDto;
import com.gie.fleet.service.VersementService;
import com.gie.fleet.tenant.TenantContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/versements")
@RequiredArgsConstructor
@Tag(name = "Versements")
public class VersementController {

    private final VersementService versementService;

    @PostMapping
    public ResponseEntity<VersementDto> saisir(@Valid @RequestBody SaisieVersementRequest request) {
        return ResponseEntity.ok(versementService.saisir(TenantContext.getGieId(), TenantContext.getUserId(), request));
    }

    @GetMapping("/grille")
    public ResponseEntity<List<VersementGrilleLigneDto>> grille() {
        return ResponseEntity.ok(versementService.grille(TenantContext.getGieId()));
    }

    @GetMapping("/chauffeur/{chauffeurId}")
    public ResponseEntity<List<VersementDto>> historiqueParChauffeur(@PathVariable Long chauffeurId) {
        return ResponseEntity.ok(versementService.historiqueParChauffeur(chauffeurId));
    }

    @GetMapping("/vehicule/{vehiculeId}")
    public ResponseEntity<List<VersementDto>> historiqueParVehicule(@PathVariable Long vehiculeId) {
        return ResponseEntity.ok(versementService.historiqueParVehicule(vehiculeId));
    }
}
