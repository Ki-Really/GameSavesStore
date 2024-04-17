/*insert into role(name) values ('ROLE_USER'),('ROLE_ADMIN') on conflict do nothing;
insert into game_state_parameter_type(type) values ('type1'),
                                                   ('type2'),
                                                   ('type3'),
                                                   ('type4') on conflict do nothing ;
insert into person(username,email,password,fk_role,is_blocked) values('testo','testoprotesto123321@gmail.com','$2a$10$y3k9MF5fU0qOHENqLWlfzuthoREI6Fn9Scx8GKlSL3YN4pX76tVYq',2,false),
                                                                     ('user','user@gmail.com','$2a$10$hj7dZQNIKWL21XhahiMwf.uyIyVQQ0m4eSJTYSBFSRXC7LNzieWo.',1,false) on conflict do nothing ;
*/


INSERT INTO role(name)
SELECT 'ROLE_USER'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ROLE_USER');

INSERT INTO role(name)
SELECT 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ROLE_ADMIN');

-- Вставка данных в таблицу game_state_parameter_type
INSERT INTO game_state_parameter_type(type)
SELECT 'type1'
WHERE NOT EXISTS (SELECT 1 FROM game_state_parameter_type WHERE type = 'type1');

INSERT INTO game_state_parameter_type(type)
SELECT 'type2'
WHERE NOT EXISTS (SELECT 1 FROM game_state_parameter_type WHERE type = 'type2');

INSERT INTO game_state_parameter_type(type)
SELECT 'type3'
WHERE NOT EXISTS (SELECT 1 FROM game_state_parameter_type WHERE type = 'type3');

INSERT INTO game_state_parameter_type(type)
SELECT 'type4'
WHERE NOT EXISTS (SELECT 1 FROM game_state_parameter_type WHERE type = 'type4');



INSERT INTO game_state_parameter_type(type)
SELECT 'time_seconds'
WHERE NOT EXISTS (SELECT 1 FROM game_state_parameter_type WHERE type = 'time_seconds');

INSERT INTO game_state_parameter_type(type)
SELECT 'time_hours'
WHERE NOT EXISTS (SELECT 1 FROM game_state_parameter_type WHERE type = 'time_hours');

INSERT INTO game_state_parameter_type(type)
SELECT 'time_ms'
WHERE NOT EXISTS (SELECT 1 FROM game_state_parameter_type WHERE type = 'time_ms');

INSERT INTO game_state_parameter_type(type)
SELECT 'time_minutes'
WHERE NOT EXISTS (SELECT 1 FROM game_state_parameter_type WHERE type = 'time_minutes');

INSERT INTO game_state_parameter_type(type)
SELECT 'gender'
WHERE NOT EXISTS (SELECT 1 FROM game_state_parameter_type WHERE type = 'gender');

INSERT INTO game_state_parameter_type(type)
SELECT 'male'
WHERE NOT EXISTS (SELECT 1 FROM game_state_parameter_type WHERE type = 'male');

INSERT INTO game_state_parameter_type(type)
SELECT 'female'
WHERE NOT EXISTS (SELECT 1 FROM game_state_parameter_type WHERE type = 'female');


-- Вставка данных в таблицу person
INSERT INTO person(username, email, password, fk_role, is_blocked)
SELECT 'testo', 'testoprotesto123321@gmail.com', '$2a$10$y3k9MF5fU0qOHENqLWlfzuthoREI6Fn9Scx8GKlSL3YN4pX76tVYq', 2, false
WHERE NOT EXISTS (SELECT 1 FROM person WHERE username = 'testo');

INSERT INTO person(username, email, password, fk_role, is_blocked)
SELECT 'user', 'user@gmail.com', '$2a$10$hj7dZQNIKWL21XhahiMwf.uyIyVQQ0m4eSJTYSBFSRXC7LNzieWo.', 1, false
WHERE NOT EXISTS (SELECT 1 FROM person WHERE username = 'user');