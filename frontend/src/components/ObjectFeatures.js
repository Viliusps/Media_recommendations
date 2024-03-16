export default function ObjectFeatures({ object, type }) {
  const songFeatures = [
    'chordsChangesRate',
    'keyStrength',
    'danceability',
    'bpm',
    'beatsLoudness',
    'beatsCount',
    'spectralEnergy',
    'silenceRate',
    'dissonance',
    'averageLoudness',
    'dynamicComplexity',
    'pitchSalience'
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
              {Object.entries(object).map(
                ([key, value]) =>
                  songFeatures.includes(key) && (
                    <div key={key}>
                      <strong>{key}:</strong> {value}
                    </div>
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
