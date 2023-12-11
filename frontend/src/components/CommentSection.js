import { Pagination, Divider, Typography, Paper, TextField, Button } from '@mui/material';
import StarRating from './StarRating';
import styled from 'styled-components';
import { useState } from 'react';
import { postComment } from '../api/comments-axios';
import { getMovie } from '../api/movies-axios';
import { toast } from 'react-toastify';

const CommentContainer = styled(Paper)`
  padding: 20px;
  margin-top: 20px;
  border-radius: 8px;
`;

const CommentField = styled(TextField)`
  max-width: 400px;
`;

const StyledText = styled(Typography)`
  color: red;
`;

const StyledPagination = styled(Pagination)`
  margin: 20px auto;
  display: flex;
  justify-content: center;
  max-width: 300px;
`;

const StyledDiv = styled.div`
  max-width: 400px;
  word-wrap: break-word;
  margin: auto;
`;

const StyledDivider = styled(Divider)`
  margin: 20px auto 20px auto !important;
  max-width: 800px;
`;

export default function CommentSection({ movie, id, setMovie }) {
  const [comment, setComment] = useState('');
  const [rating, setRating] = useState(0);
  const [errorMesssage, setErrorMessage] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const commentsPerPage = 5;

  const indexOfLastComment = currentPage * commentsPerPage;
  const indexOfFirstComment = indexOfLastComment - commentsPerPage;
  const currentComments = (movie?.comments || []).slice(indexOfFirstComment, indexOfLastComment);

  const handlePageChange = (event, value) => {
    setCurrentPage(value);
  };

  const handleCommentSubmit = () => {
    if (comment === '') setErrorMessage('Please enter a comment.');
    else if (comment.length > 300) setErrorMessage('Your comment must be less than 300 characters');
    else if (rating === 0) setErrorMessage('Please select a rating.');
    else {
      setErrorMessage('');
      postComment(id, comment, rating).then(() => {
        getMovie(id).then((data) => {
          showToastMessage();
          setMovie(data);
        });
      });
    }
  };

  const handleRatingChange = (newRating) => {
    setRating(newRating);
  };

  const showToastMessage = () => {
    toast.success('Comment posted successfully!', {
      position: 'top-center',
      autoClose: 1500,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: false,
      draggable: false,
      progress: undefined,
      theme: 'light'
    });
  };

  return (
    <CommentContainer elevation={3}>
      {localStorage.getItem('token') && (
        <>
          <CommentField
            label="Add a comment"
            fullWidth
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            margin="normal"
            variant="outlined"
          />
          <StarRating rating={rating} onRatingChange={handleRatingChange} />
          <StyledText>{errorMesssage}</StyledText>
          <Button
            type="submit"
            variant="contained"
            color="primary"
            onClick={() => handleCommentSubmit()}>
            Submit Comment
          </Button>
          <StyledDivider />
        </>
      )}

      <Typography variant="h5" gutterBottom>
        Comments
      </Typography>
      {movie.comments.length === 0 ? (
        <Typography variant="body2" color="textSecondary">
          No comments yet.
        </Typography>
      ) : (
        currentComments.map((comment, index) => (
          <StyledDiv key={index}>
            <StyledDivider />
            <Typography key={index} variant="body2">
              {comment.commentText}
            </Typography>
            <StarRating rating={comment.rating} onRatingChange={() => {}} />
          </StyledDiv>
        ))
      )}
      <StyledPagination
        count={Math.ceil(movie?.comments.length / commentsPerPage)}
        page={currentPage}
        onChange={handlePageChange}
        color="primary"
      />
    </CommentContainer>
  );
}
