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

  function stripHtml(html) {
    return html.replace(/<\/?[^>]+(>|$)/g, '').replace(/&#39;/g, "'");
  }

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
        <Grid templateColumns="repeat(4, 1fr)" gap={6}>
          <GridItem colSpan={1}>
            <Image src={game?.backgroundImage} alt={game?.name} />
          </GridItem>
          <GridItem colSpan={3}>
            <Box textAlign="left">
              <Heading>{game?.name}</Heading>
              <MovieDetails label="Genres" value={game?.genres} />
              <MovieDetails label="Release Date" value={game?.releaseDate} />
              <MovieDetails label="Playtime" value={`${game?.playtime} hours.`} />
              <MovieDetails label="Rating" value={game?.rating} />
              <MovieDetails label="Description" value={stripHtml(game?.description)} />
            </Box>
          </GridItem>
        </Grid>
        <CommentSection object={game} id={id} setObject={setGame} type={'Game'} />
      </>
    </LoadingWrapper>
  );
}
