package com.gie.fleet.dto.dashboard;

import java.math.BigDecimal;
import java.util.List;

public record DashboardStatsDto(
        BigDecimal totalCollecteJour,
        BigDecimal totalCollecteSemaine,
        BigDecimal totalCollecteMois,
        BigDecimal totalRestesCumules,
        long nombreVehiculesActifs,
        long nombreChauffeursActifs,
        List<TopRetardDto> topRetards
) {
    public record TopRetardDto(Long chauffeurId, String chauffeurNomComplet, BigDecimal reste, int nombrePeriodesEnRetard) {
    }
}
