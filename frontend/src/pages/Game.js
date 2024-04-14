import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import LoadingWrapper from '../components/LoadingWrapper';
import CommentSection from '../components/CommentSection';
import { getGame } from '../api/games-axios';
import MovieDetails from '../components/MovieDetails';
import { Grid, Image, GridItem, Heading, Box } from '@chakra-ui/react';

export default function Game() {
  const [loading, setLoading] = useState(true);
  const { id } = useParams();
  const [game, setGame] = useState(null);
  const [error, setError] = useState(false);

  useEffect(() => {
    getGame(id)
      .then((data) => {
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
        <Grid templateColumns="repeat(4, 1fr)" gap={6}>
          <GridItem colSpan={1}>
            <Image src={game?.backgroundImage} alt={game?.name} />
          </GridItem>
          <GridItem colSpan={3}>
            <Box textAlign="left">
              <Heading>{game?.name}</Heading>
              <MovieDetails label="Genre" value={game?.genre} />
              <MovieDetails label="Release Date" value={game?.releaseDate} />
              <MovieDetails label="Playtime" value={`${game?.playtime} hours.`} />
              <MovieDetails label="Rating" value={game?.rating} />
            </Box>
          </GridItem>
        </Grid>
        <CommentSection object={game} id={id} setObject={setGame} type={'Game'} />
      </>
    </LoadingWrapper>
  );
}
