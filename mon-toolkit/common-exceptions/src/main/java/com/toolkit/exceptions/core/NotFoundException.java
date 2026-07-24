package com.toolkit.exceptions.core;

import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.util.Map;

/**
 * Levée lorsqu'une ressource identifiée par un identifiant n'existe pas.
 * Traduite en HTTP 404 par le gestionnaire d'erreurs global.
 */
public class NotFoundException extends BusinessException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(errorCode, message, HttpStatus.NOT_FOUND, details);
    }

    /**
     * Construit une exception avec un message standardisé "{@code <type> '<id>' introuvable}".
     *
     * @param resourceType nom lisible de la ressource (ex : "Utilisateur")
     * @param id           identifiant recherché
     */
    public static NotFoundException forResource(String resourceType, Object id) {
        return new NotFoundException(
                ErrorCode.RESOURCE_NOT_FOUND,
                "%s '%s' introuvable".formatted(resourceType, id),
                Map.of("resourceType", resourceType, "resourceId", String.valueOf(id))
        );
    }
}
