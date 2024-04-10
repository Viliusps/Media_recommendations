import { useEffect, useState } from 'react';
import { getPageMovies, searchMovies } from '../api/movies-axios';
import { useNavigate } from 'react-router-dom';
import DisplayCard from '../components/MovieCard';
import LoadingWrapper from '../components/LoadingWrapper';
import { Input, Button } from '@chakra-ui/react';
import styled from 'styled-components';
import { Pagination } from '@mui/material';

const SearchWrapper = styled.div`
  margin: auto auto 20px auto;

  width: 500px;
`;

const StyledPagination = styled(Pagination)`
  display: flex;
  justify-content: center;
  margin-top: 20px;
`;

export default function Movies() {
  const [movies, setMovies] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalMovies, setTotalMovies] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);
  const moviesPerPage = 10;
  const [searchTerm, setSearchTerm] = useState('');
  const Navigate = useNavigate();

  const handleSearch = async () => {
    setLoading(true);
    searchMovies(searchTerm)
      .then((result) => {
        setMovies(result.movies);
        setTotalMovies(result.totalMovies);
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
      getPageMovies(currentPage, moviesPerPage)
        .then((data) => {
          setMovies(data.movies);
          setTotalMovies(data.totalMovies);
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
      <h1>Movies page</h1>
      <SearchWrapper>
        <Input
          label="Search Songs"
          variant="outlined"
          size="small"
          fullWidth
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          InputProps={{
            endAdornment: (
              <Button variant="contained" onClick={handleSearch}>
                Search
              </Button>
            )
          }}
        />
      </SearchWrapper>
      <LoadingWrapper loading={loading} error={error}>
        {totalMovies > 0 ? (
          <>
            {movies.map((movie) => (
              <DisplayCard key={movie.id} movie={movie} />
            ))}
            <StyledPagination
              count={Math.ceil(totalMovies / moviesPerPage)}
              page={currentPage + 1}
              onChange={(event, value) => {
                setCurrentPage(value - 1);
              }}
            />
          </>
        ) : (
          <h1>No movies found</h1>
        )}
      </LoadingWrapper>
    </div>
  );
}
