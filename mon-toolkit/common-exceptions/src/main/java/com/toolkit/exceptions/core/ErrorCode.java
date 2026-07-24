package com.toolkit.exceptions.core;

import org.springframework.http.HttpStatus;

/**
 * Codes d'erreur métier standardisés, partagés par toutes les API construites sur ce toolkit.
 *
 * <p>Chaque code porte un statut HTTP par défaut et un message par défaut, tous deux
 * surchageables au moment où l'exception est levée (voir {@link BusinessException}).
 * Le {@code code} est destiné à être consommé par les clients (front-end, intégrateurs)
 * pour un traitement programmatique indépendant du libellé humain, qui peut être traduit.
 */
public enum ErrorCode {

    VALIDATION_ERROR("ERR-VALIDATION-400", HttpStatus.BAD_REQUEST, "La requête contient des données invalides."),
    INVALID_REQUEST("ERR-REQUEST-400", HttpStatus.BAD_REQUEST, "La requête est invalide."),
    AUTHENTICATION_FAILED("ERR-AUTH-401", HttpStatus.UNAUTHORIZED, "Authentification requise ou invalide."),
    ACCESS_DENIED("ERR-ACCESS-403", HttpStatus.FORBIDDEN, "Accès refusé à cette ressource."),
    RESOURCE_NOT_FOUND("ERR-NOTFOUND-404", HttpStatus.NOT_FOUND, "La ressource demandée est introuvable."),
    CONFLICT_STATE("ERR-CONFLICT-409", HttpStatus.CONFLICT, "La ressource est dans un état conflictuel."),
    OPTIMISTIC_LOCK_CONFLICT("ERR-CONFLICT-409-LOCK", HttpStatus.CONFLICT, "La ressource a été modifiée entre-temps par un autre utilisateur."),
    RATE_LIMIT_EXCEEDED("ERR-RATE-429", HttpStatus.TOO_MANY_REQUESTS, "Limite de requêtes dépassée."),
    INTERNAL_ERROR("ERR-INTERNAL-500", HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur interne est survenue.");

    private final String code;
    private final HttpStatus defaultStatus;
    private final String defaultMessage;

    ErrorCode(String code, HttpStatus defaultStatus, String defaultMessage) {
        this.code = code;
        this.defaultStatus = defaultStatus;
        this.defaultMessage = defaultMessage;
    }

    public String code() {
        return code;
    }

    public HttpStatus defaultStatus() {
        return defaultStatus;
    }

    public String defaultMessage() {
        return defaultMessage;
    }
}
