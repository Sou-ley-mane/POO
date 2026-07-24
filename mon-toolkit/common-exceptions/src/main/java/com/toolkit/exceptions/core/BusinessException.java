package com.toolkit.exceptions.core;

import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Racine de la hiérarchie d'exceptions métier du toolkit.
 *
 * <p>Chaque instance porte un {@link ErrorCode} exploitable par les clients, un statut HTTP
 * à renvoyer et une carte de détails additionnels immuable, retranscrite telle quelle dans la
 * réponse {@code ProblemDetail} (RFC 7807) par le {@code GlobalExceptionHandler}.
 *
 * <p>Cette classe n'est pas destinée à être levée directement : utiliser l'une des
 * sous-classes ({@link NotFoundException}, {@link BadRequestException}, {@link ConflictException},
 * {@link UnauthorizedException}) ou, pour un cas non couvert, en créer une nouvelle.
 */
public abstract class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    private final Map<String, Object> details;

    protected BusinessException(ErrorCode errorCode, String message, HttpStatus httpStatus) {
        this(errorCode, message, httpStatus, Map.of());
    }

    protected BusinessException(ErrorCode errorCode, String message, HttpStatus httpStatus, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = details == null || details.isEmpty()
                ? Map.of()
                : Collections.unmodifiableMap(new LinkedHashMap<>(details));
    }

    public ErrorCode errorCode() {
        return errorCode;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }

    /**
     * Détails additionnels structurés (ex : nom du champ invalide, identifiant recherché).
     * Toujours non-null ; vide si aucun détail n'a été fourni.
     */
    public Map<String, Object> details() {
        return details;
    }
}
