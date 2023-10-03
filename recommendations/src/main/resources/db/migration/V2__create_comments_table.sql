CREATE TABLE comments(
    id              SERIAL PRIMARY KEY NOT NULL,
    movie           BIGINT NOT NULL,
    comment_text    text NOT NULL,
    rating          INT NOT NULL,
    FOREIGN KEY(movie) 
      REFERENCES movies(id)
);

INSERT INTO comments(movie, comment_text, rating) VALUES 
(1, 'Very good movie!', 5),
(2, 'Great acting.', 5),
(3, 'A bit boring but enjoyable', 4),
(4, 'Absolutely trash', 1),
(5, 'Very good movie!', 3);