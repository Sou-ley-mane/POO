package com.toolkit.crud.mapper;

/**
 * Contrat de mapping minimal requis par {@link com.toolkit.crud.service.AbstractCrudService}
 * pour convertir entre l'entité JPA et son DTO exposé publiquement.
 *
 * <p>Volontairement indépendant de tout framework de mapping : une implémentation peut être
 * écrite à la main ou générée par <a href="https://mapstruct.org">MapStruct</a> en implémentant
 * simplement cette interface sur un mapper {@code @Mapper(componentModel = "spring")}.
 *
 * @param <E> type de l'entité JPA
 * @param <D> type du DTO exposé par l'API
 */
public interface CrudMapper<E, D> {

    /**
     * Convertit une entité persistée en DTO destiné à la réponse HTTP.
     */
    D toDto(E entity);

    /**
     * Construit une nouvelle entité transiente à partir d'un DTO de création.
     */
    E toEntity(D dto);

    /**
     * Applique les champs modifiables du DTO sur une entité déjà chargée, en place
     * (l'entité passée en paramètre est mutée, pas remplacée), afin de préserver son
     * identifiant et sa version JPA pour le contrôle optimiste.
     */
    void updateEntityFromDto(D dto, E entity);
}
