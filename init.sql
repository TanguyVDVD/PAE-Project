------------- VERSION 1.5 --------------

-------------- INIT DB -----------------

DROP SCHEMA IF EXISTS pae CASCADE;
CREATE SCHEMA pae;


----------- CREATE TABLES --------------


CREATE TABLE pae.users
(
    id_user        SERIAL PRIMARY KEY,
    version_number INTEGER NOT NULL,
    last_name      VARCHAR(50)  NOT NULL,
    first_name     VARCHAR(50)  NOT NULL,
    phone_number   VARCHAR(12)  NOT NULL,
    email          VARCHAR(100) NOT NULL,
    password       VARCHAR(100) NOT NULL,
    photo          BOOLEAN      NOT NULL,
    register_date  DATE         NOT NULL,
    role           VARCHAR(12)  NOT NULL
);

CREATE TABLE pae.object_types
(
    id_object_type SERIAL PRIMARY KEY,
    label          VARCHAR(25)
);

CREATE TABLE pae.availabilities
(
    id_availability SERIAL PRIMARY KEY,
    date            DATE NOT NULL
);

CREATE TABLE pae.objects (
                             id_object SERIAL PRIMARY KEY,
                             version_number INTEGER NOT NULL,
                             description VARCHAR(120) NOT NULL,
                             time_slot VARCHAR(15) NOT NULL,
                             price FLOAT,
                             state VARCHAR(15),
                             status VARCHAR(15),
                             reason_for_refusal VARCHAR(120),
                             is_visible BOOLEAN NOT NULL,
                             offer_date DATE NOT NULL,
                             acceptance_date DATE,
                             refusal_date DATE,
                             workshop_date DATE,
                             deposit_date DATE,
                             on_sale_date DATE,
                             selling_date DATE,
                             withdrawal_date DATE,
                             phone_number VARCHAR(12),
                             id_user INTEGER REFERENCES pae.users,
                             receipt_date INTEGER REFERENCES pae.availabilities NOT NULL,
                             id_object_type INTEGER REFERENCES pae.object_types NOT NULL
);

CREATE TABLE pae.notifications
(
    id_notification   SERIAL PRIMARY KEY,
    notification_text VARCHAR(200)                    NOT NULL,
    id_object         INTEGER REFERENCES pae.objects NOT NULL
);

CREATE TABLE pae.users_notifications
(
    read            BOOLEAN                              NOT NULL,
    id_notification INTEGER REFERENCES pae.notifications NOT NULL,
    id_user         INTEGER REFERENCES pae.users         NOT NULL,
    PRIMARY KEY (id_notification, id_user)
);


--------------- SEED ------------------


INSERT INTO pae.users VALUES (DEFAULT, 1, 'Riez', 'Christophe', '0498183040', 'riez@ressourcerie.be', '$2a$12$zEcrRmtOvT3Kyx7Jc.YYPu7rf3BKfDEjlNZXbEY9QVs6dqzOprEWi', true, '2023-02-01', 'responsable');
INSERT INTO pae.users VALUES (DEFAULT, 1, 'Vdv', 'Constantine', '0498183041', 'constantine@ressourcerie.be', '$2a$12$zEcrRmtOvT3Kyx7Jc.YYPu7rf3BKfDEjlNZXbEY9QVs6dqzOprEWi', true, '2023-02-01', 'aidant');
INSERT INTO pae.users VALUES (DEFAULT, 1, 'lsh', 'Bernard', '0498183042', 'bernard@ressourcerie.be', '$2a$12$zEcrRmtOvT3Kyx7Jc.YYPu7rf3BKfDEjlNZXbEY9QVs6dqzOprEWi', true, '2023-02-01', 'aidant');
INSERT INTO pae.users VALUES (DEFAULT, 1, 'bdh', 'Lise', '0498183043', 'lise@ressourcerie.be', '$2a$12$zEcrRmtOvT3Kyx7Jc.YYPu7rf3BKfDEjlNZXbEY9QVs6dqzOprEWi', true, '2023-02-01', 'utilisateur');

INSERT INTO pae.object_types VALUES (DEFAULT,'Meuble');
INSERT INTO pae.object_types VALUES (DEFAULT,'Table');
INSERT INTO pae.object_types VALUES (DEFAULT,'Chaise');
INSERT INTO pae.object_types VALUES (DEFAULT,'Fauteuil');
INSERT INTO pae.object_types VALUES (DEFAULT,'Lit/sommier');
INSERT INTO pae.object_types VALUES (DEFAULT,'Matelas');
INSERT INTO pae.object_types VALUES (DEFAULT,'Couvertures');
INSERT INTO pae.object_types VALUES (DEFAULT,'Matériel de cuisine');
INSERT INTO pae.object_types VALUES (DEFAULT,'Vaisselle');

INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-03-04');
INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-03-11');
INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-03-18');
INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-03-25');
INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-04-01');
INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-04-15');
INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-04-22');

INSERT INTO pae.objects
VALUES (DEFAULT, 1, 'Chaise en bois brut avec cousin beige', 'matin', 2, 'en vente', 'accepté', null,
        true, '2023-03-01', '2023-03-15', null, null, '2023-03-18', '2023-03-20', null, null, null, 2, 3, 3);

INSERT INTO pae.objects
VALUES (DEFAULT, 1, 'Canapé 3 places blanc', 'matin', 3, 'vendu', 'accepté', null,
        true, '2023-03-01', '2023-03-15', null, null, '2023-03-18', '2023-03-20', '2023-03-22', null, null, 3, 3, 4);

INSERT INTO pae.objects
VALUES (DEFAULT, 1, 'Secrétaire', 'après-midi', null, 'refusé', 'refusé', 'Ce meuble est magnifique mais fragile pour l’usage qui en sera fait.',
        true, '2023-03-01', null, '2023-03-04', null, null, null, null, null, '0496321654', null, 4, 1);

INSERT INTO pae.objects
VALUES (DEFAULT, 1, '100 assiettes blanches', 'après-midi', null, 'accepté', 'accepté', null,
        true, '2023-03-01', '2023-03-20', null, null, null, null, null, null, null, 4, 4, 9);

INSERT INTO pae.objects
VALUES (DEFAULT, 1, 'Grand canapé 4 places bleu usé', 'après-midi', null, 'accepté', 'accepté', null,
        true, '2023-03-01', '2023-03-20', null, null, null, null, null, null, null, 2, 4, 4);

INSERT INTO pae.objects
VALUES (DEFAULT, 1, 'Fauteuil design très confortable', 'après-midi', null, 'proposé', null, null,
        true, '2023-03-01', null, null, null, null, null, null, null, null, 3, 5, 4);

INSERT INTO pae.objects
VALUES (DEFAULT, 1, 'Tabouret de bar en cuir', 'après-midi', null, 'proposé', null, null,
        true, '2023-03-01', null, null, null, null, null, null, null, null, 2, 5, 3);

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA pae TO manal_hebbal;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA pae TO erman_sayin;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA pae TO louis_vanbel;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA pae TO tanguy_vandervelden;