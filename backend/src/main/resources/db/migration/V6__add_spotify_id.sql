ALTER TABLE songs
ADD COLUMN spotify_id VARCHAR(255);

UPDATE songs
SET spotify_id = 
    CASE 
        WHEN name = 'Highway to hell' THEN '2zYzyRzz6pRmhPzyfMEC8s?si=6c2f0c5e3f174ef6'
        WHEN name = 'Fur Elise' THEN '1DfGPEHxTYeaJpiNA4xUb5?si=22478c82434d4800'
        WHEN name = 'Umbrella' THEN '49FYlytm3dAAraYgpoJZux?si=73c8831b41ac4cfa'
        ELSE NULL
    END;