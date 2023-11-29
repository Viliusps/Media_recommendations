import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getPageSongs } from '../api/songs-axios';
import { Grid } from '@mui/material';
import SongCard from '../components/SongCard';
import LoadingWrapper from '../components/LoadingWrapper';

export default function Songs() {
  const [songs, setSongs] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);
  const Navigate = useNavigate();

  useEffect(() => {
    getPageSongs(currentPage, 10)
      .then((data) => {
        console.log(data);
        setSongs(data);
      })
      .catch((error) => {
        setError(true);
        if (error.response && error.response.status === 403) {
          Navigate('/login');
        } else {
          console.error('An error occurred:', error);
        }
      })
      .finally(() => {
        setLoading(false);
      });
  }, [currentPage]);

  const handleNextPage = () => {
    setCurrentPage(currentPage + 1);
  };

  const handlePrevPage = () => {
    if (currentPage > 0) {
      setCurrentPage(currentPage - 1);
    }
  };

  return (
    <div>
      <h1>Songs page</h1>
      <LoadingWrapper loading={loading} error={error}>
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
        <div>
          <button onClick={handlePrevPage} disabled={currentPage === 0}>
            Previous Page
          </button>
          <button onClick={handleNextPage}>Next Page</button>
        </div>
      </LoadingWrapper>
    </div>
  );
}
