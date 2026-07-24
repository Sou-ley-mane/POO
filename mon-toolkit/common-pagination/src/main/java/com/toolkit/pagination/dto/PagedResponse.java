package com.toolkit.pagination.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/**
 * Représentation JSON stable d'une page de résultats, découplée du type {@link Page} de
 * Spring Data afin de ne jamais exposer de détails d'implémentation JPA/Hibernate
 * (proxies, métadonnées internes) dans les réponses HTTP de l'API.
 *
 * @param data           contenu de la page courante
 * @param page           index de la page courante (0-based)
 * @param size           taille de page demandée
 * @param totalElements  nombre total d'éléments toutes pages confondues
 * @param totalPages     nombre total de pages
 * @param <T>            type des éléments de la page
 */
public record PagedResponse<T>(List<T> data, int page, int size, long totalElements, int totalPages) {

    /**
     * Construit une {@link PagedResponse} directement à partir d'une {@link Page} Spring Data,
     * sans transformation du contenu.
     */
    public static <T> PagedResponse<T> from(Page<T> page) {
        return new PagedResponse<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    /**
     * Construit une {@link PagedResponse} en transformant chaque élément de la page source
     * (ex : entité JPA -> DTO), tout en conservant les métadonnées de pagination d'origine.
     */
    public static <T, U> PagedResponse<U> from(Page<T> page, Function<? super T, ? extends U> mapper) {
        List<U> mapped = page.getContent().stream().map(mapper).toList();
        return new PagedResponse<>(mapped, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
