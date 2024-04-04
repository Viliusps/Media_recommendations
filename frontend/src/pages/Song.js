import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Typography, Paper, Grid } from '@mui/material';
import styled from 'styled-components';
import LoadingWrapper from '../components/LoadingWrapper';
import CommentSection from '../components/CommentSection';
import { getSong } from '../api/songs-axios';
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

export default function Song() {
  const [loading, setLoading] = useState(true);
  const { id } = useParams();
  const [song, setSong] = useState(null);
  const [error, setError] = useState(false);

  useEffect(() => {
    getSong(id)
      .then((data) => {
        console.log(data);
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
        <ContentContainer elevation={3}>
          <Grid container spacing={3}>
            <Grid item xs={12} md={3}>
              <StyledImage src={song?.imageUrl} alt={song?.name} />
            </Grid>
            <Grid item xs={12} md={9}>
              <Typography variant="h4" gutterBottom>
                {song?.title}
              </Typography>
              <Typography variant="h5" gutterBottom>
                Overview
              </Typography>
              <MovieDetails label="Genre" value={song?.genre} />
              <MovieDetails label="Release Date" value={song?.releaseDate} />
              <MovieDetails label="Playtime" value={`${song?.playtime} hours.`} />
              <MovieDetails label="Rating" value={song?.rating} />
              <MovieDetails label="Popularity" value={song?.popularity} />
            </Grid>
          </Grid>
        </ContentContainer>
        <CommentSection object={song} id={id} setObject={setSong} type={'Song'} />
      </>
    </LoadingWrapper>
  );
}
