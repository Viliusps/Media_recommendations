import numpy as np
import pandas as pd
from sklearn.preprocessing import StandardScaler, LabelEncoder
from sklearn.model_selection import train_test_split
from keras.models import Sequential
from keras.layers import Dense
import joblib

data = {
    'song_chords_changes_rate': [0.2, 0.3, 0.5],
    'song_key_strength': [0.8, 0.6, 0.7],
    'song_danceability': [0.6, 0.7, 0.8],
    # Add more song features here...
    'movie_release_date': ['2022-01-01', '2021-12-15', '2023-05-20'],
    'movie_genres': ['Action', 'Comedy', 'Drama'],
    'movie_ratings': [7.5, 8.0, 6.5],
    # Add more movie features here...
    'game_release_date': ['2022-03-10', '2023-06-05', '2021-11-20'],
    'game_genres': ['RPG', 'FPS', 'Strategy'],
    'game_rating': [4.5, 4.0, 4.7],
    # Add more game features here...
    'recommended_song': ['Song A', 'Song B', 'Song C'],
    'recommended_movie': ['Movie X', 'Movie Y', 'Movie Z'],
    'recommended_game': ['Game 1', 'Game 2', 'Game 3']
}

df = pd.DataFrame(data)


numeric_features = ['song_chords_changes_rate', 'song_key_strength', 'song_danceability', 'movie_ratings', 'game_rating']
scaler = StandardScaler()
df[numeric_features] = scaler.fit_transform(df[numeric_features])


label_encoders = {}
categorical_features = ['movie_genres', 'game_genres']
for feature in categorical_features:
    label_encoders[feature] = LabelEncoder()
    df[feature] = label_encoders[feature].fit_transform(df[feature])


X = df[numeric_features + categorical_features + ['movie_release_date', 'game_release_date']].values
y = df['recommended_song'].values


X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

model = Sequential([
    Dense(64, activation='relu', input_shape=(X_train.shape[1],)),
    Dense(32, activation='relu'),
    Dense(1)
])

model.compile(optimizer='adam', loss='mean_squared_error')

model.fit(X_train, y_train, epochs=50, batch_size=32, validation_split=0.2)

joblib.dump(model, 'recommendation_model.pkl')
print("Model saved to recommendation_model.pkl")

loss = model.evaluate(X_test, y_test)
print('Test Loss:', loss)
