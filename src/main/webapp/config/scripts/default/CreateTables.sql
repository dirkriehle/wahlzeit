CREATE TABLE users (
   id serial PRIMARY KEY,
   name text UNIQUE,
   creation_time bigint,
   email_address text,
   "password" text,
   rights integer,
   name_as_tag text,
   "language" integer,
   notify_about_praise boolean,
   home_page text,
   gender integer,
   status integer,
   confirmation_code bigint,
   photo integer
);

CREATE TABLE photos (
    id serial PRIMARY KEY,
    creation_time bigint,
    owner_id integer REFERENCES users(id),
    status integer,
    width integer,
    height integer,
    tags text,
    praise_sum integer,
    no_votes integer
);

CREATE TABLE tags (
	tag text,
	photo_id integer
);

CREATE TABLE cases (
	id integer PRIMARY KEY,
	photo integer,
	flagger text,
	reason integer,
	explanation text,
	creation_time bigint,
	was_decided boolean,
	decision_time bigint
);

CREATE TABLE globals (
	id integer PRIMARY KEY,
	last_user_id integer,
	last_photo_id integer,
	last_case_id integer,
	last_session_id integer
);

INSERT INTO globals (id, last_user_id, last_photo_id, last_case_id, last_session_id)
	VALUES (0, 1, 0, 0, 0);

INSERT INTO users (id, creation_time, name, name_as_tag, email_address, "password", rights, status)
	VALUES (0, 0, 'admin', 'admin', 'root@localhost', 'admin', 4, 1);

