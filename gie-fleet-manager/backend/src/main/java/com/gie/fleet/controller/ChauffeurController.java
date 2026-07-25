package com.gie.fleet.controller;

import com.gie.fleet.dto.chauffeur.AffectationRequest;
import com.gie.fleet.dto.chauffeur.ChauffeurDto;
import com.gie.fleet.dto.chauffeur.CreateChauffeurRequest;
import com.gie.fleet.entity.enums.StatutChauffeur;
import com.gie.fleet.service.ChauffeurService;
import com.gie.fleet.tenant.TenantContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chauffeurs")
@RequiredArgsConstructor
@Tag(name = "Chauffeurs")
public class ChauffeurController {

    private final ChauffeurService chauffeurService;

    @PostMapping
    public ResponseEntity<ChauffeurDto> creer(@Valid @RequestBody CreateChauffeurRequest request) {
        return ResponseEntity.ok(chauffeurService.creer(TenantContext.getGieId(), request));
    }

    @GetMapping
    public ResponseEntity<List<ChauffeurDto>> lister() {
        return ResponseEntity.ok(chauffeurService.lister(TenantContext.getGieId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChauffeurDto> obtenir(@PathVariable Long id) {
        return ResponseEntity.ok(chauffeurService.obtenir(TenantContext.getGieId(), id));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<ChauffeurDto> changerStatut(@PathVariable Long id, @RequestParam StatutChauffeur statut) {
        return ResponseEntity.ok(chauffeurService.changerStatut(TenantContext.getGieId(), id, statut));
    }

    @PostMapping("/{id}/affectation")
    public ResponseEntity<ChauffeurDto> affecter(@PathVariable Long id, @Valid @RequestBody AffectationRequest request) {
        return ResponseEntity.ok(chauffeurService.affecterVehicule(TenantContext.getGieId(), id, request.vehiculeId()));
    }

    @DeleteMapping("/{id}/affectation")
    public ResponseEntity<ChauffeurDto> retirerAffectation(@PathVariable Long id) {
        return ResponseEntity.ok(chauffeurService.retirerAffectation(TenantContext.getGieId(), id));
    }
}
