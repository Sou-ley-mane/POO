-- Schéma initial : multi-tenant partagé, filtrage par gie_id

CREATE TABLE gie (
    id                  BIGSERIAL PRIMARY KEY,
    nom                 VARCHAR(150)    NOT NULL,
    sigle               VARCHAR(30)     NOT NULL,
    logo_url            VARCHAR(500),
    couleur_primaire    VARCHAR(7)      NOT NULL DEFAULT '#0d6efd',
    couleur_secondaire  VARCHAR(7)      NOT NULL DEFAULT '#6c757d',
    couleur_accent      VARCHAR(7)      NOT NULL DEFAULT '#ffc107',
    adresse             VARCHAR(255),
    telephone           VARCHAR(20),
    email               VARCHAR(150),
    statut              VARCHAR(20)     NOT NULL DEFAULT 'ACTIF',
    date_creation       TIMESTAMP       NOT NULL DEFAULT now()
);

CREATE TABLE utilisateur (
    id                      BIGSERIAL PRIMARY KEY,
    gie_id                  BIGINT REFERENCES gie(id),
    nom                     VARCHAR(100)    NOT NULL,
    prenom                  VARCHAR(100)    NOT NULL,
    telephone               VARCHAR(20)     NOT NULL UNIQUE,
    pin_hash                VARCHAR(255)    NOT NULL,
    email                   VARCHAR(150),
    role                    VARCHAR(30)     NOT NULL,
    statut                  VARCHAR(20)     NOT NULL DEFAULT 'ACTIF',
    doit_changer_pin        BOOLEAN         NOT NULL DEFAULT TRUE,
    nombre_tentatives_echouees INTEGER      NOT NULL DEFAULT 0,
    date_blocage            TIMESTAMP,
    date_creation           TIMESTAMP       NOT NULL DEFAULT now()
);
CREATE INDEX idx_utilisateur_gie ON utilisateur(gie_id);

CREATE TABLE vehicule (
    id                  BIGSERIAL PRIMARY KEY,
    gie_id              BIGINT          NOT NULL REFERENCES gie(id),
    immatriculation     VARCHAR(20)     NOT NULL,
    marque              VARCHAR(80),
    modele              VARCHAR(80),
    annee               INTEGER,
    couleur             VARCHAR(50),
    statut              VARCHAR(20)     NOT NULL DEFAULT 'EN_CIRCULATION',
    chauffeur_actuel_id BIGINT,
    date_mise_en_service DATE,
    CONSTRAINT uq_vehicule_immat_gie UNIQUE (gie_id, immatriculation)
);
CREATE INDEX idx_vehicule_gie ON vehicule(gie_id);

CREATE TABLE chauffeur (
    id                      BIGSERIAL PRIMARY KEY,
    gie_id                  BIGINT      NOT NULL REFERENCES gie(id),
    nom                     VARCHAR(100) NOT NULL,
    prenom                  VARCHAR(100) NOT NULL,
    telephone               VARCHAR(20),
    numero_permis           VARCHAR(50),
    numero_cni              VARCHAR(50),
    date_inscription        DATE        NOT NULL DEFAULT CURRENT_DATE,
    statut                  VARCHAR(20) NOT NULL DEFAULT 'ACTIF',
    vehicule_affecte_id     BIGINT REFERENCES vehicule(id),
    periodicite_versement   VARCHAR(20) NOT NULL DEFAULT 'HEBDOMADAIRE',
    montant_attendu_par_periode NUMERIC(12,2) NOT NULL DEFAULT 0
);
CREATE INDEX idx_chauffeur_gie ON chauffeur(gie_id);

ALTER TABLE vehicule
    ADD CONSTRAINT fk_vehicule_chauffeur_actuel
    FOREIGN KEY (chauffeur_actuel_id) REFERENCES chauffeur(id);

CREATE TABLE type_frais (
    id          BIGSERIAL PRIMARY KEY,
    gie_id      BIGINT NOT NULL REFERENCES gie(id),
    libelle     VARCHAR(100) NOT NULL,
    montant     NUMERIC(12,2) NOT NULL DEFAULT 0,
    periodicite VARCHAR(20) NOT NULL DEFAULT 'MENSUELLE',
    actif       BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE INDEX idx_type_frais_gie ON type_frais(gie_id);

CREATE TABLE frais_vehicule (
    id              BIGSERIAL PRIMARY KEY,
    vehicule_id     BIGINT NOT NULL REFERENCES vehicule(id),
    type_frais_id   BIGINT NOT NULL REFERENCES type_frais(id),
    montant         NUMERIC(12,2) NOT NULL,
    date_echeance   DATE,
    statut          VARCHAR(20) NOT NULL DEFAULT 'EN_ATTENTE'
);
CREATE INDEX idx_frais_vehicule_vehicule ON frais_vehicule(vehicule_id);

CREATE TABLE versement (
    id                  BIGSERIAL PRIMARY KEY,
    gie_id              BIGINT NOT NULL REFERENCES gie(id),
    chauffeur_id        BIGINT NOT NULL REFERENCES chauffeur(id),
    vehicule_id         BIGINT NOT NULL REFERENCES vehicule(id),
    periode_debut       DATE NOT NULL,
    periode_fin         DATE NOT NULL,
    montant_attendu     NUMERIC(12,2) NOT NULL,
    montant_verse       NUMERIC(12,2) NOT NULL DEFAULT 0,
    reste               NUMERIC(12,2) NOT NULL,
    date_paiement       TIMESTAMP,
    mode_paiement       VARCHAR(20),
    statut              VARCHAR(20) NOT NULL DEFAULT 'IMPAYE',
    saisi_par           BIGINT REFERENCES utilisateur(id),
    date_creation       TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT uq_versement_periode UNIQUE (chauffeur_id, periode_debut, periode_fin)
);
CREATE INDEX idx_versement_gie ON versement(gie_id);
CREATE INDEX idx_versement_chauffeur ON versement(chauffeur_id);
CREATE INDEX idx_versement_vehicule ON versement(vehicule_id);

CREATE TABLE versement_frais (
    versement_id    BIGINT NOT NULL REFERENCES versement(id) ON DELETE CASCADE,
    frais_vehicule_id BIGINT NOT NULL REFERENCES frais_vehicule(id),
    PRIMARY KEY (versement_id, frais_vehicule_id)
);

CREATE TABLE historique_affectation (
    id              BIGSERIAL PRIMARY KEY,
    gie_id          BIGINT NOT NULL REFERENCES gie(id),
    vehicule_id     BIGINT NOT NULL REFERENCES vehicule(id),
    chauffeur_id    BIGINT NOT NULL REFERENCES chauffeur(id),
    date_debut      TIMESTAMP NOT NULL DEFAULT now(),
    date_fin        TIMESTAMP
);
CREATE INDEX idx_historique_vehicule ON historique_affectation(vehicule_id);
CREATE INDEX idx_historique_chauffeur ON historique_affectation(chauffeur_id);
