# mon-toolkit

Librairie multi-module de composants réutilisables pour accélérer le développement d'API
Spring Boot 3 / Java 21 commercialisables : gestion d'erreurs RFC 7807, pagination/filtrage
JPA, CRUD générique, sécurité (JWT, API Key, rate limiting), logging structuré, testing,
validation, stockage de fichiers, configuration type-safe et notifications multi-canal.

## Modules

| Module | Rôle | Statut |
|---|---|---|
| `common-core` | Utilitaires génériques, `BaseMapper`, `Result<T>` | à détailler |
| `common-exceptions` | Hiérarchie d'exceptions métier + gestion d'erreurs RFC 7807 | ✅ implémenté |
| `common-pagination` | `PagedResponse<T>`, parsing des query params, filtrage dynamique | ✅ implémenté |
| `common-crud` | `AbstractCrudService` / `AbstractCrudController` génériques | ✅ implémenté |
| `common-security` | JWT, API Key, `@RequiresScope`, rate limiting Bucket4j | à détailler |
| `common-logging` | Logback JSON, MDC, interception HTTP | à détailler |
| `common-testing` | Testcontainers PostgreSQL/Redis, builders de données | à détailler |
| `common-validation` | Annotations de validation custom + messages i18n | à détailler |
| `common-storage` | Stockage de fichiers S3-compatible | à détailler |
| `common-config` | `@ConfigurationProperties` type-safe par domaine | à détailler |
| `common-notification` | Envoi Email/SMS/Webhook asynchrone avec retry | à détailler |

## Prérequis

- Java 21
- Maven 3.9+
- Docker (pour les tests d'intégration Testcontainers)

## Build

```bash
# Build complet
mvn clean install

# Build d'un seul module (et de ses dépendances internes déjà installées)
mvn clean install -pl common-crud -am

# Tests unitaires uniquement
mvn test

# Tests d'intégration (Testcontainers, nécessite Docker)
mvn verify
```

## Intégration dans un projet consommateur

Ajouter le module souhaité dans le `pom.xml` du projet cible :

```xml
<dependency>
    <groupId>com.toolkit</groupId>
    <artifactId>common-exceptions</artifactId>
    <version>1.0.0</version>
</dependency>
```

Chaque module dispose de son propre `README.md` avec un exemple d'utilisation en moins
de 5 minutes.

## CI/CD

Voir [`Jenkinsfile`](./Jenkinsfile) pour le pipeline complet (build, tests, qualité,
sécurité, packaging, publication Nexus) et [`settings.xml`](./settings.xml) pour la
configuration Nexus associée.

## Versioning

Le versioning suit le schéma [Maven CI Friendly Versions](https://maven.apache.org/maven-ci-friendly.html) :
la version effective est calculée à partir des propriétés `revision` / `changelist` / `sha1`,
elles-mêmes dérivées des tags Git par le pipeline Jenkins (release sur tag, snapshot sur
`main`/`develop`).
