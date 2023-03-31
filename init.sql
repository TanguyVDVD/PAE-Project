------------- VERSION 1.3 --------------

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
    is_helper     BOOLEAN      NOT NULL,
    version_number INTEGER NOT NULL
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
                             time_slot VARCHAR(15) NOT NULL,
                             price FLOAT,
                             state VARCHAR(15),
                             status VARCHAR(15),
                             reason_for_refusal VARCHAR(50),
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
                             version_number INTEGER NOT NULL,
                             id_user INTEGER REFERENCES pae.users,
                             receipt_date INTEGER REFERENCES pae.availability NOT NULL,
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


INSERT INTO pae.users VALUES (DEFAULT, 'Riez', 'Christophe', '0498183040', 'riez@ressourcerie.be', '$2a$12$zEcrRmtOvT3Kyx7Jc.YYPu7rf3BKfDEjlNZXbEY9QVs6dqzOprEWi', false, '2023-02-01', true, 1);
INSERT INTO pae.users VALUES (DEFAULT, 'Vdv', 'Constantine', '0498183041', 'constantine@ressourcerie.be', '$2a$12$zEcrRmtOvT3Kyx7Jc.YYPu7rf3BKfDEjlNZXbEY9QVs6dqzOprEWi', false, '2023-02-01', true, 1);
INSERT INTO pae.users VALUES (DEFAULT, 'lsh', 'Bernard', '0498183042', 'bernard@ressourcerie.be', '$2a$12$zEcrRmtOvT3Kyx7Jc.YYPu7rf3BKfDEjlNZXbEY9QVs6dqzOprEWi', false, '2023-02-01', true, 1);
INSERT INTO pae.users VALUES (DEFAULT, 'bdh', 'Lise', '0498183043', 'lise@ressourcerie.be', '$2a$12$zEcrRmtOvT3Kyx7Jc.YYPu7rf3BKfDEjlNZXbEY9QVs6dqzOprEWi', false, '2023-02-01', false, 1);

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

INSERT INTO pae.objects VALUES (DEFAULT, 'Garde robe'             , true, 'matin'     , null, 'proposé' , null     , null               , true , '2023-02-15', null        , null        , null        , null        , null        , null        , null        , null        , 1, 2   , 1 , 1);
INSERT INTO pae.objects VALUES (DEFAULT, 'Étagère en chêne massif', true, 'après-midi', null, 'proposé' , null     , null               , true , '2023-03-21', null        , null        , null        , null        , null        , null        , null        , null        , 1, 3   , 3 , 1);
INSERT INTO pae.objects VALUES (DEFAULT, 'Canapé bon état'        , true, 'matin'     , 6   , 'en vente', 'accepté', null               , true , '2023-02-15', '2023-02-16', null        , '2023-02-17', '2023-02-18', '2023-02-20', null        , null        , null        , 1, 4   , 5 , 4);
INSERT INTO pae.objects VALUES (DEFAULT, 'Chaise cuisine'         , true, 'après-midi', null, 'refusé'  , 'refusé' , 'trop mauvais etat', true , '2023-02-24', null        , '2023-02-27', null        , null        , null        , null        , null        , '0472369482', 1, null, 7 , 3);
INSERT INTO pae.objects VALUES (DEFAULT, 'Table de cuisine'       , true, 'matin'     , 9   , 'vendu'   , 'accepté', null               , true , '2023-02-15', '2023-02-17', null        , null        , '2023-02-18', '2023-02-20', '2023-03-10', null        , null        , 1, 2   , 13, 2);

