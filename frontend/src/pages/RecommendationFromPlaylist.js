import { useEffect, useState } from 'react';
import { getUserSpotifySongs } from '../api/songs-axios';

export default function RecommendationFromPlaylist() {
  const [userSongs, setUserSongs] = useState([]);

  useEffect(() => {
    if (localStorage.getItem('spotifyAuthToken')) {
      getUserSpotifySongs().then((data) => {
        console.log(data);
        setUserSongs(data);
      });
    }
  }, []);

  return (
    <div>
      <h1>Recommending based on a playlist. Here are your most recent songs</h1>
      {userSongs.map((song, index) => (
        <h3 key={index}>{song}</h3>
      ))}
    </div>
  );
}
