package com.toolkit.crud.service;

import com.toolkit.crud.mapper.CrudMapper;
import com.toolkit.crud.model.BaseEntity;
import com.toolkit.exceptions.core.ConflictException;
import com.toolkit.exceptions.core.NotFoundException;
import com.toolkit.pagination.dto.PagedResponse;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * Implémentation générique des opérations CRUD standard, à étendre par les services métier
 * concrets pour éliminer le code répétitif de create/read/update/delete/list.
 *
 * <p>Le repository doit implémenter à la fois {@link JpaRepository} (opérations de base) et
 * {@link JpaSpecificationExecutor} (filtrage dynamique, voir {@code common-pagination}) :
 * c'est le cas de toute interface Spring Data déclarée
 * {@code extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>}.
 *
 * <p>Le contrôle optimiste ({@code @Version} sur {@link BaseEntity}) est traduit en
 * {@link ConflictException} plutôt que de laisser fuiter l'exception technique Spring Data.
 *
 * @param <T>   type de l'entité JPA, doit étendre {@link BaseEntity}
 * @param <ID>  type de l'identifiant
 * @param <DTO> type du DTO exposé par l'API
 * @param <R>   type du repository Spring Data associé à l'entité
 */
public abstract class AbstractCrudService<
        T extends BaseEntity<ID>,
        ID extends Serializable,
        DTO,
        R extends JpaRepository<T, ID> & JpaSpecificationExecutor<T>>
        implements CrudOperations<ID, DTO> {

    protected final R repository;
    protected final CrudMapper<T, DTO> mapper;

    protected AbstractCrudService(R repository, CrudMapper<T, DTO> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Nom lisible de la ressource, utilisé dans les messages d'erreur
     * (ex : {@code "Utilisateur '42' introuvable"}). À implémenter par la sous-classe.
     */
    protected abstract String entityName();

    @Override
    @Transactional
    public DTO create(DTO dto) {
        T entity = mapper.toEntity(dto);
        T saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DTO getById(ID id) {
        return mapper.toDto(findEntityOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<DTO> list(Pageable pageable) {
        return PagedResponse.from(repository.findAll(pageable), mapper::toDto);
    }

    /**
     * Variante paginée avec filtrage dynamique, typiquement construite via
     * {@code GenericSpecificationBuilder} (module {@code common-pagination}).
     */
    @Transactional(readOnly = true)
    public PagedResponse<DTO> list(Specification<T> specification, Pageable pageable) {
        return PagedResponse.from(repository.findAll(specification, pageable), mapper::toDto);
    }

    @Override
    @Transactional
    public DTO update(ID id, DTO dto) {
        T entity = findEntityOrThrow(id);
        mapper.updateEntityFromDto(dto, entity);
        try {
            T saved = repository.save(entity);
            return mapper.toDto(saved);
        } catch (OptimisticLockingFailureException ex) {
            throw ConflictException.optimisticLock(entityName(), id);
        }
    }

    @Override
    @Transactional
    public void delete(ID id) {
        T entity = findEntityOrThrow(id);
        repository.delete(entity);
    }

    protected T findEntityOrThrow(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> NotFoundException.forResource(entityName(), id));
    }
}
