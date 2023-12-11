CREATE TABLE movies (
    id SERIAL PRIMARY KEY NOT NULL,
    title VARCHAR(255) NOT NULL,
    year VARCHAR(255) NOT NULL,
    rated VARCHAR(255) NOT NULL,
    released VARCHAR(255),
    runtime VARCHAR(255),
    genre VARCHAR(255),
    director VARCHAR(255),
    writer VARCHAR(255),
    actors VARCHAR(255),
    plot VARCHAR(1000),
    language VARCHAR(255),
    country VARCHAR(255),
    awards VARCHAR(255),
    poster VARCHAR(255),
    metascore VARCHAR(255),
    imdb_rating VARCHAR(255),
    imdb_votes VARCHAR(255),
    imdb_id VARCHAR(255) NOT NULL,
    type VARCHAR(255),
    dvd VARCHAR(255),
    box_office VARCHAR(255),
    production VARCHAR(255),
    website VARCHAR(255),
    response VARCHAR(255) NOT NULL
);

CREATE TABLE movie_rating (
    id SERIAL PRIMARY KEY NOT NULL,
    movie BIGINT NOT NULL,
    source VARCHAR(255),
    value VARCHAR(255),
    FOREIGN KEY (movie) REFERENCES movies(id)
);

INSERT INTO movies (
    title, year, rated, released, runtime, genre, director, writer, actors, plot,
    language, country, awards, poster, metascore, imdb_rating, imdb_votes, imdb_id,
    type, dvd, box_office, production, website, response
)
VALUES (
    'The Shawshank Redemption', '1994', 'R', '14 Oct 1994', '142 min', 'Drama', 'Frank Darabont',
    'Stephen King (short story "Rita Hayworth and Shawshank Redemption"), Frank Darabont (screenplay)',
    'Tim Robbins, Morgan Freeman, Bob Gunton, William Sadler', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.',
    'English', 'USA', 'Nominated for 7 Oscars. Another 21 wins & 36 nominations.', 'poster_url', '80',
    '9.3', '2,295,112', 'tt0111161', 'Movie', '21 Oct 2014', '$28,341,469', 'Columbia Pictures',
    'http://www.shawshankredemption.com', 'True'
);

INSERT INTO movie_rating (movie, source, value) VALUES (1, 'Internet Movie Database', '9.3');
INSERT INTO movie_rating (movie, source, value) VALUES (1, 'Rotten Tomatoes', '91%');
INSERT INTO movie_rating (movie, source, value) VALUES (1, 'Metacritic', '80');
