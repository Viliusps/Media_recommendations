import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getMovie, getOmdbMovie } from '../api/movies-axios';
import { Typography, Paper, Grid, Button, TextField } from '@mui/material';
import styled from 'styled-components';

const ContentContainer = styled(Paper)`
  padding: 20px;
  margin-top: 20px;
  border-radius: 8px;
`;

const CommentContainer = styled(Paper)`
  padding: 20px;
  margin-top: 20px;
  border-radius: 8px;
`;

const MovieDetails = ({ label, value }) => (
  <Typography variant="body2" color="textSecondary" gutterBottom>
    <strong>{label}:</strong>{' '}
    {Array.isArray(value) ? value.map((genre) => genre.name).join(', ') : value}
  </Typography>
);

export default function Movie() {
  const { id } = useParams();
  const [movie, setMovie] = useState(null);
  const [dbMovie, setDbMovie] = useState(null);
  const [comment, setComment] = useState('');

  useEffect(() => {
    getMovie(id).then((data) => {
      setDbMovie(data);
      getOmdbMovie(data.title).then((result) => {
        setMovie(result);
      });
    });
  }, [id]);

  const handleCommentSubmit = (event) => {
    console.log(event);
  };

  return (
    <div>
      {movie && (
        <>
          <ContentContainer elevation={3}>
            <Grid container spacing={3}>
              <Grid item xs={12} md={3}>
                <img
                  src={movie.imageUrl}
                  alt={movie.title}
                  style={{ width: '100%', borderRadius: '8px' }}
                />
              </Grid>
              <Grid item xs={12} md={9}>
                <Typography variant="h4" gutterBottom>
                  {movie.title}
                </Typography>
                <Typography variant="h5" gutterBottom>
                  Overview
                </Typography>
                <Typography variant="body1">{movie.overview}</Typography>
                <MovieDetails label="Genres" value={movie.genres} />
                <MovieDetails label="Release Date" value={movie.releaseDate} />
                <MovieDetails label="Runtime" value={`${movie.runtime} minutes`} />
                <MovieDetails label="Language" value={movie.originalLanguage} />
                <MovieDetails label="Production Countries" value={movie.productionCountries} />
                <MovieDetails label="Vote Average" value={movie.voteAverage} />
                <MovieDetails label="Vote Count" value={movie.voteCount} />
                <Button
                  variant="contained"
                  color="secondary"
                  style={{ marginTop: '20px' }}
                  onClick={() =>
                    window.open(`https://www.imdb.com/title/${movie.imdbId}`, '_blank')
                  }>
                  More Info
                </Button>
              </Grid>
            </Grid>
          </ContentContainer>
          <CommentContainer elevation={3}>
            <Typography variant="h5" gutterBottom>
              Comments
            </Typography>
            {dbMovie.comments.length === 0 ? (
              <Typography variant="body2" color="textSecondary">
                No comments yet.
              </Typography>
            ) : (
              dbMovie.comments.map((comment, index) => (
                <Typography key={index} variant="body2">
                  {comment.commentText}
                </Typography>
              ))
            )}
            <form onSubmit={handleCommentSubmit}>
              <TextField
                label="Add a comment"
                fullWidth
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                margin="normal"
                variant="outlined"
              />
              <Button type="submit" variant="contained" color="primary">
                Submit Comment
              </Button>
            </form>
          </CommentContainer>
        </>
      )}
    </div>
  );
}
