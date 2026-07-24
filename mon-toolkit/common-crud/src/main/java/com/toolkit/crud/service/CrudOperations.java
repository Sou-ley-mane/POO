package com.toolkit.crud.service;

import com.toolkit.pagination.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

/**
 * Contrat exposé aux contrôleurs, indépendant du type d'entité JPA sous-jacent.
 *
 * <p>Séparé de {@link AbstractCrudService} pour que
 * {@link com.toolkit.crud.controller.AbstractCrudController} n'ait besoin de connaître ni le
 * type d'entité ni le type de repository, uniquement l'identifiant et le DTO.
 *
 * @param <ID>  type de l'identifiant de la ressource
 * @param <DTO> type du DTO exposé par l'API
 */
public interface CrudOperations<ID extends Serializable, DTO> {

    DTO create(DTO dto);

    DTO getById(ID id);

    PagedResponse<DTO> list(Pageable pageable);

    DTO update(ID id, DTO dto);

    void delete(ID id);
}
