import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import LoadingWrapper from '../components/LoadingWrapper';
import { getPageGames, searchGames } from '../api/games-axios';
import GameCard from '../components/GameCard';
import { Button, Input, useColorModeValue, Grid, GridItem, Heading } from '@chakra-ui/react';
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

export default function Games() {
  const [games, setGames] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalGames, setTotalGames] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const Navigate = useNavigate();
  const gamesPerPage = 12;

  const handleSearch = async () => {
    setLoading(true);
    searchGames(searchTerm)
      .then((result) => {
        setGames(result.games);
        setTotalGames(result.totalGames);
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
      getPageGames(currentPage, gamesPerPage)
        .then((data) => {
          setGames(data.games);
          setTotalGames(data.totalGames);
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
      <Heading as="h3" size="md">
        Most popular games among users
      </Heading>
      <SearchWrapper>
        <Input
          placeholder="Search for a game..."
          variant="outlined"
          marginTop={2}
          marginBottom={2}
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <Button
          px={8}
          bg={useColorModeValue('#151f21', 'gray.900')}
          color={'white'}
          rounded={'md'}
          _hover={{
            transform: 'translateY(-2px)',
            boxShadow: 'lg'
          }}
          onClick={() => handleSearch}>
          Search
        </Button>
      </SearchWrapper>
      <LoadingWrapper loading={loading} error={error}>
        {totalGames > 0 ? (
          <>
            <Grid templateColumns="repeat(4, 1fr)" gap={6}>
              {games.map((game) => (
                <GridItem key={game.id}>
                  <GameCard game={game} />
                </GridItem>
              ))}
            </Grid>
            <StyledPagination
              count={Math.ceil(totalGames / gamesPerPage)}
              page={currentPage + 1}
              onChange={(event, value) => {
                setCurrentPage(value - 1);
              }}
            />
          </>
        ) : (
          <h1>No games found</h1>
        )}
      </LoadingWrapper>
    </div>
  );
}
