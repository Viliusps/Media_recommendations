import { useEffect, useState } from 'react';
import { getPageMovies } from '../api/movies-axios';
import { useNavigate } from 'react-router-dom';
import DisplayCard from '../components/MovieCard';
import { Paper, Pagination } from '@mui/material';
import styled from 'styled-components';
import LoadingWrapper from '../components/LoadingWrapper';

const StyledPaper = styled(Paper)`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
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
  const Navigate = useNavigate();

  useEffect(() => {
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
  }, [currentPage]);

  return (
    <div>
      <h1>Movies page</h1>
      <LoadingWrapper loading={loading} error={error}>
        <StyledPaper>
          {movies.map((movie) => (
            <DisplayCard key={movie.id} movie={movie} />
          ))}
        </StyledPaper>
        <StyledPagination
          count={Math.ceil(totalMovies / moviesPerPage)}
          page={currentPage + 1}
          onChange={(event, value) => {
            setCurrentPage(value - 1);
          }}
        />
      </LoadingWrapper>
    </div>
  );
}
