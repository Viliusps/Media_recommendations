CREATE TABLE games(
    id              SERIAL PRIMARY KEY NOT NULL,
    name            VARCHAR(255) NOT NULL,
    release_date    VARCHAR(255) NOT NULL,
    rating          VARCHAR(255) NOT NULL,
    genre           VARCHAR(255) NOT NULL,
    playtime        INTEGER NOT NULL
);