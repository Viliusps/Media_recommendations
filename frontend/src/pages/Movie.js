import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getMovie } from '../api/movies-axios';
import LoadingWrapper from '../components/LoadingWrapper';
import MovieDetails from '../components/MovieDetails';
import CommentSection from '../components/CommentSection';
import {
  Button,
  Text,
  Grid,
  useColorModeValue,
  Image,
  GridItem,
  Heading,
  Box
} from '@chakra-ui/react';

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
        <Grid templateColumns="repeat(4, 1fr)" gap={6}>
          <GridItem colSpan={1}>
            <Image src={movie?.Poster} alt={movie?.Title} />
          </GridItem>
          <GridItem colSpan={3}>
            <Box textAlign="left">
              <Heading>{movie?.Title}</Heading>
              <Text marginBottom={2}>{movie?.Plot}</Text>
              <MovieDetails label="Genre" value={movie?.Genre} />
              <MovieDetails label="Release Date" value={movie?.Released} />
              <MovieDetails label="Runtime" value={`${movie?.Runtime}.`} />
              <MovieDetails label="Language" value={movie?.Language} />
              <MovieDetails label="Produced by" value={movie?.Production} />
              <MovieDetails label="IMDB rating" value={movie?.imdbRating} />
              <MovieDetails label="Vote Count" value={movie?.imdbVotes} />
              <Button
                px={8}
                bg={useColorModeValue('#151f21', 'gray.900')}
                color={'white'}
                rounded={'md'}
                _hover={{
                  transform: 'translateY(-2px)',
                  boxShadow: 'lg'
                }}
                onClick={() =>
                  window.open(`https://www.imdb.com/title/${movie?.imdbID}`, '_blank')
                }>
                More Info
              </Button>
            </Box>
          </GridItem>
        </Grid>
        <CommentSection object={movie} id={id} setObject={setMovie} type={'Movie'} />
      </>
    </LoadingWrapper>
  );
}
