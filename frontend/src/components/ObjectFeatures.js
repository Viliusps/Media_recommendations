export default function ObjectFeatures({ object, type }) {
  const songFeatures = [
    'mfccZeroMean',
    'dynamicComplexity',
    'averageLoudness',
    'onsetRate',
    'bpmHistogramSecondPeakBpmMedian',
    'bpmHistogramSecondPeakBpmMean',
    'bpmHistogramFirstPeakBpmMedian',
    'bpmHistogramFirstPeakBpmMean',
    'bpm',
    'danceability',
    'tuningFrequency',
    'tuningEqualTemperedDeviation',
    'keyScale',
    'keyKey'
  ];

  const movieFeatures = ['Released', 'Genre', 'imdbRating', 'Runtime', 'BoxOffice'];
  const gameFeatures = ['releaseDate', 'rating', 'genre', 'playtime'];
  return (
    <>
      {object && (
        <>
          {type === 'Movie' && (
            <div>
              {Object.entries(object).map(
                ([key, value]) =>
                  movieFeatures.includes(key) && (
                    <div key={key}>
                      <strong>{key}:</strong> {value}
                    </div>
                  )
              )}
            </div>
          )}
          {type === 'Song' && (
            <div>
              {Object.entries(object).some(
                ([key, value]) => value === null && songFeatures.includes(key)
              ) ? (
                <div>Unfortunately the features of this song could not be analyzed.</div>
              ) : (
                Object.entries(object).map(
                  ([key, value]) =>
                    songFeatures.includes(key) && (
                      <div key={key}>
                        <strong>{key}:</strong> {value}
                      </div>
                    )
                )
              )}
            </div>
          )}

          {type === 'Game' && (
            <div>
              {Object.entries(object).map(
                ([key, value]) =>
                  gameFeatures.includes(key) && (
                    <div key={key}>
                      <strong>{key}:</strong> {value}
                    </div>
                  )
              )}
              <p>
                This information was acquired from <a href="https://rawg.io/">RAWG</a>
              </p>
            </div>
          )}
        </>
      )}
    </>
  );
}
