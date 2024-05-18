/*create schema if not exists cloud_game_saves;*/

create table if not exists game(
    id integer generated by default as identity primary key,
    name varchar not null unique,
    description varchar
);

create table if not exists path(
    id int primary key Generated By Default as identity,
    name varchar NOT NULL,
    fk_game_id int References Game(id) NOT NULL
    );

create table if not exists extraction_pipeline
(
    id integer generated by default as identity primary key,
    type varchar not null,
    input_filename varchar not null,
    output_filename varchar not null,
    fk_game_id integer not null REFERENCES game(id)
    );

create table if not exists image
(
    id integer generated by default as identity primary key,
    name varchar not null unique,
    fk_game_id integer not null unique references game(id)
    );

create table if not exists scheme
(
    id integer generated by default as identity primary key,
    filename varchar not null,
    fk_game_id integer not null references game(id)
    );

create table if not exists game_state_parameter_type
(
    id integer generated by default as identity primary key,
    type varchar not null unique
);

create table if not exists common_parameter
(
    id integer generated by default as identity primary key,
    label varchar not null unique,
    description varchar,
    fk_game_state_parameter_type integer not null references game_state_parameter_type(id)
);

create table if not exists game_state_parameter
(
    id integer generated by default as identity primary key,
    key varchar not null,
    label varchar not null,
    description varchar not null,
    fk_scheme_id integer not null references scheme(id),
    fk_game_state_parameter_type_id integer not null references game_state_parameter_type(id),
    fk_common_parameter_id integer references common_parameter(id)
    );

create table if not exists role
(
    id   integer generated by default as identity primary key,
    name varchar not null unique
);

create table if not exists person
(
    id integer generated by default as identity primary key,
    username varchar(100) not null unique,
    email varchar(100) not null unique,
    password varchar not null,
    fk_role integer not null references role(id),
    is_blocked boolean default false not null
    );

create table if not exists password_recovery_token
(
    id integer generated by default as identity primary key,
    token varchar not null,
    fk_person_id integer not null references person(id)
    );

create table if not exists game_state
(
    id integer generated by default as identity primary key,
    name varchar not null,
    local_path varchar not null,
    archive_name varchar not null,
    size_in_bytes bigint,
    fk_person_id integer not null references person(id),
    fk_game_id integer not null references game(id),
    is_public boolean not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    uploaded_at timestamp not null
    );

create table if not exists game_state_value
(
    id integer generated by default as identity primary key,
    fk_game_state_id integer not null references game_state(id),
    fk_game_state_parameter_id integer not null references game_state_parameter(id),
    value varchar not null
    );

create table if not exists shared_save
(
    id integer generated by default as identity primary key,
    fk_shared_with_id integer not null references person(id),
    fk_game_state_id integer not null references game_state(id)
);

create table if not exists graphic_common(
  id integer generated by default as identity primary key ,
  visual_type varchar not null,
  fk_common_parameter_id int REFERENCES common_parameter(id) NOT NULL
);

-- create table if not exists persistent_logins
-- (
--     username varchar(64) not null,
--     series varchar(64) not null primary key,
--     token varchar(64) not null,
--     last_used timestamp not null
-- );

CREATE TABLE IF NOT EXISTS SPRING_SESSION (
    PRIMARY_ID CHAR(36) NOT NULL,
    SESSION_ID CHAR(36) NOT NULL,
    CREATION_TIME BIGINT NOT NULL,
    LAST_ACCESS_TIME BIGINT NOT NULL,
    MAX_INACTIVE_INTERVAL INT NOT NULL,
    EXPIRY_TIME BIGINT NOT NULL,
    PRINCIPAL_NAME VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

CREATE UNIQUE INDEX IF NOT EXISTS SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX IF NOT EXISTS SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX IF NOT EXISTS SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE IF NOT EXISTS SPRING_SESSION_ATTRIBUTES (
    SESSION_PRIMARY_ID CHAR(36) NOT NULL,
    ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES BYTEA NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
);




