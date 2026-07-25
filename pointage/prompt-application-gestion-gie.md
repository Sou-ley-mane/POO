# Prompt de développement — Application de gestion de flotte pour GIE (Transport)

## CONTEXTE MÉTIER

Je veux développer une application web pour des **GIE (Groupements d'Intérêt Économique)** qui possèdent des véhicules de transport (taxis, cars, etc.). Ces GIE :
- possèdent un parc de **véhicules** ;
- accueillent des **chauffeurs** qui s'inscrivent auprès du GIE pour conduire un véhicule donné ;
- chaque véhicule génère des **frais périodiques** (assurance, vignette, visite technique, bon de stationnement, etc.) ;
- chaque chauffeur doit **verser une somme d'argent au GIE** selon une périodicité qui lui est propre : **hebdomadaire, bimensuelle (2 semaines), trimensuelle (3 semaines) ou mensuelle** ;
- le GIE suit aujourd'hui cela via un tableau Excel du type :

| Voiture | Chauffeur | Bon | Assurance | ... (autres frais) | Montant versé | Reste |
|---|---|---|---|---|---|---|
| DK-2344-H | ... | ... | ... | ... | ... | ... |

L'application doit **digitaliser et automatiser** ce suivi, avec calcul automatique du reste à payer, historique des versements, alertes de retard, et tableaux de bord.

Chaque GIE doit pouvoir **personnaliser l'apparence de l'application avec au moins 3 couleurs qui lui sont propres** (thème paramétrable par GIE, façon "marque blanche").

---

## OBJECTIF

Construire une application **multi-GIE (multi-tenant)** où :
- un **Super Admin** gère la plateforme et crée les comptes des GIE ;
- chaque **GIE** dispose de son propre espace isolé (ses véhicules, ses chauffeurs, ses paiements, ses couleurs) ;
- aucune donnée d'un GIE n'est visible par un autre.

### Mode de déploiement et stratégie multi-tenant retenue
L'application sera déployée **sur un seul serveur VPS**, en une seule instance, et sera utilisée par **plusieurs GIE simultanément** (SaaS mono-instance). En conséquence, la stratégie multi-tenant à implémenter est :
- **Une seule base de données PostgreSQL partagée**, avec une colonne `gie_id` sur toutes les tables métier (véhicules, chauffeurs, versements, types de frais, utilisateurs) — *et non* une base ou un schéma séparé par GIE (trop lourd pour un seul VPS).
- Filtrage systématique par `gie_id` à chaque requête backend (via un `@Filter` Hibernate activé automatiquement à partir du contexte de l'utilisateur connecté, ou une clause `WHERE gie_id = :gieId` injectée dans chaque repository).
- Un seul déploiement Docker (backend + frontend + PostgreSQL) sert donc tous les GIE ; c'est uniquement le **thème (couleurs/logo)** et les **données** qui changent selon le GIE de l'utilisateur connecté, pas l'infrastructure.

---

## ACTEURS ET RÔLES

| Rôle | Description |
|---|---|
| **SUPER_ADMIN** | Administre la plateforme, crée/active/désactive les GIE, supervise globalement |
| **ADMIN_GIE** | Administrateur du GIE : gère véhicules, chauffeurs, frais, paramètres, couleurs, voit tous les rapports |
| **GESTIONNAIRE** | Employé du GIE qui saisit les versements au quotidien, affecte les chauffeurs aux véhicules |
| **CHAUFFEUR** *(optionnel, à confirmer)* | Accède en lecture seule à son propre historique de versements et son solde |

---

## MODÈLE DE DONNÉES (entités principales)

### 1. `Gie`
- id, nom, sigle, logoUrl
- couleurPrimaire, couleurSecondaire, couleurAccent (codes hexadécimaux — thème dynamique)
- adresse, telephone, email
- statut (ACTIF / SUSPENDU)
- dateCreation

### 2. `Utilisateur`
- id, nom, prenom, **telephone (identifiant de connexion, unique)**, **pinHash (code PIN à 4 chiffres, hashé avec bcrypt)**, email (optionnel, pour notifications), role, gieId (nullable pour SUPER_ADMIN), statut, dateCreation
- nombreTentativesEchouees, dateBlocage (pour la protection anti brute-force, voir plus bas)

### 3. `Vehicule`
- id, gieId, immatriculation (ex: DK-2344-H), marque, modele, année, couleur
- statut (EN_CIRCULATION / EN_PANNE / VENDU / RETIRE)
- chauffeurActuelId (**affectation active unique** — un seul chauffeur actif possible à un instant T ; nullable si véhicule non affecté)
- dateMiseEnService

### 4. `Chauffeur`
- id, gieId, nom, prenom, telephone, numeroPermis, numeroCni
- dateInscription, statut (ACTIF / SUSPENDU / RADIE)
- vehiculeAffecteId
- periodiciteVersement (HEBDOMADAIRE / BIMENSUELLE / TRIMENSUELLE / MENSUELLE)
- montantAttenduParPeriode

### 5. `TypeFrais` (paramétrable par GIE)
- id, gieId, libelle (ex: Assurance, Bon, Vignette, Visite technique, Stationnement), montant, periodicite, actif
> Permet à chaque GIE d'ajouter/modifier ses propres colonnes de frais (comme dans le tableau Excel : "etc.").

### 6. `FraisVehicule`
- id, vehiculeId, typeFraisId, montant, dateEcheance, statut (PAYE / EN_ATTENTE / EN_RETARD)

### 7. `Versement` (le cœur du suivi)
- id, chauffeurId, vehiculeId, gieId
- periodeDebut, periodeFin (calculées selon la périodicité du chauffeur)
- montantAttendu (**montant fixe défini pour cette période précise**, pas de report d'une période à l'autre)
- montantVerse (**informatif uniquement** : ce que le chauffeur a physiquement versé sur cette période — n'entre PAS dans le calcul du reste)
- listeFraisPeriode (les frais applicables sur cette période : Bon, Assurance, Vignette, etc. — voir `FraisVehicule`/`TypeFrais`)
- **reste = montantAttendu − somme(listeFraisPeriode)** ⚠️ (le montant versé n'est PAS déduit dans ce calcul, il est affiché à titre indicatif/suivi de trésorerie)
- datePaiement, modePaiement (ESPECES / MOBILE_MONEY / VIREMENT)
- statut (SOLDE / PARTIEL / IMPAYE)
- saisiPar (utilisateurId)

> **Règle de gestion confirmée** : `reste = montant attendu − somme de tous les frais de la période` (Bon + Assurance + Vignette + etc.). Le **montant versé** est enregistré à part, à titre informatif/traçabilité, mais n'intervient pas dans ce calcul du reste. Chaque période (semaine, quinzaine, 3 semaines, mois) a son propre montant attendu et son propre reste, sans report d'une période à l'autre.

### 8. `HistoriqueAffectation`
- id, vehiculeId, chauffeurId, dateDebut, dateFin (traçabilité des changements de chauffeur sur un véhicule)

---

## FONCTIONNALITÉS ATTENDUES

### Module Authentification & Multi-tenant
- **Connexion par numéro de téléphone + code PIN à 4 chiffres** (pas d'email/mot de passe classique)
- Génération de token JWT après validation du PIN, gestion des rôles, isolation stricte des données par `gieId`
- Le Super Admin crée un GIE → génère un compte ADMIN_GIE avec un téléphone + un PIN initial (à changer à la première connexion)
- **Sécurité renforcée obligatoire** (un PIN à 4 chiffres = 10 000 combinaisons possibles, donc à protéger sérieusement) :
  - Blocage du compte après un nombre limité de tentatives échouées (ex : 5 tentatives → blocage temporaire de 15 min, puis déblocage manuel par l'Admin GIE après un certain seuil)
  - PIN toujours hashé (bcrypt), jamais stocké en clair
  - Option de vérification par **OTP SMS** à la création du compte ou en cas de réinitialisation du PIN (recommandé, à valider avec le client)
  - Changement de PIN obligatoire à la première connexion
  - Limitation du débit des tentatives de connexion côté backend (rate limiting par IP/téléphone)

### Module Paramétrage GIE
- Configuration des 3 couleurs (primaire, secondaire, accent) + logo → appliqué dynamiquement à toute l'interface du GIE (variables CSS)
- Configuration des types de frais propres au GIE
- Configuration des périodicités disponibles

### Module Véhicules
- CRUD véhicules, fiche détaillée avec historique des chauffeurs affectés
- Suivi des frais (assurance, vignette, visite technique...) avec échéances et alertes

### Module Chauffeurs
- Inscription d'un chauffeur, affectation à un véhicule
- Définition de sa périodicité de versement et montant attendu
- Suspension / radiation

### Module Versements (remplace le tableau Excel)
- Saisie rapide d'un versement (chauffeur, véhicule, montant versé)
- **Calcul automatique du reste = Montant attendu − somme des frais de la période** (le montant versé est enregistré à part, à titre informatif)
- Vue "grille" reproduisant le tableau actuel : Véhicule | Chauffeur | Bon | Assurance | ... | Montant versé | Reste
- Historique complet par chauffeur / par véhicule / par période
- Génération automatique des échéances selon la périodicité (semaine 1, 2, 3, mois)
- Alerte visuelle sur impayés / retards

### Module Tableau de bord
- Total collecté (jour / semaine / mois)
- Total impayés / restes cumulés
- Nombre de véhicules actifs, chauffeurs actifs
- Top retards de paiement

### Module Rapports
- Export Excel / PDF (reprenant le format du tableau actuel)
- Filtres par période, véhicule, chauffeur

### Notifications *(optionnel V2)*
- Rappel avant échéance (email/SMS)

---

## STACK TECHNIQUE IMPOSÉE

### Frontend — Angular
- Angular 18+ (standalone components, signals)
- Angular Material **ou** PrimeNG pour les composants UI
- Gestion d'état : Signals ou NgRx (selon complexité)
- **Thématisation dynamique** : variables CSS (`--color-primary`, `--color-secondary`, `--color-accent`) injectées au chargement selon le GIE connecté, via un `ThemeService`
- Architecture modulaire (feature modules : auth, vehicules, chauffeurs, versements, dashboard, admin)
- Guards de rôle par route
- Intercepteur HTTP pour JWT

### Backend — Spring Boot
- Spring Boot 3.x, Java 17+
- Spring Security + JWT (authentification stateless)
- Spring Data JPA + PostgreSQL
- Architecture en couches : Controller / Service / Repository / DTO
- Multi-tenant par **discriminant `gieId`** filtré systématiquement (via `@Filter` Hibernate ou intercepteur de requêtes)
- Migrations de base de données avec **Flyway** ou **Liquibase**
- Validation des données (Bean Validation)
- Gestion centralisée des exceptions (`@ControllerAdvice`)
- Documentation API avec **Swagger / OpenAPI**

### Base de données
- PostgreSQL

### Autres exigences techniques
- Architecture REST (API versionnée `/api/v1/...`)
- Tests unitaires backend (JUnit + Mockito) et frontend (Jasmine/Karten ou Jest)
- Dockerisation (docker-compose : frontend, backend, postgres)
- Gestion des logs (SLF4J)
- Sécurité : **PIN à 4 chiffres hashé (bcrypt)**, protection anti brute-force (blocage après tentatives échouées, rate limiting), protection CORS, validation des rôles côté backend (jamais uniquement côté frontend)

---

## EXIGENCES UI/UX

- Interface propre, moderne, responsive (mobile/tablette pour usage terrain par les gestionnaires)
- Palette de couleurs **paramétrable par GIE** (minimum 3 couleurs : primaire, secondaire, accent) appliquée à : header, boutons, badges de statut, graphiques du dashboard
- Vue "grille" reprenant visuellement le tableau Excel existant pour ne pas dérouter les utilisateurs habitués
- Codes couleur de statut clairs (vert = soldé, orange = partiel, rouge = impayé/retard)

---

## LIVRABLES ATTENDUS DE L'IA

1. Modèle de données complet (schéma + script SQL/Flyway)
2. Architecture backend Spring Boot (arborescence des packages, entités, DTO, services, contrôleurs REST)
3. Architecture frontend Angular (arborescence des modules, composants, services, thème dynamique)
4. Endpoints API documentés (Swagger)
5. Écrans prioritaires à générer en premier :
   - Connexion
   - Dashboard GIE
   - Liste/fiche véhicules
   - Liste/fiche chauffeurs
   - Grille des versements (vue façon tableau Excel)
   - Paramètres GIE (couleurs, frais)
6. Docker-compose pour lancer l'environnement complet

---

## RÈGLES DE GESTION CONFIRMÉES
- ✅ Le montant attendu est **défini pour chaque période** (indépendant des autres périodes).
- ✅ **Reste = Montant attendu − somme de tous les frais de la période** (Bon + Assurance + Vignette + etc.). Le montant versé n'est **pas** déduit dans ce calcul : il est enregistré séparément à titre purement informatif/traçabilité de trésorerie.
- ✅ L'application est déployée **en une seule instance sur un seul VPS**, utilisée par plusieurs GIE (base de données partagée, filtrage par `gie_id`).
- ✅ **Un chauffeur ne conduit qu'un seul véhicule à la fois** (affectation unique et exclusive), mais il **peut changer de véhicule** au cours du temps. Chaque changement doit être tracé dans `HistoriqueAffectation` (date de début, date de fin) pour garder l'historique complet des versements liés à chaque couple véhicule/chauffeur passé.
  - Contrainte technique : un véhicule ne peut avoir qu'un seul chauffeur **actif** à un instant T, et un chauffeur ne peut avoir qu'un seul véhicule **actif** à un instant T. Toute nouvelle affectation clôture automatiquement l'affectation précédente (met `dateFin` sur l'ancienne ligne d'historique).
- ✅ **Authentification par numéro de téléphone + code PIN à 4 chiffres** pour tous les utilisateurs (pas d'email/mot de passe classique), avec protection anti brute-force obligatoire (voir module Authentification).

## QUESTIONS ENCORE OUVERTES À CLARIFIER AVEC L'IA AVANT DE CODER
- Les chauffeurs auront-ils un accès direct à l'application (lecture seule sur leur solde) ou uniquement les gestionnaires/admins saisissent-ils tout ?
- Faut-il un système de notifications (SMS/email) dès la V1 ou en V2 ?
