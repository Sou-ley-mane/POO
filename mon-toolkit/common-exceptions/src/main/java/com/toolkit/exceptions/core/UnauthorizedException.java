package com.toolkit.exceptions.core;

import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.util.Map;

/**
 * Levée lorsque l'authentification est absente, invalide ou expirée. Traduite en HTTP 401.
 *
 * <p>À ne pas confondre avec un refus d'accès pour un utilisateur authentifié mais non
 * autorisé, qui relève de {@link ErrorCode#ACCESS_DENIED} (HTTP 403) et de la gestion
 * standard de Spring Security ({@code AccessDeniedException}).
 */
public class UnauthorizedException extends BusinessException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String message) {
        super(ErrorCode.AUTHENTICATION_FAILED, message, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(ErrorCode errorCode, String message) {
        super(errorCode, message, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(errorCode, message, HttpStatus.UNAUTHORIZED, details);
    }
}
