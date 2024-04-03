import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Typography, Paper, Grid } from '@mui/material';
import styled from 'styled-components';
import LoadingWrapper from '../components/LoadingWrapper';
//import CommentSection from '../components/CommentSection';
import { getGame } from '../api/games-axios';
import MovieDetails from '../components/MovieDetails';

const ContentContainer = styled(Paper)`
  padding: 20px;
  margin-top: 20px;
  border-radius: 8px;
`;
const StyledImage = styled.img`
  width: 100%;
  border-radius: 8px;
`;

export default function Game() {
  const [loading, setLoading] = useState(true);
  const { id } = useParams();
  const [game, setGame] = useState(null);
  const [error, setError] = useState(false);

  useEffect(() => {
    getGame(id)
      .then((data) => {
        console.log(data);
        setError(false);
        setGame(data);
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
              <StyledImage src={game?.backgroundImage} alt={game?.name} />
            </Grid>
            <Grid item xs={12} md={9}>
              <Typography variant="h4" gutterBottom>
                {game?.name}
              </Typography>
              <Typography variant="h5" gutterBottom>
                Overview
              </Typography>
              <MovieDetails label="Genre" value={game?.genre} />
              <MovieDetails label="Release Date" value={game?.releaseDate} />
              <MovieDetails label="Playtime" value={`${game?.playtime} hours.`} />
              <MovieDetails label="Rating" value={game?.rating} />
              <MovieDetails label="Popularity" value={game?.popularity} />
            </Grid>
          </Grid>
        </ContentContainer>
        {/* <CommentSection movie={game} id={id} setGame={setGame} /> */}
      </>
    </LoadingWrapper>
  );
}
