CREATE TABLE songs(
    id         SERIAL PRIMARY KEY NOT NULL,
    name       VARCHAR(255) NOT NULL,
    singer     VARCHAR(255) NOT NULL,
    genre      VARCHAR(255) NOT NULL
);

INSERT INTO songs(name, singer, genre) VALUES
('Highway to hell', 'AC/DC', 'Rock'),
('Fur Elise', 'Beethoven', 'Classical'),
('Umbrella', 'Rihanna', 'Pop');