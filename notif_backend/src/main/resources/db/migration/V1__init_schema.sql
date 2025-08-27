-- Table: template
CREATE TABLE template (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          type VARCHAR(255),
                          text VARCHAR(1000),
                          titre VARCHAR(255),
                          langue VARCHAR(255)
);

-- Table: utilisateur
CREATE TABLE utilisateur (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             departement VARCHAR(255),
                             email VARCHAR(255),
                             nom VARCHAR(255),
                             password VARCHAR(255),
                             tel VARCHAR(255),
                             role VARCHAR(255)
);

-- Table: attachement
CREATE TABLE attachement (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             file_name VARCHAR(255),
                             path VARCHAR(255)
);

-- Table: destinataire
CREATE TABLE destinataire (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              email VARCHAR(255),
                              tel VARCHAR(255),
                              type VARCHAR(255),
                              nom_complet VARCHAR(255),
                              organisation VARCHAR(255),
                              adresse TEXT
);

-- Table: envoi
CREATE TABLE envoi (
                       numero_envoi BIGINT AUTO_INCREMENT PRIMARY KEY,
                       date_envoi DATE,
                       date_prevue_envoi DATE,
                       forme VARCHAR(255)
);

-- Table: notification
CREATE TABLE notification (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              contenu TEXT,
                              date_creation VARCHAR(255),
                              objet VARCHAR(255),
                              signataire VARCHAR(255),
                              envoi_id BIGINT,
                              template_id BIGINT,
                              utilisateur_id BIGINT,
                              destinataire_id BIGINT,
                              etat VARCHAR(20) DEFAULT 'CREATED',
                              FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id),
                              FOREIGN KEY (template_id) REFERENCES template(id),
                              FOREIGN KEY (envoi_id) REFERENCES envoi(numero_envoi) ON DELETE SET NULL,
                              FOREIGN KEY (destinataire_id) REFERENCES destinataire(id)
);

-- Table: retour_envoi
CREATE TABLE retour_envoi (
                              reference_envoi BIGINT AUTO_INCREMENT PRIMARY KEY,
                              date_retour VARCHAR(255),
                              motif VARCHAR(255),
                              envoi_id BIGINT UNIQUE,
                              FOREIGN KEY (envoi_id) REFERENCES envoi(numero_envoi)
);

-- Table de jointure: envoi_destinataires
CREATE TABLE envoi_destinataires (
                                     envois_numero_envoi BIGINT NOT NULL,
                                     destinataires_id BIGINT NOT NULL,
                                     FOREIGN KEY (envois_numero_envoi) REFERENCES envoi(numero_envoi),
                                     FOREIGN KEY (destinataires_id) REFERENCES destinataire(id)
);

-- Table de jointure: notification_attachements
CREATE TABLE notification_attachements (
                                           notification_id BIGINT NOT NULL,
                                           attachements_id BIGINT NOT NULL,
                                           FOREIGN KEY (notification_id) REFERENCES notification(id),
                                           FOREIGN KEY (attachements_id) REFERENCES attachement(id)
);
