ALTER TABLE movies
ADD COLUMN popularity int DEFAULT 0;

ALTER TABLE songs
ADD COLUMN popularity int DEFAULT 0;

ALTER TABLE games
ADD COLUMN popularity int DEFAULT 0;