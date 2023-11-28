import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getSongs } from '../api/songs-axios';
import { Button } from '@mui/material';

export default function Songs() {
  const [songs, setSongs] = useState([]);
  const Navigate = useNavigate();

  useEffect(() => {
    getSongs()
      .then((data) => {
        setSongs(data);
      })
      .catch((error) => {
        if (error.response && error.response.status === 403) {
          Navigate('/login');
        } else {
          console.error('An error occurred:', error);
        }
      });
  }, []);

  return (
    <div>
      <h1>Songs list</h1>
      {songs.map((song) => (
        <>
          <h3 key={song.id}>{song.name}</h3>
          <Button
            onClick={() => {
              window.open(`https://open.spotify.com/track/${song.spotifyId}`, '_blank');
            }}>
            Listen on Spotify
          </Button>
        </>
      ))}
    </div>
  );
}
