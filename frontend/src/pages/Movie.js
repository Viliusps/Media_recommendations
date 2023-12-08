import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getMovie, getOmdbMovie } from '../api/movies-axios';
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
  const [dbMovie, setDbMovie] = useState(null);
  const [error, setError] = useState(false);

  useEffect(() => {
    getMovie(id)
      .then((data) => {
        setError(false);
        setDbMovie(data);
        getOmdbMovie(data.title)
          .then((result) => {
            setMovie(result);
          })
          .catch(() => {
            setError(true);
          })
          .finally(() => {
            setLoading(false);
          });
      })
      .catch(() => {
        setError(true);
      });
  }, [id]);

  return (
    <LoadingWrapper loading={loading} error={error}>
      <>
        <ContentContainer elevation={3}>
          <Grid container spacing={3}>
            <Grid item xs={12} md={3}>
              <StyledImage src={movie?.imageUrl} alt={movie?.title} />
            </Grid>
            <Grid item xs={12} md={9}>
              <Typography variant="h4" gutterBottom>
                {movie?.title}
              </Typography>
              <Typography variant="h5" gutterBottom>
                Overview
              </Typography>
              <Typography variant="body1">{movie?.overview}</Typography>
              <MovieDetails label="Genres" value={movie?.genres} />
              <MovieDetails label="Release Date" value={movie?.releaseDate} />
              <MovieDetails label="Runtime" value={`${movie?.runtime} minutes`} />
              <MovieDetails label="Language" value={movie?.originalLanguage} />
              <MovieDetails label="Production Countries" value={movie?.productionCountries} />
              <MovieDetails label="Vote Average" value={movie?.voteAverage} />
              <MovieDetails label="Vote Count" value={movie?.voteCount} />
              <StyledButton
                variant="contained"
                color="secondary"
                onClick={() =>
                  window.open(`https://www.imdb.com/title/${movie?.imdbId}`, '_blank')
                }>
                More Info
              </StyledButton>
            </Grid>
          </Grid>
        </ContentContainer>
        <CommentSection dbMovie={dbMovie} id={id} setDbMovie={setDbMovie} />
      </>
    </LoadingWrapper>
  );
}
