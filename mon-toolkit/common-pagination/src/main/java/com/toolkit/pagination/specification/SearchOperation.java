package com.toolkit.pagination.specification;

/**
 * Opérateurs de comparaison supportés par {@link GenericSpecificationBuilder} pour construire
 * des prédicats JPA à partir de critères de recherche dynamiques.
 */
public enum SearchOperation {
    EQUALS,
    NOT_EQUALS,
    GREATER_THAN,
    GREATER_THAN_OR_EQUAL,
    LESS_THAN,
    LESS_THAN_OR_EQUAL,
    LIKE,
    IN,
    IS_NULL,
    IS_NOT_NULL
}
