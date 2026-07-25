# GIE Fleet Manager

Application multi-tenant de gestion de flotte pour GIE (Groupements d'Intérêt Économique) de transport : suivi des véhicules, des chauffeurs, des versements périodiques et des frais, avec calcul automatique du reste à payer, tableau de bord et thème personnalisable par GIE.

Voir [`../pointage/prompt-application-gestion-gie.md`](../pointage/prompt-application-gestion-gie.md) pour le cahier des charges complet.

## Stack

- **Backend** : Spring Boot 3 / Java 17, Spring Security + JWT, Spring Data JPA, PostgreSQL, Flyway, springdoc-openapi (Swagger)
- **Frontend** : Angular 18 (standalone components, signals), thématisation dynamique par variables CSS
- **Base de données** : PostgreSQL, une seule instance partagée, filtrage systématique par `gie_id`

## Lancer l'environnement complet avec Docker

```bash
cd gie-fleet-manager
docker compose up --build
```

- Frontend : http://localhost:4200
- API backend : http://localhost:8080/api/v1
- Swagger UI : http://localhost:8080/swagger-ui.html

Un compte `SUPER_ADMIN` est créé automatiquement au premier démarrage (téléphone/PIN définis par les variables d'environnement `SUPER_ADMIN_TELEPHONE` / `SUPER_ADMIN_PIN_INITIAL`, valeurs par défaut `+000000000` / `0000` — **à changer immédiatement en production**). Il permet de créer les premiers GIE via `POST /api/v1/super-admin/gie`.

## Lancer en local sans Docker

### Backend

Prérequis : JDK 17, Maven, une instance PostgreSQL locale (`gie_fleet` / `gie_fleet` / `gie_fleet` par défaut, voir `backend/src/main/resources/application.yml`).

```bash
cd backend
mvn spring-boot:run
```

Les migrations Flyway (`src/main/resources/db/migration`) créent le schéma automatiquement au démarrage.

### Frontend

Prérequis : Node.js 20+.

```bash
cd frontend
npm install
npm start
```

L'application est alors disponible sur http://localhost:4200 et pointe vers le backend sur `http://localhost:8080/api/v1` (voir `src/environments/environment.ts`).

## Tests

```bash
cd backend && mvn test
```

## Règles de gestion clés

- `reste = montant attendu − somme des frais de la période` (le montant versé est enregistré séparément, à titre informatif/traçabilité, et n'entre jamais dans ce calcul).
- Un chauffeur ne conduit qu'un seul véhicule actif à la fois, et inversement ; chaque changement d'affectation est tracé dans `historique_affectation` (voir `ChauffeurService.affecterVehicule`).
- Authentification par téléphone + PIN à 4 chiffres (bcrypt), blocage après tentatives échouées, rate limiting sur `/api/v1/auth/login`, changement de PIN obligatoire à la première connexion.
- Isolation multi-tenant : chaque requête est filtrée par le `gieId` extrait du JWT (`TenantContext`), jamais fourni par le client.

## Note sur cet environnement de génération

Ce scaffold a été généré dans un environnement cloud isolé sans accès à Maven Central ni au registre npm (politique réseau restrictive). Le code n'a donc pas pu être compilé/buildé automatiquement ici : vérifiez `mvn compile` et `npm install` en local avant la mise en production.
