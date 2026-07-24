package com.toolkit.exceptions.handler;

import com.toolkit.exceptions.core.BusinessException;
import com.toolkit.exceptions.core.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Gestionnaire d'erreurs global : traduit toute exception levée par les contrôleurs en une
 * réponse conforme à la <a href="https://www.rfc-editor.org/rfc/rfc7807">RFC 7807</a>
 * (Problem Details for HTTP APIs), via le type {@link ProblemDetail} intégré à Spring 6.
 *
 * <p>Étend {@link ResponseEntityExceptionHandler} afin de reformater également les erreurs
 * standard de Spring (ex : validation des {@code @RequestBody}) dans le même format,
 * pour que les consommateurs de l'API n'aient qu'une seule structure d'erreur à gérer.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final URI ERROR_TYPE_BASE_URI = URI.create("https://errors.mon-toolkit.dev/");

    /**
     * Gère toute la hiérarchie {@link BusinessException} (NotFound, BadRequest, Conflict,
     * Unauthorized, ...) en s'appuyant sur le statut HTTP et l'{@link ErrorCode} qu'elle porte.
     */
    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(BusinessException ex, HttpServletRequest request) {
        ProblemDetail problem = buildProblemDetail(ex.httpStatus(), ex.errorCode(), ex.getMessage(), request);
        ex.details().forEach(problem::setProperty);

        if (ex.httpStatus().is5xxServerError()) {
            log.error("Erreur métier non gérée [{}]: {}", ex.errorCode().code(), ex.getMessage(), ex);
        } else {
            log.warn("Erreur métier [{}]: {}", ex.errorCode().code(), ex.getMessage());
        }
        return problem;
    }

    /**
     * Filet de sécurité pour toute exception non prévue : ne divulgue jamais le message ou
     * la stack trace au client, mais les journalise pour investigation.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(Exception ex, HttpServletRequest request) {
        log.error("Erreur inattendue lors du traitement de {} {}", request.getMethod(), request.getRequestURI(), ex);
        return buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCode.INTERNAL_ERROR,
                "Une erreur interne est survenue. Veuillez réessayer ultérieurement.",
                request
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> Optional.ofNullable(fe.getDefaultMessage()).orElse("valeur invalide"),
                        (existing, duplicate) -> existing,
                        LinkedHashMap::new
                ));

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "La requête contient un ou plusieurs champs invalides.");
        problem.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        problem.setType(errorType(ErrorCode.VALIDATION_ERROR));
        problem.setProperty("errorCode", ErrorCode.VALIDATION_ERROR.code());
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("fieldErrors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(problem);
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, ErrorCode errorCode, String message, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, message);
        problem.setTitle(status.getReasonPhrase());
        problem.setType(errorType(errorCode));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("errorCode", errorCode.code());
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    private URI errorType(ErrorCode errorCode) {
        return ERROR_TYPE_BASE_URI.resolve(errorCode.code().toLowerCase(Locale.ROOT));
    }
}
