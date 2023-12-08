import axios from 'axios';
import authHeader from '../auth/auth-header';
// eslint-disable-next-line no-undef
const URL = process.env.REACT_APP_API_URL;

export const postComment = async (movie, commentText, rating) => {
  await axios.post(
    `${URL}/comments`,
    { movie: movie, commentText: commentText, rating: rating },
    authHeader()
  );
};
