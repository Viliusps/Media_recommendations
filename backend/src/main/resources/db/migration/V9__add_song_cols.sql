ALTER TABLE songs
ADD COLUMN isrc VARCHAR(20),

ADD COLUMN mfcc_zero_mean VARCHAR(200),
ADD COLUMN dynamic_complexity VARCHAR(200),
ADD COLUMN average_loudness VARCHAR(200),
ADD COLUMN onset_rate VARCHAR(200),
ADD COLUMN bpm_histogram_second_peak_bpm_median VARCHAR(200),
ADD COLUMN bpm_histogram_second_peak_bpm_mean VARCHAR(200),
ADD COLUMN bpm_histogram_first_peak_bpm_median VARCHAR(200),
ADD COLUMN bpm_histogram_first_peak_bpm_mean VARCHAR(200),
ADD COLUMN bpm VARCHAR(200),
ADD COLUMN danceability VARCHAR(200),
ADD COLUMN tuning_frequency VARCHAR(200),
ADD COLUMN tuning_equal_tempered_deviation VARCHAR(200),
ADD COLUMN key_scale VARCHAR(200),
ADD COLUMN key_key VARCHAR(200);