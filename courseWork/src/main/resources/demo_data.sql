insert into role(name) values ('ROLE_USER'),('ROLE_ADMIN') on conflict do nothing;
insert into game_state_parameter_type(type) values ('type1'),
                                                   ('type2'),
                                                   ('type3'),
                                                   ('type4') on conflict do nothing ;
insert into person(username,email,password,fk_role,is_blocked) values('testo','testoprotesto123321@gmail.com','$2a$10$y3k9MF5fU0qOHENqLWlfzuthoREI6Fn9Scx8GKlSL3YN4pX76tVYq',2,false),
                                                                     ('user','user@gmail.com','$2a$10$hj7dZQNIKWL21XhahiMwf.uyIyVQQ0m4eSJTYSBFSRXC7LNzieWo.',1,false) on conflict do nothing ;