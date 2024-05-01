import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import LoadingWrapper from '../components/LoadingWrapper';
import CommentSection from '../components/CommentSection';
import { getSong } from '../api/songs-axios';
import MovieDetails from '../components/MovieDetails';
import { Heading, Grid, Image, GridItem, Box } from '@chakra-ui/react';

export default function Song() {
  const [loading, setLoading] = useState(true);
  const { id } = useParams();
  const [song, setSong] = useState(null);
  const [error, setError] = useState(false);

  useEffect(() => {
    getSong(id)
      .then((data) => {
        setError(false);
        setSong(data);
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
            <Image src={song?.imageUrl} alt={song?.name} />
          </GridItem>
          <GridItem colSpan={3}>
            <Box textAlign="left">
              <Heading>{song?.title}</Heading>
              <MovieDetails label="Singer" value={song?.singer} />
            </Box>
          </GridItem>
        </Grid>
        <CommentSection object={song} id={id} setObject={setSong} type={'Song'} />
      </>
    </LoadingWrapper>
  );
}
