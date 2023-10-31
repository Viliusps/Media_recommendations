INSERT INTO roles (role) VALUES
('USER'),
('ADMIN');

INSERT INTO users (id,username,email,password, role)
VALUES (1, 'testAdmin', 'testAdmin@gmail.com', '$2a$10$ZLPv4sv5n7DE6aIF6ZAgyuxOQJbpW8OSFGcn5v1uyAJ/7ugRRmoMK', 'ADMIN');
INSERT INTO users (id,username,email,password, role)
VALUES (2, 'testUser', 'testUser@gmail.com', '$2a$10$X76wy2bATaK4LHXT6KOORu3aRJyfoj49ZBgNuSjtUPiCktaYBkRDS', 'USER');

SELECT SETVAL('public.users_id_seq', COALESCE(MAX(id), 1) ) FROM public.users;