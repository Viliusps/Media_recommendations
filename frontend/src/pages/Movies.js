import { useEffect, useState } from 'react';
import { getMovies } from '../api/movies-axios';
import { useNavigate } from 'react-router-dom';
import DisplayCard from '../components/Card';
import { Paper, styled } from '@mui/material';

const StyledPaper = styled(Paper)`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
`;

export default function Movies() {
  const [movies, setMovies] = useState([]);
  const Navigate = useNavigate();

  useEffect(() => {
    getMovies()
      .then((data) => {
        setMovies(data);
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
      <h1>Movies page</h1>
      <StyledPaper>
        {movies.map((movie) => (
          <DisplayCard key={movie.id} movie={movie} />
        ))}
      </StyledPaper>
    </div>
  );
}
