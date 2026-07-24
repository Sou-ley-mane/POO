package com.toolkit.pagination.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Résout les paramètres de requête HTTP {@code page}, {@code size} et {@code sort} en un
 * {@link Pageable} Spring Data, en appliquant des valeurs par défaut et des bornes de sécurité
 * plutôt que de faire confiance aveuglément à l'entrée du client.
 *
 * <p>Volontairement découplé de la couche web (aucune dépendance à {@code HttpServletRequest}
 * ou {@code @RequestParam}) : le contrôleur extrait les paramètres bruts et les transmet ici.
 *
 * <p>Format attendu pour {@code sort} : {@code "champ,asc"} ou {@code "champ,desc"}
 * (plusieurs valeurs peuvent être fournies pour un tri multi-critères), par exemple
 * {@code ?sort=lastName,asc&sort=createdAt,desc}.
 */
public final class PageRequestUtils {

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 100;

    private PageRequestUtils() {
    }

    /**
     * Résout un {@link Pageable} à partir des paramètres bruts, avec les tailles par défaut
     * du toolkit ({@link #DEFAULT_SIZE}, plafonnée à {@link #MAX_SIZE}).
     */
    public static Pageable toPageable(Integer page, Integer size, List<String> sort) {
        return toPageable(page, size, sort, DEFAULT_SIZE, MAX_SIZE);
    }

    /**
     * Résout un {@link Pageable} en autorisant à surcharger la taille par défaut et le
     * plafond maximum, pour les endpoints ayant des besoins spécifiques.
     */
    public static Pageable toPageable(Integer page, Integer size, List<String> sort, int defaultSize, int maxSize) {
        int resolvedPage = (page == null || page < 0) ? DEFAULT_PAGE : page;
        int resolvedSize = (size == null || size <= 0) ? defaultSize : Math.min(size, maxSize);
        return PageRequest.of(resolvedPage, resolvedSize, parseSort(sort));
    }

    /**
     * Traduit une liste de paramètres {@code "champ,direction"} en {@link Sort}.
     * Renvoie {@link Sort#unsorted()} si la liste est vide ou nulle. Les entrées mal
     * formées (champ vide) sont ignorées plutôt que de faire échouer la requête.
     */
    public static Sort parseSort(List<String> sortParams) {
        if (sortParams == null || sortParams.isEmpty()) {
            return Sort.unsorted();
        }

        List<Sort.Order> orders = new ArrayList<>();
        for (String param : sortParams) {
            if (param == null || param.isBlank()) {
                continue;
            }
            String[] parts = param.split(",");
            String property = parts[0].trim();
            if (property.isEmpty()) {
                continue;
            }
            Sort.Direction direction = parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim())
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            orders.add(new Sort.Order(direction, property));
        }
        return orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
    }
}
