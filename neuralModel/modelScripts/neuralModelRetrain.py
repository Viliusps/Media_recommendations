import pandas as pd
import psycopg2
import sys

if len(sys.argv) > 2:
    recommendingBy = sys.argv[1]
    recommending = sys.argv[2]
else:
    print("Insufficient arguments provided")
    sys.exit(1)

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
WHERE first_type = %s AND second_type = %s AND rating = true
ORDER BY date DESC
LIMIT 100;
"""

cur.execute(fetch_recommendations_sql, (recommendingBy, recommending))

records = cur.fetchall()

def fetch_item_data(item_id, item_type):
   if item_type == 'Movie':
        select_query = "SELECT released, genre, imdb_rating, runtime, imdb_votes FROM movies WHERE id = %s;"
        feature_keys = ["released", "genre", "imdb_rating", "runtime", "imdb_votes"]
   elif item_type == 'Song':
        select_query = "SELECT mfcc_zero_mean, dynamic_complexity, average_loudness, onset_rate, bpm_histogram_second_peak_bpm_median, bpm_histogram_second_peak_bpm_mean, bpm_histogram_first_peak_bpm_median, bpm_histogram_first_peak_bpm_mean, bpm, danceability, tuning_frequency, tuning_equal_tempered_deviation FROM songs WHERE id = %s;"
        feature_keys = ["mfcc_zero_mean", "dynamic_complexity", "average_loudness", "onset_rate", "bpm_histogram_second_peak_bpm_median", "bpm_histogram_second_peak_bpm_mean", "bpm_histogram_first_peak_bpm_median", "bpm_histogram_first_peak_bpm_mean", "bpm", "danceability", "tuning_frequency", "tuning_equal_tempered_deviation"]
   elif item_type == 'Game':
        select_query = "SELECT release_date, rating, genre, playtime FROM games WHERE id = %s;"
        feature_keys = ["release_date", "rating", "genre", "playtime"]
   else:
        return None

   cur.execute(select_query, (item_id,))
   item_data = cur.fetchone()

   if item_data:
        features_dict = {key: value for key, value in zip(feature_keys, item_data)}
        if 'genre' in features_dict:
            genres = features_dict['genre'].split(',')
            features_dict['genre_0'] = genres[0].strip() if len(genres) > 0 else ''
            features_dict['genre_1'] = genres[1].strip() if len(genres) > 1 else ''
            del features_dict['genre']
        return features_dict
   else:
        return None

def process_movie_data(data):
    data['released'] = data['released'][-4:]
    data['runtime'] = data['runtime'][:-4]
    data['imdb_votes'] = data['imdb_votes'].replace(',', '')
    return data

def process_game_data(data):
    data['release_date'] = data['release_date'][:4]
    return data

def process_data(records):
  data_pairs = []
  for first_id, second_id, first_type, second_type in records:
      first_item_data = fetch_item_data(first_id, first_type)
      second_item_data = fetch_item_data(second_id, second_type)

      if first_type == 'Movie':
            first_item_data = process_movie_data(first_item_data)
      if second_type == 'Movie':
            second_item_data = process_movie_data(second_item_data)

      if first_type == 'Game':
            first_item_data = process_game_data(first_item_data)
      if second_type == 'Game':
            second_item_data = process_game_data(second_item_data)

      if(first_type == second_type):
          first_type = "First_" + first_type
          second_type = "Second_" + second_type

      if first_item_data and second_item_data:
          combined_features = {
              f"{first_type}_{''.join(key)}": value for key, value in first_item_data.items()
          }
          combined_features.update({f"{second_type}_{''.join(key)}": value for key, value in second_item_data.items()})
          data_pairs.append(combined_features)
  df = pd.DataFrame(data_pairs)
  return df

data_pairs = []
for first_id, second_id, first_type, second_type in records:
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

data_df = process_data(records)

print(data_df.to_string(index=False))

cur.close()
conn.close()

def determine_filename(first_type, second_type):
    type_pair = sorted([first_type, second_type])
    return f"neuralModel/datasets/{type_pair[0]}{type_pair[1]}.csv"

existing_csv_path = determine_filename(first_type, second_type)
existing_df = pd.read_csv(existing_csv_path)

data_df = data_df[existing_df.columns]

combined_df = pd.concat([existing_df, data_df], ignore_index=True)

combined_df.to_csv(existing_csv_path, index=False)

filename = f"neuralModel/modelScripts/{first_type}{second_type}.py"
with open(filename, 'r') as file:
    exec(file.read())
