CREATE TABLE spotify_history (
    id SERIAL PRIMARY KEY NOT NULL,
    user_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (song_id) REFERENCES songs(id)
);

CREATE TABLE steam_history (
    id SERIAL PRIMARY KEY NOT NULL,
    user_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (game_id) REFERENCES games(id)
);