ALTER TABLE comments
ADD COLUMN user_id BIGINT,
ADD FOREIGN KEY (user_id) REFERENCES users(id);