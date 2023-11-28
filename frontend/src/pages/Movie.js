import { useEffect, useState } from 'react';
import DisplayCard from '../components/MovieCard';
import { Paper, styled } from '@mui/material';
import { getMovie } from '../api/movies-axios';
import { useParams } from 'react-router-dom';

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
    console.log(id);
    getMovie(id).then((data) => {
      setMovie(data);
    });
  }, [id]);

  return (
    <div>
      {movie && (
        <>
          <h1>{movie.originalTitle}</h1>
          <StyledPaper>
            <DisplayCard key={movie.id} movie={movie} />
          </StyledPaper>
        </>
      )}
    </div>
  );
}
