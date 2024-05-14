import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import MinMaxScaler
from sklearn.neighbors import KNeighborsRegressor
from sklearn.metrics import mean_squared_error, mean_absolute_error, r2_score
import numpy as np
import matplotlib.pyplot as plt

df = pd.read_csv('neuralModel/datasets/GameGame.csv')

in_game_numerical_features = ['First_Game_release_date', 'First_Game_rating', 'First_Game_playtime']
out_game_numerical_features = ['Second_Game_release_date', 'Second_Game_rating', 'Second_Game_playtime']

X_numerical = df[in_game_numerical_features].astype('float32')
X_genre = pd.get_dummies(df['First_Game_genre_0'], dtype='float32')

y_numerical = df[out_game_numerical_features].astype('float32')
y_genre = pd.get_dummies(df['Second_Game_genre_0'], dtype='float32')

X_train_numerical, X_test_numerical, y_train_numerical, y_test_numerical = train_test_split(X_numerical, y_numerical, test_size=0.2, random_state=42)
X_train_genre, X_test_genre, y_train_genre, y_test_genre = train_test_split(X_genre, y_genre, test_size=0.2, random_state=42)

scaler_X_numerical = MinMaxScaler().fit(X_train_numerical)
scaler_y_numerical = MinMaxScaler().fit(y_train_numerical)
X_train_numerical_scaled = scaler_X_numerical.transform(X_train_numerical)
X_test_numerical_scaled = scaler_X_numerical.transform(X_test_numerical)
y_train_numerical_scaled = scaler_y_numerical.transform(y_train_numerical)
y_test_numerical_scaled = scaler_y_numerical.transform(y_test_numerical)

X_train = np.concatenate((X_train_numerical_scaled, X_train_genre), axis=1)
X_test = np.concatenate((X_test_numerical_scaled, X_test_genre), axis=1)
y_train = np.concatenate((y_train_numerical_scaled, y_train_genre), axis=1)
y_test = np.concatenate((y_test_numerical_scaled, y_test_genre), axis=1)

knn_model = KNeighborsRegressor(n_neighbors=120)

knn_model.fit(X_train, y_train)

y_pred = knn_model.predict(X_test)

mse = mean_squared_error(y_test, y_pred)
mae = mean_absolute_error(y_test, y_pred)
r2 = r2_score(y_test, y_pred)

print(f'MSE: {mse}')
print(f'MAE: {mae}')
print(f'R-squared: {r2}')

actual = y_test[:, 1]
predicted = y_pred[:, 1]

plt.figure(figsize=(10, 6))
plt.scatter(actual, predicted, alpha=0.5)
plt.title('Actual vs. Predicted for the Second Feature')
plt.xlabel('Actual Values')
plt.ylabel('Predicted Values')
plt.grid(True)

# Plot a line of perfect prediction
plt.plot([actual.min(), actual.max()], [actual.min(), actual.max()], 'k--', lw=2)
plt.show()