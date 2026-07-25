package com.gie.fleet.controller;

import com.gie.fleet.dto.vehicule.CreateVehiculeRequest;
import com.gie.fleet.dto.vehicule.VehiculeDto;
import com.gie.fleet.entity.enums.StatutVehicule;
import com.gie.fleet.service.VehiculeService;
import com.gie.fleet.tenant.TenantContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicules")
@RequiredArgsConstructor
@Tag(name = "Véhicules")
public class VehiculeController {

    private final VehiculeService vehiculeService;

    @PostMapping
    public ResponseEntity<VehiculeDto> creer(@Valid @RequestBody CreateVehiculeRequest request) {
        return ResponseEntity.ok(vehiculeService.creer(TenantContext.getGieId(), request));
    }

    @GetMapping
    public ResponseEntity<List<VehiculeDto>> lister() {
        return ResponseEntity.ok(vehiculeService.lister(TenantContext.getGieId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDto> obtenir(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculeService.obtenir(TenantContext.getGieId(), id));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<VehiculeDto> changerStatut(@PathVariable Long id, @RequestParam StatutVehicule statut) {
        return ResponseEntity.ok(vehiculeService.changerStatut(TenantContext.getGieId(), id, statut));
    }
}
