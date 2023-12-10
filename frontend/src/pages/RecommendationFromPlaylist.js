import { useEffect, useState } from 'react';
import { getUserSpotifySongs } from '../api/songs-axios';
import { Button, Typography } from '@mui/material';
// eslint-disable-next-line no-undef
const clientId = process.env.REACT_APP_SPOTIFY_CLIENT_ID;

export default function RecommendationFromPlaylist() {
  const [userSongs, setUserSongs] = useState([]);
  const [loggedin, setLoggedin] = useState(false);

  const handleLogin = () => {
    const redirectUri = 'http://localhost:3000/playlistRecommendation';
    const authUrl = `https://accounts.spotify.com/authorize?client_id=${clientId}&redirect_uri=${encodeURIComponent(
      redirectUri
    )}&response_type=token&scope=user-read-recently-played`;
    window.location.href = authUrl;
  };

  useEffect(() => {
    const params = new URLSearchParams(window.location.hash.substring(1));
    const token = params.get('access_token');

    if (token) {
      localStorage.setItem('spotifyAuthToken', token);
    }
    if (token || localStorage.getItem('spotifyAuthToken')) {
      setLoggedin(true);
      getUserSpotifySongs().then((data) => {
        setUserSongs(data);
      });
    }
  }, []);

  return (
    <div>
      {loggedin ? (
        <>
          <h1>Recommending based on a playlist. Here are your most recent songs</h1>
          {userSongs.map((song, index) => (
            <Typography key={index}>{song}</Typography>
          ))}
          <Button>Continue</Button>
        </>
      ) : (
        <>
          <h1>Not logged in</h1>
          <button onClick={() => handleLogin()}>Login with Spotify</button>
        </>
      )}
    </div>
  );
}
