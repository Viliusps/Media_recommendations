ALTER TABLE songs
ADD COLUMN isrc VARCHAR(20),
ADD COLUMN chords_changes_rate VARCHAR(200),
ADD COLUMN key_strength VARCHAR(200),
ADD COLUMN danceability VARCHAR(200),
ADD COLUMN bpm VARCHAR(200),
ADD COLUMN beats_loudness VARCHAR(200),
ADD COLUMN beats_count VARCHAR(200),
ADD COLUMN spectral_energy VARCHAR(200),
ADD COLUMN silence_rate VARCHAR(200),
ADD COLUMN dissonance VARCHAR(200),
ADD COLUMN average_loudness VARCHAR(200),
ADD COLUMN dynamic_complexity VARCHAR(200),
ADD COLUMN pitch_salience VARCHAR(200)