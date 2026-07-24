package com.toolkit.pagination.specification;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Construit dynamiquement une {@link Specification} JPA à partir d'une liste de
 * {@link SearchCriteria}, typiquement extraite de query params libres (ex :
 * {@code ?status=ACTIVE&createdAt.gt=2026-01-01}).
 *
 * <p>Les critères sont combinés en ET logique. Pour un OU ou des combinaisons plus
 * complexes, composer plusieurs {@link Specification} directement via
 * {@link Specification#or(Specification)} en dehors de ce builder.
 *
 * <p>Non thread-safe : instancier un builder par requête de filtrage.
 *
 * @param <T> type de l'entité JPA ciblée
 */
public class GenericSpecificationBuilder<T> {

    private final List<SearchCriteria> criteriaList = new ArrayList<>();

    public GenericSpecificationBuilder<T> with(String key, SearchOperation operation, Object value) {
        criteriaList.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public GenericSpecificationBuilder<T> with(SearchCriteria criteria) {
        criteriaList.add(criteria);
        return this;
    }

    /**
     * Assemble tous les critères accumulés en une seule {@link Specification}.
     *
     * @return la spécification combinée, ou {@code null} si aucun critère n'a été ajouté
     *         (convention Spring Data : {@code Specification.where(null)} ne filtre rien)
     */
    public Specification<T> build() {
        if (criteriaList.isEmpty()) {
            return null;
        }
        Specification<T> result = toSpecification(criteriaList.get(0));
        for (int i = 1; i < criteriaList.size(); i++) {
            result = result.and(toSpecification(criteriaList.get(i)));
        }
        return result;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Specification<T> toSpecification(SearchCriteria criteria) {
        return (root, query, cb) -> {
            Path<Comparable> path = resolvePath(root, criteria.key());
            return switch (criteria.operation()) {
                case EQUALS -> cb.equal(path, criteria.value());
                case NOT_EQUALS -> cb.notEqual(path, criteria.value());
                case LIKE -> cb.like(cb.lower(path.as(String.class)),
                        "%" + String.valueOf(criteria.value()).toLowerCase() + "%");
                case IN -> path.in(asCollection(criteria.value()));
                case IS_NULL -> cb.isNull(path);
                case IS_NOT_NULL -> cb.isNotNull(path);
                case GREATER_THAN -> cb.greaterThan(path, (Comparable) criteria.value());
                case GREATER_THAN_OR_EQUAL -> cb.greaterThanOrEqualTo(path, (Comparable) criteria.value());
                case LESS_THAN -> cb.lessThan(path, (Comparable) criteria.value());
                case LESS_THAN_OR_EQUAL -> cb.lessThanOrEqualTo(path, (Comparable) criteria.value());
            };
        };
    }

    /**
     * Résout un chemin potentiellement imbriqué (ex : {@code "address.city"}) en
     * naviguant les jointures implicites via {@link Path#get(String)}. Le type brut
     * {@link Comparable} est utilisé pour pouvoir satisfaire indifféremment les
     * opérateurs d'égalité et les opérateurs de comparaison ({@code <}, {@code >}, ...).
     */
    @SuppressWarnings("rawtypes")
    private Path<Comparable> resolvePath(Path<?> root, String key) {
        String[] segments = key.split("\\.");
        Path<Comparable> path = root.get(segments[0]);
        for (int i = 1; i < segments.length; i++) {
            path = path.get(segments[i]);
        }
        return path;
    }

    private Collection<?> asCollection(Object value) {
        if (value instanceof Collection<?> collection) {
            return collection;
        }
        throw new IllegalArgumentException("La valeur du critère IN doit être une Collection: " + value);
    }
}
