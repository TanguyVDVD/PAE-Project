------------- VERSION 1.0 --------------

-------------- INIT DB -----------------

DROP SCHEMA IF EXISTS pae CASCADE;
CREATE SCHEMA pae;


----------- CREATE TABLES --------------


CREATE TABLE pae.users
(
    id_user       SERIAL PRIMARY KEY,
    last_name     VARCHAR(50)  NOT NULL,
    first_name    VARCHAR(50)  NOT NULL,
    phone_number  VARCHAR(12)  NOT NULL,
    email         VARCHAR(100) NOT NULL,
    password      VARCHAR(100) NOT NULL,
    photo         BOOLEAN      NOT NULL,
    register_date DATE         NOT NULL,
    is_helper     BOOLEAN      NOT NULL
);

CREATE TABLE pae.object_types
(
    id_object_type SERIAL PRIMARY KEY,
    label          VARCHAR(25)
);

CREATE TABLE pae.availability
(
    id_availability SERIAL PRIMARY KEY,
    date            DATE NOT NULL
);

CREATE TABLE pae.objects (
                             id_object SERIAL PRIMARY KEY,
                             description VARCHAR(120) NOT NULL,
                             photo BOOLEAN NOT NULL,
                             is_visible BOOLEAN NOT NULL ,
                             price FLOAT,
                             state VARCHAR(15),
                             acceptance_date DATE,
                             workshop_date DATE,
                             deposit_date DATE,
                             selling_date DATE,
                             withdrawal_date DATE,
                             time_slot VARCHAR(15) NOT NULL,
                             status VARCHAR(15),
                             reason_for_refusal VARCHAR(50),
                             phone_number VARCHAR(12),
                             pickup_date INTEGER REFERENCES pae.availability NOT NULL,
                             id_user INTEGER REFERENCES pae.users,
                             id_object_type INTEGER REFERENCES pae.object_types NOT NULL
);

CREATE TABLE pae.notifications
(
    id_notification   SERIAL PRIMARY KEY,
    notification_text VARCHAR(50)                    NOT NULL,
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


INSERT INTO pae.users VALUES (DEFAULT, 'Riez', 'Christophe', '0498183040', 'riez@ressourcerie.be', '$2a$12$zEcrRmtOvT3Kyx7Jc.YYPu7rf3BKfDEjlNZXbEY9QVs6dqzOprEWi', false, '2023-02-01', true);
INSERT INTO pae.users VALUES (DEFAULT, 'Vdv', 'Constantine', '0498183041', 'constantine@ressourcerie.be', '$2a$12$zEcrRmtOvT3Kyx7Jc.YYPu7rf3BKfDEjlNZXbEY9QVs6dqzOprEWi', false, '2023-02-01', true);
INSERT INTO pae.users VALUES (DEFAULT, 'lsh', 'Bernard', '0498183042', 'bernard@ressourcerie.be', '$2a$12$zEcrRmtOvT3Kyx7Jc.YYPu7rf3BKfDEjlNZXbEY9QVs6dqzOprEWi', false, '2023-02-01', true);
INSERT INTO pae.users VALUES (DEFAULT, 'bdh', 'Lise', '0498183043', 'lise@ressourcerie.be', '$2a$12$zEcrRmtOvT3Kyx7Jc.YYPu7rf3BKfDEjlNZXbEY9QVs6dqzOprEWi', false, '2023-02-01', false);

INSERT INTO pae.object_types VALUES (DEFAULT,'Meuble');
INSERT INTO pae.object_types VALUES (DEFAULT,'Table');
INSERT INTO pae.object_types VALUES (DEFAULT,'Chaise');
INSERT INTO pae.object_types VALUES (DEFAULT,'Fauteuil');
INSERT INTO pae.object_types VALUES (DEFAULT,'Lit/sommier');
INSERT INTO pae.object_types VALUES (DEFAULT,'Matelas');
INSERT INTO pae.object_types VALUES (DEFAULT,'Couvertures');
INSERT INTO pae.object_types VALUES (DEFAULT,'Matériel de cuisine');
INSERT INTO pae.object_types VALUES (DEFAULT,'Vaisselle');

INSERT INTO pae.availability VALUES (DEFAULT, '2023-04-08');
INSERT INTO pae.availability VALUES (DEFAULT, '2023-04-09');
INSERT INTO pae.availability VALUES (DEFAULT, '2023-04-10');
INSERT INTO pae.availability VALUES (DEFAULT, '2023-04-11');
INSERT INTO pae.availability VALUES (DEFAULT, '2023-04-12');
INSERT INTO pae.availability VALUES (DEFAULT, '2023-04-15');
INSERT INTO pae.availability VALUES (DEFAULT, '2023-04-16');
INSERT INTO pae.availability VALUES (DEFAULT, '2023-04-28');
INSERT INTO pae.availability VALUES (DEFAULT, '2023-04-29');
INSERT INTO pae.availability VALUES (DEFAULT, '2023-05-08');
INSERT INTO pae.availability VALUES (DEFAULT, '2023-05-09');
INSERT INTO pae.availability VALUES (DEFAULT, '2023-05-10');
INSERT INTO pae.availability VALUES (DEFAULT, '2023-05-11');
INSERT INTO pae.availability VALUES (DEFAULT, '2023-05-12');
INSERT INTO pae.availability VALUES (DEFAULT, '2023-05-15');
INSERT INTO pae.availability VALUES (DEFAULT, '2023-05-16');
INSERT INTO pae.availability VALUES (DEFAULT, '2023-05-28');
INSERT INTO pae.availability VALUES (DEFAULT, '2023-05-29');

INSERT INTO pae.objects VALUES (DEFAULT, 'Canapé bon état', true, true, 6, 'en vente', '2023-02-15', '2023-02-16', '2023-02-17', null, null, 'matin', 'accepté', null, null, 1, 3, 1);
INSERT INTO pae.objects VALUES (DEFAULT, 'Garde robe', true, true, 6, 'proposé', null, null, null, null, null, 'matin', 'accepté', null, null, 1, 3, 1);
INSERT INTO pae.objects VALUES (DEFAULT, 'Chaise cuisine', true, false, null, 'refusé', null, null, null, null, null, 'après-midi', 'refusé', 'trop mauvais etat', '+32412369482', 2, null, 3);
INSERT INTO pae.objects VALUES (DEFAULT, 'Table de cuisine', true, false, 9, 'vendu', '2023-02-15', '2023-02-17', '2023-02-18', '2023-03-20', null, 'matin', 'accepté', null, null, 1, 3, 1);
