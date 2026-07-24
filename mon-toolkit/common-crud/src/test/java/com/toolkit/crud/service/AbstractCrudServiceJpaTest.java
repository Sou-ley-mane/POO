package com.toolkit.crud.service;

import com.toolkit.crud.support.TestProduct;
import com.toolkit.crud.support.TestProductDto;
import com.toolkit.crud.support.TestProductMapper;
import com.toolkit.crud.support.TestProductRepository;
import com.toolkit.crud.support.TestProductService;
import com.toolkit.exceptions.core.NotFoundException;
import com.toolkit.pagination.dto.PagedResponse;
import com.toolkit.pagination.specification.GenericSpecificationBuilder;
import com.toolkit.pagination.specification.SearchOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test d'intégration JPA (H2 en mémoire) couvrant le cycle de vie complet exposé par
 * {@link AbstractCrudService} : create/read/update/delete, pagination et filtrage dynamique
 * via {@code GenericSpecificationBuilder} (module {@code common-pagination}).
 */
@DataJpaTest
class AbstractCrudServiceJpaTest {

    @Autowired
    private TestProductRepository repository;

    private TestProductService service;

    @BeforeEach
    void setUp() {
        service = new TestProductService(repository, new TestProductMapper());
    }

    @Test
    void shouldCreateAndRetrieveProduct() {
        TestProductDto created = service.create(new TestProductDto(null, "Clavier", "Informatique", null));

        assertThat(created.id()).isNotNull();
        assertThat(service.getById(created.id()).name()).isEqualTo("Clavier");
    }

    @Test
    void shouldThrowNotFoundExceptionForUnknownId() {
        assertThatThrownBy(() -> service.getById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Produit");
    }

    @Test
    void shouldUpdateExistingProduct() {
        TestProductDto created = service.create(new TestProductDto(null, "Souris", "Informatique", null));

        TestProductDto updated = service.update(
                created.id(),
                new TestProductDto(created.id(), "Souris sans fil", "Informatique", created.version()));

        assertThat(updated.name()).isEqualTo("Souris sans fil");
    }

    @Test
    void shouldDeleteProduct() {
        TestProductDto created = service.create(new TestProductDto(null, "Ecran", "Informatique", null));

        service.delete(created.id());

        assertThatThrownBy(() -> service.getById(created.id())).isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldListProductsPaginated() {
        service.create(new TestProductDto(null, "A", "Cat1", null));
        service.create(new TestProductDto(null, "B", "Cat1", null));
        service.create(new TestProductDto(null, "C", "Cat2", null));

        PagedResponse<TestProductDto> page = service.list(PageRequest.of(0, 2));

        assertThat(page.data()).hasSize(2);
        assertThat(page.totalElements()).isEqualTo(3);
        assertThat(page.totalPages()).isEqualTo(2);
    }

    @Test
    void shouldFilterProductsUsingGenericSpecification() {
        service.create(new TestProductDto(null, "A", "Cat1", null));
        service.create(new TestProductDto(null, "B", "Cat2", null));

        Specification<TestProduct> spec = new GenericSpecificationBuilder<TestProduct>()
                .with("category", SearchOperation.EQUALS, "Cat2")
                .build();

        PagedResponse<TestProductDto> page = service.list(spec, PageRequest.of(0, 10));

        assertThat(page.data()).extracting(TestProductDto::name).containsExactly("B");
    }
}
