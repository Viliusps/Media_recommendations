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
      console.log(results);
      setRecentRecommendations(results);
    });
  }, []);
  return (
    <>
      <h1>Profile</h1>
      <h2>Your recently played games: </h2>
      {steamHistory.map((entry, index) => (
        <p key={index}>{entry.name}</p>
      ))}
      <h2>Your recently listened songs: </h2>
      {spotifyHistory.map((entry, index) => (
        <p key={index}>{entry.title}</p>
      ))}
      <h2>Your recent recommendations: </h2>
      <h4>(only recommendations you rated show up)</h4>
      {recentRecommendations.map((entry, index) => (
        <p key={index}>{entry.originalType}</p>
      ))}
    </>
  );
}
