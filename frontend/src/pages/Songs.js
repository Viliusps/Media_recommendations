import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getPageSongs, searchSongs } from '../api/songs-axios';
import { Grid, Pagination, TextField, InputAdornment, Button } from '@mui/material';
import styled from 'styled-components';
import SongCard from '../components/SongCard';
import LoadingWrapper from '../components/LoadingWrapper';

const StyledPagination = styled(Pagination)`
  display: flex;
  justify-content: center;
  margin-top: 20px;
`;

const SearchWrapper = styled.div`
  margin: auto auto 20px auto;

  width: 500px;
`;

const SearchButton = styled(Button)`
  margin-left: 10px;
`;

export default function Songs() {
  const [songs, setSongs] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalSongs, setTotalSongs] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const Navigate = useNavigate();
  const songsPerPage = 12;

  const handleSearch = async () => {
    setLoading(true);
    searchSongs(searchTerm)
      .then((result) => {
        setSongs(result.songs);
        setTotalSongs(result.totalSongs);
      })
      .catch((error) => {
        setError(true);
        console.error(error);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  useEffect(() => {
    if (!searchTerm) {
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
    }
  }, [currentPage, searchTerm, Navigate]);

  return (
    <div>
      <h1>Songs page</h1>
      <SearchWrapper>
        <TextField
          label="Search Songs"
          variant="outlined"
          size="small"
          fullWidth
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          InputProps={{
            endAdornment: (
              <InputAdornment position="end">
                <SearchButton variant="contained" onClick={handleSearch}>
                  Search
                </SearchButton>
              </InputAdornment>
            )
          }}
        />
      </SearchWrapper>
      <LoadingWrapper loading={loading} error={error}>
        {totalSongs > 0 ? (
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
        ) : (
          <h1>No songs found</h1>
        )}

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
