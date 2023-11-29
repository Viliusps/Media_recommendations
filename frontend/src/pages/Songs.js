import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getSongs } from '../api/songs-axios';
import { Grid } from '@mui/material';
import SongCard from '../components/SongCard';

export default function Songs() {
  const [songs, setSongs] = useState([]);
  const Navigate = useNavigate();

  useEffect(() => {
    getSongs()
      .then((data) => {
        console.log(data);
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
    <Grid container spacing={1}>
      {songs.map((song) => (
        <Grid item key={song.id} xs={12} sm={6} md={4} lg={2}>
          <SongCard
            title={song.name}
            artist={song.singer}
            imageUrl={song.imageUrl}
            spotifyUrl={song.spotifyId}
          />
        </Grid>
      ))}
    </Grid>
  );
}
