import numpy as np
import pandas as pd
from sklearn.preprocessing import StandardScaler, LabelEncoder
from sklearn.model_selection import train_test_split
from keras.models import Sequential
from keras.layers import Dense, Input, Flatten
from keras.optimizers import Adam
import joblib
import psycopg2
from tensorflow.keras.preprocessing.sequence import pad_sequences

db_params = {
    'dbname': 'media_recommendations',
    'user': 'admin',
    'password': 'pass',
    'host': 'localhost'
}

conn = psycopg2.connect(**db_params)
cur = conn.cursor()

fetch_recommendations_sql = """
SELECT first, second, first_type, second_type
FROM recommendations
ORDER BY date DESC
LIMIT 100;
"""

cur.execute(fetch_recommendations_sql)
recommendations = cur.fetchall()

def fetch_item_data(item_id, item_type):
   if item_type == 'movie':
        select_query = "SELECT released, genre, imdb_rating, runtime, box_office FROM movies WHERE id = %s;"
        feature_keys = ["released", "genre", "imdb_rating", "runtime", "box_office"]
   elif item_type == 'song':
        select_query = "SELECT chords_changes_rate, key_strength, danceability, bpm, beats_loudness, beats_count, spectral_energy, silence_rate, dissonance, average_loudness, dynamic_complexity, pitch_salience FROM songs WHERE id = %s;"
        feature_keys = ["chords_changes_rate", "key_strength", "danceability", "bpm", "beats_loudness", "beats_count", "spectral_energy", "silence_rate", "dissonance", "average_loudness", "dynamic_complexity", "pitch_salience"]
   elif item_type == 'game':
        select_query = "SELECT release_date, rating, genre, playtime FROM games WHERE id = %s;"
        feature_keys = ["release_date", "rating", "genre", "playtime"]
   else:
        return None

   cur.execute(select_query, (item_id,))
   item_data = cur.fetchone()

   if item_data:
        features_dict = {key: value for key, value in zip(feature_keys, item_data)}
        return features_dict
   else:
        return None


data_pairs = []
for first_id, second_id, first_type, second_type in recommendations:
    print("START PAIR.")
    first_item_data = fetch_item_data(first_id, first_type)
    second_item_data = fetch_item_data(second_id, second_type)

    if first_item_data and second_item_data:
        pair_data = {
            "first_item": {
                "type": first_type,
                "features": first_item_data
            },
            "second_item": {
                "type": second_type,
                "features": second_item_data
            }
        }
    
    data_pairs.append(pair_data)
    
    print("First Item:", first_type, first_item_data)
    print("Second Item:", second_type, second_item_data)
    print("---")

cur.close()
conn.close()

features = []
target_features = []
types = []
target_types = []

for pair in data_pairs:
    features.append(pair["first_item"]["features"])
    target_features.append(pair["second_item"]["features"])
    types.append(pair["first_item"]["type"])
    target_types.append(pair["second_item"]["type"])


type_encoder = LabelEncoder()
encoded_types = type_encoder.fit_transform(types + target_types).reshape(-1, 1)
split_index = len(types)
encoded_input_types = encoded_types[:split_index]
encoded_target_types = encoded_types[split_index:]


max_len = max(max(len(f) for f in features), max(len(f) for f in target_features))
padded_features = pad_sequences(features, maxlen=max_len, padding='post', dtype='float32')
padded_target_features = pad_sequences(target_features, maxlen=max_len, padding='post', dtype='float32')

print("First encoded Item:", padded_features, encoded_input_types)
print("Second encoded Item:", padded_target_features, encoded_target_types)

final_features = np.hstack([padded_features, encoded_input_types])
final_target_features = np.hstack([padded_target_features, encoded_target_types])

X_train, X_test, y_train, y_test = train_test_split(final_features, final_target_features, test_size=0.2, random_state=42)

model = Sequential([
    Input(shape=(X_train.shape[1],)),
    Dense(128, activation='relu'),
    Dense(64, activation='relu'),
    Dense(max_len + 1, activation='linear')
])

model.compile(optimizer=Adam(), loss='mean_squared_error')

model.fit(X_train, y_train, epochs=50, batch_size=32, validation_split=0.2)

model.save('recommendation_model.h5')

