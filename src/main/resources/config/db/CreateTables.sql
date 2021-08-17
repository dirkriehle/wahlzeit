CREATE TABLE users
(
    id            serial PRIMARY KEY,
    name          text UNIQUE,
    email_address text UNIQUE,
    creation_time bigint,
    password      text,
    rights        integer,
    language      text
);

CREATE TABLE photos
(
    id            serial PRIMARY KEY,
    creation_time bigint,
    owner_id      integer REFERENCES users (id),
    data          bytea,
    status        integer,
    width         integer,
    height        integer,
    tags          text,
    praise_sum    integer,
    no_votes      integer
);

CREATE TABLE tags
(
    tag      text,
    photo_id integer
);

CREATE TABLE cases
(
    id            serial PRIMARY KEY,
    photo_id      integer REFERENCES photos (id),
    flagger_id    integer REFERENCES users (id),
    reason        integer,
    explanation   text,
    creation_time bigint,
    was_decided   boolean,
    decision_time bigint
);

INSERT INTO users (id, creation_time, name, email_address, password, rights, language)
VALUES (0, 0, 'admin', 'root@localhost', 'admin', 4, 'de');

