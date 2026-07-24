# common-exceptions

Hiérarchie d'exceptions métier standardisée + gestionnaire d'erreurs global produisant des
réponses conformes à la [RFC 7807](https://www.rfc-editor.org/rfc/rfc7807) (`ProblemDetail`).

## Installation

```xml
<dependency>
    <groupId>com.toolkit</groupId>
    <artifactId>common-exceptions</artifactId>
    <version>1.0.0</version>
</dependency>
```

Le `@RestControllerAdvice` (`GlobalExceptionHandler`) est détecté automatiquement par le
scan de composants Spring dès lors que le package `com.toolkit.exceptions` est inclus dans
le scan (c'est le cas par défaut si votre application principale est dans un package parent,
ex : `com.acme.myapp` avec le toolkit en `com.toolkit`, il suffit d'ajouter
`@ComponentScan(basePackages = {"com.acme.myapp", "com.toolkit"})` sur votre classe
`@SpringBootApplication`, ou d'importer explicitement la classe avec `@Import(GlobalExceptionHandler.class)`).

## Utilisation

```java
@Service
public class UserService {

    private final UserRepository repository;

    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> NotFoundException.forResource("Utilisateur", id));
    }

    public void register(String email) {
        if (repository.existsByEmail(email)) {
            throw new ConflictException(ErrorCode.CONFLICT_STATE, "Un compte existe déjà pour cet email");
        }
    }
}
```

Réponse HTTP générée automatiquement pour un `NotFoundException` :

```json
HTTP/1.1 404 Not Found
Content-Type: application/problem+json

{
  "type": "https://errors.mon-toolkit.dev/err-notfound-404",
  "title": "Not Found",
  "status": 404,
  "detail": "Utilisateur '42' introuvable",
  "instance": "/api/users/42",
  "errorCode": "ERR-NOTFOUND-404",
  "timestamp": "2026-07-24T10:15:30Z",
  "resourceType": "Utilisateur",
  "resourceId": "42"
}
```

## Exceptions disponibles

| Exception | HTTP | Usage |
|---|---|---|
| `BadRequestException` | 400 | Requête syntaxiquement/sémantiquement invalide |
| `UnauthorizedException` | 401 | Authentification absente, invalide ou expirée |
| `NotFoundException` | 404 | Ressource introuvable (`forResource(type, id)` disponible) |
| `ConflictException` | 409 | Doublon, transition invalide, verrou optimiste (`optimisticLock(type, id)`) |

Toutes héritent de `BusinessException`, qui porte un `ErrorCode` (enum de codes d'erreur
stables destinés aux clients) et une carte de `details` librement enrichissable, reprise
telle quelle dans la réponse JSON.

## Étendre avec vos propres exceptions

```java
public class QuotaExceededException extends BusinessException {
    public QuotaExceededException(String message) {
        super(ErrorCode.INVALID_REQUEST, message, HttpStatus.PAYMENT_REQUIRED);
    }
}
```

Aucune modification du `GlobalExceptionHandler` n'est nécessaire : il gère `BusinessException`
et toutes ses sous-classes de façon polymorphe.
