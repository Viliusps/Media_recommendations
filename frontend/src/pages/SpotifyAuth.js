import React, { useEffect } from 'react';
// eslint-disable-next-line no-undef
const clientId = process.env.REACT_APP_SPOTIFY_CLIENT_ID;

const SpotifyAuth = () => {
  useEffect(() => {
    const params = new URLSearchParams(window.location.hash.substring(1));
    const token = params.get('access_token');

    if (token) {
      localStorage.setItem('spotifyAuthToken', token);
    }
  }, []);

  const handleLogin = () => {
    const redirectUri = 'http://localhost:3000/spotify';
    const authUrl = `https://accounts.spotify.com/authorize?client_id=${clientId}&redirect_uri=${encodeURIComponent(
      redirectUri
    )}&response_type=token&scope=user-read-recently-played`;
    window.location.href = authUrl;
  };

  return (
    <div>
      <button onClick={() => handleLogin()}>Login with Spotify</button>
    </div>
  );
};

export default SpotifyAuth;
