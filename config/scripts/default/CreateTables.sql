CREATE TABLE users (
	id integer PRIMARY KEY,
	name text,
	name_as_tag text,
	email_address text,
	"password" text,
	rights integer,
	"language" integer,
	notify_about_praise boolean,
	home_page text,
	gender integer,
	status integer,
	confirmation_code bigint,
	photo integer,
	creation_time bigint
);

CREATE TABLE photos (
	id integer PRIMARY KEY,
	owner_id integer REFERENCES users(id),
	owner_name text,
	owner_notify_about_praise boolean,
	owner_email_address text,
	owner_language integer,
	owner_home_page text,
	width integer,
	height integer,
	tags text,
	status integer,
	praise_sum integer,
	no_votes integer,
	creation_time bigint
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

INSERT INTO users (id, name, name_as_tag, email_address, "password", rights, status)
	VALUES (1, 'admin', 'admin', 'root@localhost', 'admin', 4, 1);

