import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import LoadingWrapper from '../components/LoadingWrapper';
import CommentSection from '../components/CommentSection';
import { getGame } from '../api/games-axios';
import MovieDetails from '../components/MovieDetails';
import { Text, Grid, Image, Container } from '@chakra-ui/react';

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
        <Container elevation={3}>
          <Grid container spacing={3}>
            <Grid item xs={12} md={3}>
              <Image src={game?.backgroundImage} alt={game?.name} />
            </Grid>
            <Grid item xs={12} md={9}>
              <Text variant="h4" gutterBottom>
                {game?.name}
              </Text>
              <Text variant="h5" gutterBottom>
                Overview
              </Text>
              <MovieDetails label="Genre" value={game?.genre} />
              <MovieDetails label="Release Date" value={game?.releaseDate} />
              <MovieDetails label="Playtime" value={`${game?.playtime} hours.`} />
              <MovieDetails label="Rating" value={game?.rating} />
              <MovieDetails label="Popularity" value={game?.popularity} />
            </Grid>
          </Grid>
        </Container>
        <CommentSection object={game} id={id} setObject={setGame} type={'Game'} />
      </>
    </LoadingWrapper>
  );
}
