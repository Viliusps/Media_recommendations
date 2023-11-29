import { useEffect, useState } from 'react';
import { getPageMovies } from '../api/movies-axios';
import { useNavigate } from 'react-router-dom';
import DisplayCard from '../components/MovieCard';
import { Paper, styled } from '@mui/material';
import LoadingWrapper from '../components/LoadingWrapper';

const StyledPaper = styled(Paper)`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
`;

export default function Movies() {
  const [movies, setMovies] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);
  const Navigate = useNavigate();

  useEffect(() => {
    getPageMovies(currentPage, 10)
      .then((data) => {
        setMovies(data);
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
      <h1>Movies page</h1>
      <LoadingWrapper loading={loading} error={error}>
        <StyledPaper>
          {movies.map((movie) => (
            <DisplayCard key={movie.id} movie={movie} />
          ))}
        </StyledPaper>
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
