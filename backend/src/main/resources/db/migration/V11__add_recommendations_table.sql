CREATE TABLE recommendations(
    first       BIGINT NOT NULL,
    second      BIGINT NOT NULL,
    first_type  VARCHAR(10) NOT NULL,
    second_type VARCHAR(10) NOT NULL,
    rating      BOOLEAN NOT NULL,
    date        DATE NOT NULL
);

INSERT INTO recommendations(first, second, first_type, second_type, rating, date) VALUES (22, 2, 'movie', 'song', TRUE, '2024-02-18');