package com.toolkit.pagination.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenericSpecificationBuilderTest {

    @SuppressWarnings("unchecked")
    @Test
    void shouldReturnNullWhenNoCriteriaProvided() {
        Specification<Object> spec = new GenericSpecificationBuilder<>().build();

        assertThat(spec).isNull();
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldBuildEqualsPredicateOnSimpleField() {
        Root<Object> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path<Object> path = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.get("status")).thenReturn(path);
        when(cb.equal(path, "ACTIVE")).thenReturn(predicate);

        Specification<Object> spec = new GenericSpecificationBuilder<>()
                .with("status", SearchOperation.EQUALS, "ACTIVE")
                .build();

        assertThat(spec.toPredicate(root, query, cb)).isSameAs(predicate);
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldResolveDottedNestedPath() {
        Root<Object> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path<Object> addressPath = mock(Path.class);
        Path<Object> cityPath = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.get("address")).thenReturn(addressPath);
        when(addressPath.get("city")).thenReturn(cityPath);
        when(cb.equal(cityPath, "Paris")).thenReturn(predicate);

        Specification<Object> spec = new GenericSpecificationBuilder<>()
                .with("address.city", SearchOperation.EQUALS, "Paris")
                .build();

        assertThat(spec.toPredicate(root, query, cb)).isSameAs(predicate);
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldBuildIsNullPredicate() {
        Root<Object> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path<Object> path = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.get("deletedAt")).thenReturn(path);
        when(cb.isNull(path)).thenReturn(predicate);

        Specification<Object> spec = new GenericSpecificationBuilder<>()
                .with("deletedAt", SearchOperation.IS_NULL, null)
                .build();

        assertThat(spec.toPredicate(root, query, cb)).isSameAs(predicate);
    }
}
