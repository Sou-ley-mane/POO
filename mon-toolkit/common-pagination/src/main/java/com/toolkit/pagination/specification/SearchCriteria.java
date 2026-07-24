package com.toolkit.pagination.specification;

/**
 * Un critère de filtrage unique : le champ ciblé (notation pointée acceptée pour les
 * associations, ex : {@code "address.city"}), l'opérateur de comparaison et la valeur.
 *
 * @param key       chemin du champ sur l'entité, éventuellement imbriqué via "."
 * @param operation opérateur de comparaison à appliquer
 * @param value     valeur de comparaison (ignorée pour IS_NULL / IS_NOT_NULL)
 */
public record SearchCriteria(String key, SearchOperation operation, Object value) {
}
