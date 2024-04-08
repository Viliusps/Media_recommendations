import { useEffect, useState } from 'react';
import { getSpotifyHistory } from '../api/songs-axios';
import { getSteamHistory } from '../api/games-axios';
import { getRecentRecommendations } from '../api/recommendation-axios';

export default function Profile() {
  const [steamHistory, setSteamHistory] = useState([]);
  const [spotifyHistory, setSpotifyHistory] = useState([]);
  const [recentRecommendations, setRecentRecommendations] = useState([]);

  useEffect(() => {
    getSpotifyHistory().then((results) => {
      setSpotifyHistory(results);
    });
    getSteamHistory().then((results) => {
      setSteamHistory(results);
    });
    getRecentRecommendations().then((results) => {
      setRecentRecommendations(results);
      console.log(results);
    });
  }, []);
  return (
    <>
      <h1>Profile</h1>
      <h2>Your recently played games: </h2>
      {steamHistory != null && steamHistory.map((entry, index) => <p key={index}>{entry.name}</p>)}
      <h2>Your recently listened songs: </h2>
      {spotifyHistory != null &&
        spotifyHistory.map((entry, index) => <p key={index}>{entry.title}</p>)}
      <h2>Your recent recommendations: </h2>
      <h4>(only recommendations you rated show up)</h4>
      {recentRecommendations != null &&
        recentRecommendations.map((entry, index) => (
          <div key={index}>
            {entry.type && (
              <p>
                Recommended {entry.type}:{' '}
                {entry.type === 'Song'
                  ? entry.song.title
                  : entry.type === 'Game'
                  ? entry.game.name
                  : entry.type === 'Movie'
                  ? entry.movie.Title
                  : 'N/A'}
              </p>
            )}
            {entry.originalType && (
              <p>
                Requested {entry.originalType}:{' '}
                {entry.originalType === 'Song'
                  ? entry.originalSong.title
                  : entry.originalType === 'Game'
                  ? entry.originalGame.name
                  : entry.originalType === 'Movie'
                  ? entry.originalMovie.Title
                  : 'N/A'}
              </p>
            )}
            <p>------------------------------------------------------------</p>
          </div>
        ))}
    </>
  );
}
