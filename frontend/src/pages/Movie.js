import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getMovie } from '../api/movies-axios';
import { Typography, Paper, Grid, Button } from '@mui/material';
import styled from 'styled-components';
import LoadingWrapper from '../components/LoadingWrapper';
import MovieDetails from '../components/MovieDetails';
import CommentSection from '../components/CommentSection';

const ContentContainer = styled(Paper)`
  padding: 20px;
  margin-top: 20px;
  border-radius: 8px;
`;

const StyledButton = styled(Button)`
  margin-top: 20px;
`;

const StyledImage = styled.img`
  width: 100%;
  border-radius: 8px;
`;

export default function Movie() {
  const [loading, setLoading] = useState(true);
  const { id } = useParams();
  const [movie, setMovie] = useState(null);
  const [error, setError] = useState(false);

  useEffect(() => {
    getMovie(id)
      .then((data) => {
        setError(false);
        setMovie(data);
      })
      .catch(() => {
        setError(true);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [id]);

  return (
    <LoadingWrapper loading={loading} error={error}>
      <>
        <ContentContainer elevation={3}>
          <Grid container spacing={3}>
            <Grid item xs={12} md={3}>
              <StyledImage src={movie?.Poster} alt={movie?.Title} />
            </Grid>
            <Grid item xs={12} md={9}>
              <Typography variant="h4" gutterBottom>
                {movie?.Title}
              </Typography>
              <Typography variant="h5" gutterBottom>
                Overview
              </Typography>
              <Typography variant="body1">{movie?.Plot}</Typography>
              <MovieDetails label="Genre" value={movie?.Genre} />
              <MovieDetails label="Release Date" value={movie?.Released} />
              <MovieDetails label="Runtime" value={`${movie?.Runtime}.`} />
              <MovieDetails label="Language" value={movie?.Language} />
              <MovieDetails label="Produced by" value={movie?.Production} />
              <MovieDetails label="IMDB rating" value={movie?.imdbRating} />
              <MovieDetails label="Vote Count" value={movie?.imdbVotes} />
              <StyledButton
                variant="contained"
                color="secondary"
                onClick={() =>
                  window.open(`https://www.imdb.com/title/${movie?.imdbID}`, '_blank')
                }>
                More Info
              </StyledButton>
            </Grid>
          </Grid>
        </ContentContainer>
        <CommentSection movie={movie} id={id} setMovie={setMovie} />
      </>
    </LoadingWrapper>
  );
}
