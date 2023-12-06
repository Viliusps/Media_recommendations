import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getPageSongs } from '../api/songs-axios';
import { Grid, Pagination } from '@mui/material';
import styled from 'styled-components';
import SongCard from '../components/SongCard';
import LoadingWrapper from '../components/LoadingWrapper';

const StyledPagination = styled(Pagination)`
  display: flex;
  justify-content: center;
  margin-top: 20px;
`;

export default function Songs() {
  const [songs, setSongs] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalSongs, setTotalSongs] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);
  const Navigate = useNavigate();
  const songsPerPage = 10;

  useEffect(() => {
    getPageSongs(currentPage, songsPerPage)
      .then((data) => {
        setSongs(data.songs);
        setTotalSongs(data.totalSongs);
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

  return (
    <div>
      <h1>Songs page</h1>
      <LoadingWrapper loading={loading} error={error}>
        <Grid container spacing={1}>
          {songs.map((song) => (
            <Grid item key={song.id} xs={12} sm={6} md={4} lg={2}>
              <SongCard
                title={song.title}
                artist={song.singer}
                imageUrl={song.imageUrl}
                spotifyUrl={song.spotifyId}
              />
            </Grid>
          ))}
        </Grid>
        <StyledPagination
          count={Math.ceil(totalSongs / songsPerPage)}
          page={currentPage + 1}
          onChange={(event, value) => {
            setCurrentPage(value - 1);
          }}
        />
      </LoadingWrapper>
    </div>
  );
}
