------------- VERSION 1.2 --------------

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


INSERT INTO pae.object_types VALUES (DEFAULT, 'Meuble');
INSERT INTO pae.object_types VALUES (DEFAULT, 'Table');
INSERT INTO pae.object_types VALUES (DEFAULT, 'Chaise');
INSERT INTO pae.object_types VALUES (DEFAULT, 'Fauteuil');
INSERT INTO pae.object_types VALUES (DEFAULT, 'Lit/sommier');
INSERT INTO pae.object_types VALUES (DEFAULT, 'Matelas');
INSERT INTO pae.object_types VALUES (DEFAULT, 'Couvertures');
INSERT INTO pae.object_types VALUES (DEFAULT, 'Matériel de cuisine');
INSERT INTO pae.object_types VALUES (DEFAULT, 'Vaisselle');

INSERT INTO pae.users
VALUES (DEFAULT, 1, 'Riez', 'Robert', '0477968547', 'bert.riez@gmail.be',
        '$2a$10$61RLCqTVMfFoqZyC3CqJS.IqvdGQgr96wcFuP7WMJWtf4su3Mn0iu', true, '2023-05-01', 'responsable');

INSERT INTO pae.users
VALUES (DEFAULT, 1, 'Muise', 'Alfred', '0476963626', 'fred.muise@gmail.be',
        '$2a$10$JYWLAigic6ES55nzD3rtI.UUNqhy9UJBIVv9pdDONmYashdZ1yG6W', true, '2023-05-01', 'aidant');

INSERT INTO pae.users
VALUES (DEFAULT, 1, 'Line', 'Charles', '0481356249', 'charline@proximus.be',
        '$2a$10$z6uNgpXpNZWTsETg3KOOEevQn5BxnMQabiv9m6Xx5x4IbZdi51odi', true, '2023-05-01', 'aidant');

INSERT INTO pae.users
VALUES (DEFAULT, 1, 'Line', 'Caroline', '0487452379', 'caro.line@hotmail.com',
        '$2a$10$TR/BR7l7g8RfFfArewiyTumbCvZ10ol1Hcs9zp7oQfBw65zdRLTVS', true, '2023-05-01', 'utilisateur');

INSERT INTO pae.users
VALUES (DEFAULT, 1, 'Ile', 'Achille', '0477653224', 'ach.ile@gmail.com',
        '$2a$10$TR/BR7l7g8RfFfArewiyTumbCvZ10ol1Hcs9zp7oQfBw65zdRLTVS', true, '2023-05-01', 'utilisateur');

INSERT INTO pae.users
VALUES (DEFAULT, 1, 'Ile', 'Basile', '0485988642', 'bas.ile@gmail.be',
        '$2a$10$TR/BR7l7g8RfFfArewiyTumbCvZ10ol1Hcs9zp7oQfBw65zdRLTVS', true, '2023-05-01', 'utilisateur');

INSERT INTO pae.users
VALUES (DEFAULT, 1, 'Ile', 'Théophile', '0488353389', 'theo.phile@proximus.be',
        '$2a$10$TR/BR7l7g8RfFfArewiyTumbCvZ10ol1Hcs9zp7oQfBw65zdRLTVS', true, '2023-05-01', 'utilisateur');

INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-03-04');

INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-03-11');

INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-03-18');

INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-03-25');

INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-04-01');

INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-04-15');

INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-04-22');

INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-04-29');

INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-05-13');

INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-05-27');

INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-06-03');

INSERT INTO pae.availabilities VALUES (DEFAULT, '2023-06-17');


INSERT INTO pae.objects
VALUES (DEFAULT, 1, 'Chaise en bois brut avec coussin beige', 'matin', 2, 'en vente', 'accepté', null,
        true, '2023-03-01', '2023-03-15', null, null, '2023-03-23', '2023-03-23', null, null, null, 6, 3, 3);

INSERT INTO pae.objects
VALUES (DEFAULT, 1, 'Canapé 3 places blanc', 'matin', 3, 'vendu', 'accepté', null,
        true, '2023-03-02', '2023-03-15', null, null, '2023-03-23', '2023-03-23', '2023-03-23', null, null, 6, 3, 4);

INSERT INTO pae.objects
VALUES (DEFAULT, 1, 'Secrétaire', 'après-midi', null, 'refusé', 'refusé', 'Ce meuble est magnifique mais fragile pour l’usage qui en sera fait.',
        true, '2023-03-03', null, '2023-03-15', null, null, null, null, null, '0496321654', null, 4, 1);

