package com.toolkit.exceptions.core;

import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.util.Map;

/**
 * Levée lorsqu'une opération entre en conflit avec l'état courant de la ressource
 * (doublon, transition d'état invalide, verrou optimiste). Traduite en HTTP 409.
 */
public class ConflictException extends BusinessException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ConflictException(String message) {
        super(ErrorCode.CONFLICT_STATE, message, HttpStatus.CONFLICT);
    }

    public ConflictException(ErrorCode errorCode, String message) {
        super(errorCode, message, HttpStatus.CONFLICT);
    }

    public ConflictException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(errorCode, message, HttpStatus.CONFLICT, details);
    }

    /**
     * Construit une exception de conflit de verrouillage optimiste, typiquement levée
     * lorsqu'une mise à jour référence une version {@code @Version} obsolète.
     */
    public static ConflictException optimisticLock(String resourceType, Object id) {
        return new ConflictException(
                ErrorCode.OPTIMISTIC_LOCK_CONFLICT,
                "%s '%s' a été modifié par un autre utilisateur, veuillez recharger la ressource".formatted(resourceType, id),
                Map.of("resourceType", resourceType, "resourceId", String.valueOf(id))
        );
    }
}
