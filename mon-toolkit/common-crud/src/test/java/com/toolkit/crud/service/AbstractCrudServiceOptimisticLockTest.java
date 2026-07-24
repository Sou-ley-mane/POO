package com.toolkit.crud.service;

import com.toolkit.crud.support.TestProduct;
import com.toolkit.crud.support.TestProductDto;
import com.toolkit.crud.support.TestProductMapper;
import com.toolkit.crud.support.TestProductRepository;
import com.toolkit.crud.support.TestProductService;
import com.toolkit.exceptions.core.ConflictException;
import com.toolkit.exceptions.core.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Vérifie, avec un repository simulé, que {@link AbstractCrudService} traduit un conflit de
 * verrouillage optimiste Spring Data en {@link ConflictException} exploitable par
 * {@code common-exceptions}, plutôt que de laisser fuiter l'exception technique.
 *
 * <p>Un vrai conflit de version ne se produit qu'en cas d'accès concurrent réel ; on simule
 * ici directement la réaction du repository pour tester la traduction sans dépendre du
 * timing de deux transactions concurrentes.
 */
class AbstractCrudServiceOptimisticLockTest {

    private TestProductRepository repository;
    private TestProductService service;

    @BeforeEach
    void setUp() {
        repository = mock(TestProductRepository.class);
        service = new TestProductService(repository, new TestProductMapper());
    }

    @Test
    void shouldTranslateOptimisticLockingFailureIntoConflictException() {
        TestProduct existing = new TestProduct("Clavier", "Informatique");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenThrow(new OptimisticLockingFailureException("stale version"));

        TestProductDto update = new TestProductDto(1L, "Clavier mécanique", "Informatique", 0L);

        assertThatThrownBy(() -> service.update(1L, update))
                .isInstanceOf(ConflictException.class)
                .satisfies(ex -> {
                    ConflictException conflict = (ConflictException) ex;
                    assertThat(conflict.errorCode()).isEqualTo(ErrorCode.OPTIMISTIC_LOCK_CONFLICT);
                    assertThat(conflict.details()).containsEntry("resourceId", "1");
                });
    }
}