INSERT INTO pae.objects
VALUES (DEFAULT, 1, '100 assiettes blanches', 'après-midi', null, 'en magasin', 'accepté', null,
        true, '2023-03-08', '2023-03-20', null, null, '2023-03-29', null, null, null, null, 5, 4, 9);

INSERT INTO pae.objects
VALUES (DEFAULT, 1, 'Grand canapé 4 places bleu usé', 'après-midi', 3.5, 'vendu', 'accepté', null,
        true, '2023-03-08', '2023-03-20', null, null, '2023-03-29', '2023-03-29', '2023-03-29', null, null, 5, 4, 4);

INSERT INTO pae.objects
VALUES (DEFAULT, 1, 'Fauteuil design très confortable', 'après-midi', 5.2, 'retiré', 'accepté', null,
        true, '2023-03-09', '2023-03-15', null, null, '2023-03-18', null, null, '2023-03-29', null, 5, 3, 4);

INSERT INTO pae.objects
VALUES (DEFAULT, 1, 'Tabouret de bar en cuir', 'après-midi', null, 'refusé', 'refusé', 'Ceci n''est pas une chaise',
        true, '2023-03-26', null, '2023-03-28', null, null, null, null, null, null, 5, 5, 3);

INSERT INTO pae.objects
VALUES (DEFAULT, 1, 'Fauteuil ancien, pieds et accoudoir en bois', 'matin', null, 'à l''atelier', 'accepté', null,
        true, '2023-04-07', '2023-04-11', null, '2023-04-22', null, null, null, null, null, 6, 7, 4);

INSERT INTO pae.objects
VALUES (DEFAULT, 1, '6 bols à soupe', 'matin', 3, 'en magasin', 'accepté', null,
        true, '2023-04-08', '2023-04-11', null, null, '2023-04-25', null, null, null, null, 6, 7, 9);

INSERT INTO pae.objects
VALUES (DEFAULT, 1, 'Lit cage blanc', 'après-midi', 3, 'en magasin', 'accepté', null,
        true, '2023-04-08', '2023-04-11', null, null, '2023-04-25', null, null, null, null, 7, 7, 1);

INSERT INTO pae.objects
VALUES (DEFAULT, 1, '30 pots à épices', 'matin', 3, 'en magasin', 'accepté', null,
        true, '2023-04-12', '2023-04-18', null, null, '2023-05-05', null, null, null, null, 7, 8, 9);

INSERT INTO pae.objects
VALUES (DEFAULT, 1, '4 tasses à café et leurs sous-tasses', 'matin', 3, 'en magasin', 'accepté', null,
        true, '2023-04-15', '2023-04-18', null, null, '2023-05-05', null, null, null, null, 4, 8, 9);


---------------Requests--------------

SELECT ob.state as "État", count(ob.id_object) AS "Nombre d'objets par état"
FROM pae.objects ob
group by ob.state;

SELECT obt.id_object_type as "id", obt.label as "Type", count(o.id_object_type) AS "Nombre d'objets par type"
FROM pae.object_types obt LEFT OUTER JOIN pae.objects o
                                          ON obt.id_object_type = o.id_object_type
group by obt.id_object_type
order by obt.id_object_type;

SELECT u.id_user as "id", u.first_name as "Prénom", u.last_name as "Nom",
       count(o.id_object_type) AS "Nombre d'objets par utilisateur"
FROM pae.users u, pae.objects o
WHERE u.id_user = o.id_user
group by u.id_user
order by u.id_user;

SELECT o.id_object as "id", o.description as "Description", o.state as "État", o.offer_date "Date de proposition",
       a.date as "Date de réception", o.acceptance_date as "Date d'acceptation", o.refusal_date as "Date de refus",
       o.workshop_date as "Date de dépôt à l'atelier", o.deposit_date as "Date de dépôt en magasin",
       o.on_sale_date as "Date de mise en vente", o.selling_date as "Date de vente",
       o.withdrawal_date as "Date de retrait"
FROM pae.objects o, pae.availabilities a
WHERE a.id_availability = o.receipt_date
order by o.id_object;

SELECT count(o.offer_date) as "Date de propostition",
       count(o.receipt_date) as "Date de réception",
       count(o.workshop_date) as "Date de dépôt à l'atelier",
       count(o.deposit_date) as "Date de dépôt en magasin",
       count(o.on_sale_date) as "Date de mise en vente",
       count(o.selling_date) as "Date de vente",
       count(o.withdrawal_date) as "Date de retrait"
FROM pae.objects o;

SELECT u.role as "Rôle", count(u.role) as "Nombre d'utilisateur par rôle"
FROM pae.users u
group by u.role;