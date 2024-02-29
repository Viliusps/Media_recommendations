CREATE TABLE recommendations(
    id          SERIAL PRIMARY KEY NOT NULL,
    first       BIGINT NOT NULL,
    second      BIGINT NOT NULL,
    first_type  VARCHAR(10) NOT NULL,
    second_type VARCHAR(10) NOT NULL,
    rating      BOOLEAN NOT NULL,
    date        DATE NOT NULL
);