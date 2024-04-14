import pandas as pd
from datetime import datetime
from sklearn.preprocessing import StandardScaler, OneHotEncoder
from sklearn.compose import ColumnTransformer
from sklearn.pipeline import Pipeline

# Sample data
data = pd.DataFrame({
    'Date': ['1988-06-18'],
    'Runtime': [106],
    'BoxOffice': [165197633],
    'Rating': [7.2],
    'Genre': ['Drama']
})

# Convert Date from string to datetime
data['Date'] = pd.to_datetime(data['Date'])

# Extract days since a reference date (e.g., '1970-01-01')
data['DaysSince'] = (data['Date'] - pd.Timestamp('1970-01-01')).dt.days

# Prepare a pipeline with transformations
pipeline = ColumnTransformer([
    ('num', StandardScaler(), ['DaysSince', 'Runtime', 'BoxOffice', 'Rating']),
    ('cat', OneHotEncoder(), ['Genre'])
])

# Fit and transform data
prepared_data = pipeline.fit_transform(data)

print("Preprocessed Data:")
print(prepared_data)
