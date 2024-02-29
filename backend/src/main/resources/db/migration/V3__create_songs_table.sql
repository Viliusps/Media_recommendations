CREATE TABLE songs(
    id         SERIAL PRIMARY KEY NOT NULL,
    name       VARCHAR(255) NOT NULL,
    singer     VARCHAR(255) NOT NULL
);

INSERT INTO songs(name, singer) VALUES
('Highway to hell', 'AC/DC'),
('Fur Elise', 'Beethoven'),
('Umbrella', 'Rihanna');