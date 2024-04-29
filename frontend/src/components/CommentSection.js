import StarRating from './StarRating';
import styled from 'styled-components';
import { useState } from 'react';
import { postComment } from '../api/comments-axios';
import { getMovie } from '../api/movies-axios';
import { getGame } from '../api/games-axios';
import { getSong } from '../api/songs-axios';
import { toast } from 'react-toastify';
import {
  Text,
  Input,
  Button,
  Divider,
  useColorModeValue,
  Heading,
  Avatar,
  HStack
} from '@chakra-ui/react';
import { Pagination } from '@mui/material';

const CommentContainer = styled.div`
  padding: 20px;
  margin-top: 20px;
  border-radius: 8px;
`;

const CommentField = styled(Input)`
  max-width: 400px;
`;

const StyledText = styled(Text)`
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

export default function CommentSection({ object, id, setObject, type }) {
  const [comment, setComment] = useState('');
  const [rating, setRating] = useState(0);
  const [errorMesssage, setErrorMessage] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const commentsPerPage = 5;

  const indexOfLastComment = currentPage * commentsPerPage;
  const indexOfFirstComment = indexOfLastComment - commentsPerPage;
  console.log(object?.comments);
  const currentComments = (object?.comments || []).slice(indexOfFirstComment, indexOfLastComment);

  const handlePageChange = (event, value) => {
    setCurrentPage(value);
  };

  const handleCommentSubmit = () => {
    if (comment === '') setErrorMessage('Please enter a comment.');
    else if (comment.length > 300) setErrorMessage('Your comment must be less than 300 characters');
    else if (rating === 0) setErrorMessage('Please select a rating.');
    else {
      let gameComment = null;
      let movieComment = null;
      let songComment = null;
      setErrorMessage('');
      if (type == 'Movie') movieComment = id;
      if (type == 'Game') gameComment = id;
      if (type == 'Song') songComment = id;
      postComment(movieComment, songComment, gameComment, comment, rating).then(() => {
        if (type == 'Movie') {
          getMovie(id).then((data) => {
            showToastMessage();
            setObject(data);
          });
        }
        if (type == 'Game') {
          getGame(id).then((data) => {
            showToastMessage();
            setObject(data);
          });
        }
        if (type == 'Song') {
          getSong(id).then((data) => {
            showToastMessage();
            setObject(data);
          });
        }
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
            placeholder="Add a comment"
            fullWidth
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            margin="normal"
            variant="outlined"
          />
          <StarRating rating={rating} onRatingChange={handleRatingChange} />
          <StyledText>{errorMesssage}</StyledText>
          <Button
            px={8}
            bg={useColorModeValue('#151f21', 'gray.900')}
            color={'white'}
            rounded={'md'}
            _hover={{
              transform: 'translateY(-2px)',
              boxShadow: 'lg'
            }}
            onClick={() => handleCommentSubmit()}>
            Submit Comment
          </Button>
          <StyledDivider />
        </>
      )}

      <Heading as="h4" size="md">
        Comments
      </Heading>
      {object.comments.length === 0 ? (
        <Text>No comments yet.</Text>
      ) : (
        currentComments.map((comment, index) => (
          <StyledDiv key={index}>
            <StyledDivider />
            <HStack align="center">
              <Avatar size={'sm'} />
              <Text>
                {comment.user.username}- {comment.commentText}
              </Text>
            </HStack>

            <StarRating rating={comment.rating} onRatingChange={() => {}} />
          </StyledDiv>
        ))
      )}
      <StyledPagination
        count={Math.ceil(object?.comments.length / commentsPerPage)}
        page={currentPage}
        onChange={handlePageChange}
        color="primary"
      />
    </CommentContainer>
  );
}
