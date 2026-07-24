package com.toolkit.exceptions.core;

import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.util.Map;

/**
 * Levée lorsque la requête du client est syntaxiquement ou sémantiquement invalide
 * (hors validation Bean Validation, gérée séparément par le handler). Traduite en HTTP 400.
 */
public class BadRequestException extends BusinessException {

    @Serial
    private static final long serialVersionUID = 1L;

    public BadRequestException(String message) {
        super(ErrorCode.INVALID_REQUEST, message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(errorCode, message, HttpStatus.BAD_REQUEST, details);
    }
}
