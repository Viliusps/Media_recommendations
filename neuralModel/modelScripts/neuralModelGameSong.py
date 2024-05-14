import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import MinMaxScaler, StandardScaler
from keras.models import Sequential
from keras.layers import Dense, Dropout
from keras.regularizers import l2
from keras.optimizers import Adam
import tensorflow as tf
import json


df = pd.read_csv('neuralModel/datasets/GameSong.csv')

song_features = [
    'Song_bpm_histogram_first_peak_bpm_mean', 'Song_danceability', 
    'Song_bpm_histogram_second_peak_bpm_median', 'Song_tuning_equal_tempered_deviation', 
    'Song_tuning_frequency', 'Song_bpm_histogram_second_peak_bpm_mean', 
    'Song_bpm', 'Song_bpm_histogram_first_peak_bpm_median', 'Song_mfcc_zero_mean', 
    'Song_onset_rate', 'Song_average_loudness', 'Song_dynamic_complexity'
]

game_numerical_features = ['Game_release_date', 'Game_rating', 'Game_playtime']

y = df[song_features].astype('float32')
X_numerical = df[game_numerical_features].astype('float32')
X_genre = pd.get_dummies(df['Game_genre_0'], dtype='float32')

X_train_numerical, X_test_numerical, y_train, y_test = train_test_split(X_numerical, y, test_size=0.2, random_state=42)
X_train_genre, X_test_genre, _, _ = train_test_split(X_genre, y, test_size=0.2, random_state=42)

scaler_X_numerical = MinMaxScaler().fit(X_train_numerical)
scaler_y = StandardScaler().fit(y_train)

X_train_numerical_scaled = scaler_X_numerical.transform(X_train_numerical)
X_test_numerical_scaled = scaler_X_numerical.transform(X_test_numerical)
y_train_scaled = scaler_y.transform(y_train)
y_test_scaled = scaler_y.transform(y_test)

X_train = np.concatenate((X_train_numerical_scaled, X_train_genre), axis=1)
X_test = np.concatenate((X_test_numerical_scaled, X_test_genre), axis=1)

print("Unscaled Input (Numerical Features Only):")
print(X_numerical.iloc[0])
print("Scaled Input (Numerical Features Only):")
print(X_train_numerical_scaled[0])

print("Unscaled Output:")
print(y.iloc[0])
print("Scaled Output:")
print(y_train_scaled[0])

model = Sequential([
    Dense(64, activation='relu', input_shape=(X_train.shape[1],), name='dense_input'),
    Dense(128, activation='relu', kernel_regularizer=l2(0.01)),
    Dense(256, activation='relu', kernel_regularizer=l2(0.01)),
    Dense(y_train_scaled.shape[1], activation='linear')
])

adam = Adam(learning_rate=0.001)
model.compile(optimizer=adam, loss='mse', metrics=['mae'])

history = model.fit(X_train, y_train_scaled, epochs=100, batch_size=32, validation_split=0.2, verbose=1)

@tf.function(input_signature=[tf.TensorSpec(shape=[None, X_train.shape[1]], dtype=tf.float32, name='dense_input')])
def model_serving(dense_input):
    return model(dense_input, training=False)

tf.saved_model.save(model, 'neuralModel/model_gs', signatures={'serving_default': model_serving})

genre_encoding = X_genre.columns.tolist()

scaling_parameters = {
    "input_min": scaler_X_numerical.data_min_.tolist(),
    "input_max": scaler_X_numerical.data_max_.tolist(),
    "output_mean": scaler_y.mean_.tolist(),
    "output_std": scaler_y.scale_.tolist(),
    "input_game_genre_encoding": genre_encoding
}

with open('neuralModel/scalingParameters/scaling_parameters_gs.json', 'w') as f:
    json.dump(scaling_parameters, f)


#print(X_test_numerical_scaled[0], " vs ", X_test_numerical[0])


# plt.plot(history.history['loss'], label='Train')
# plt.plot(history.history['val_loss'], label='Validation')
# plt.title('Model Loss')
# plt.ylabel('Loss')
# plt.xlabel('Epoch')
# plt.legend(loc='upper right')
# plt.show()

# test_loss, test_mae = model.evaluate(X_test, y_test_scaled)
# print(f'Test Loss: {test_loss}, Test MAE: {test_mae}')

# y_pred = model.predict(X_test)

# y_pred_numerical, y_pred_genre = y_pred[:, :len(movie_numerical_features)], y_pred[:, len(movie_numerical_features):]
# y_test_numerical, y_test_genre = y_test[:, :len(movie_numerical_features)], y_test[:, len(movie_numerical_features):]

# y_pred_numerical_inv = scaler_y.inverse_transform(y_pred_numerical)
# y_test_numerical_inv = scaler_y.inverse_transform(y_test_numerical)

# def mean_cosine_similarity(y_true, y_pred):
#     cosine_similarities = 1 - pairwise_distances(y_pred, y_true, metric='cosine')
#     return np.mean(np.diag(cosine_similarities))

# def mean_euclidean_distance(y_true, y_pred):
#     euclidean_distances = pairwise_distances(y_pred, y_true, metric='euclidean')
#     return np.mean(np.diag(euclidean_distances))

# cosine_sim = mean_cosine_similarity(y_test_numerical_inv, y_pred_numerical_inv)
# euclidean_dist = mean_euclidean_distance(y_test_numerical_inv, y_pred_numerical_inv)
# print(f'Mean Cosine Similarity: {cosine_sim}')
# print(f'Mean Euclidean Distance: {euclidean_dist}')
# print(y_pred[0], " vs ", y_test[0])

# for i in range(y_test.shape[1]):
#     plt.figure()
#     actual = y_test[:100, i]  # First 100 actual values for feature i
#     predicted = y_pred[:100, i]  # First 100 predicted values for feature i
#     plt.scatter(actual, predicted)
#     plt.xlabel('Actual Values')
#     plt.ylabel('Predicted Values')
#     plt.title(f'Actual vs. Predicted for Feature {i+1}')
#     # Draw a line representing the perfect predictions
#     plt.plot([actual.min(), actual.max()], [actual.min(), actual.max()], 'k--', lw=4)
#     plt.show()

# plt.hist(y_pred.flatten(), bins=50, alpha=0.5, label='Scaled Predictions')
# plt.legend()
# plt.title('Histogram of Scaled Predictions')
# plt.xlabel('Value')
# plt.ylabel('Frequency')
# plt.show()

# plt.hist(y_test_numerical.flatten(), bins=50, alpha=0.5, label='Scaled Actual Values')
# plt.legend()
# plt.title('Histogram of Scaled Actual Values')
# plt.xlabel('Value')
# plt.ylabel('Frequency')
# plt.show()

# y_pred_numerical_inv = scaler_y.inverse_transform(y_pred_numerical)
# y_test_numerical_inv = scaler_y.inverse_transform(y_test_numerical)

# plt.hist(y_pred_numerical_inv.flatten(), bins=50, alpha=0.5, label='Inverse-Transformed Predictions')
# plt.legend()
# plt.title('Histogram of Inverse-Transformed Predictions')
# plt.xlabel('Value')
# plt.ylabel('Frequency')
# plt.show()

# plt.hist(y_test_numerical_inv.flatten(), bins=50, alpha=0.5, label='Inverse-Transformed Actual Values')
# plt.legend()
# plt.title('Histogram of Inverse-Transformed Actual Values')
# plt.xlabel('Value')
# plt.ylabel('Frequency')
# plt.show()
