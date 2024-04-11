import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getPageSongs, searchSongs } from '../api/songs-axios';
import styled from 'styled-components';
import SongCard from '../components/SongCard';
import LoadingWrapper from '../components/LoadingWrapper';
import { Button, Input, Grid, GridItem, useColorModeValue } from '@chakra-ui/react';
import { Pagination } from '@mui/material';

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
  const songsPerPage = 15;

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
        <Input
          label="Search Songs"
          variant="outlined"
          size="small"
          fullWidth
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <SearchButton
          px={8}
          bg={useColorModeValue('#151f21', 'gray.900')}
          color={'white'}
          rounded={'md'}
          _hover={{
            transform: 'translateY(-2px)',
            boxShadow: 'lg'
          }}
          onClick={() => handleSearch()}>
          Search
        </SearchButton>
      </SearchWrapper>
      <LoadingWrapper loading={loading} error={error}>
        {totalSongs > 0 ? (
          <Grid templateColumns="repeat(5, 1fr)" gap={6}>
            {songs.map((song) => (
              <GridItem item key={song.id}>
                <SongCard song={song} />
              </GridItem>
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
