package com.gie.fleet.controller;

import com.gie.fleet.dto.typefrais.CreateTypeFraisRequest;
import com.gie.fleet.dto.typefrais.TypeFraisDto;
import com.gie.fleet.service.TypeFraisService;
import com.gie.fleet.tenant.TenantContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/types-frais")
@RequiredArgsConstructor
@Tag(name = "Types de frais")
public class TypeFraisController {

    private final TypeFraisService typeFraisService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN_GIE', 'SUPER_ADMIN')")
    public ResponseEntity<TypeFraisDto> creer(@Valid @RequestBody CreateTypeFraisRequest request) {
        return ResponseEntity.ok(typeFraisService.creer(TenantContext.getGieId(), request));
    }

    @GetMapping
    public ResponseEntity<List<TypeFraisDto>> lister() {
        return ResponseEntity.ok(typeFraisService.lister(TenantContext.getGieId()));
    }

    @PatchMapping("/{id}/actif")
    @PreAuthorize("hasAnyRole('ADMIN_GIE', 'SUPER_ADMIN')")
    public ResponseEntity<TypeFraisDto> activerDesactiver(@PathVariable Long id, @RequestParam boolean actif) {
        return ResponseEntity.ok(typeFraisService.activerDesactiver(TenantContext.getGieId(), id, actif));
    }
}
