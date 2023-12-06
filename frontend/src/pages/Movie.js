import { useEffect, useState } from 'react';
import DisplayCard from '../components/MovieCard';
import { Paper } from '@mui/material';
import { getMovie } from '../api/movies-axios';
import { useParams } from 'react-router-dom';
import styled from 'styled-components';

const StyledPaper = styled(Paper)`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
`;

export default function Movie() {
  const { id } = useParams();
  const [movie, setMovie] = useState(null);

  useEffect(() => {
    getMovie(id).then((data) => {
      setMovie(data);
    });
  }, [id]);

  return (
    <div>
      {movie && (
        <>
          <h1>{movie.title}</h1>
          <StyledPaper>
            <DisplayCard key={movie.id} movie={movie} />
          </StyledPaper>
        </>
      )}
    </div>
  );
}
