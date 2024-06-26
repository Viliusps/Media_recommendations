import axios from 'axios';
import authHeader from '../auth/auth-header';
// eslint-disable-next-line no-undef
const URL = process.env.REACT_APP_API_URL;

export const recommend = async (
  recommendingType,
  recommendingByType,
  recommendingBy,
  recommendingByID
) => {
  var username = '';
  if (recommendingByType === 'Spotify' || recommendingByType === 'Steam')
    username = localStorage.getItem('userName');
  const response = await axios.post(
    `${URL}/recommend`,
    { recommendingType, recommendingByType, recommendingBy, username, recommendingByID },
    authHeader()
  );
  return response.data;
};

export const neuralRecommend = async (
  recommendingType,
  recommendingByType,
  recommendingBy,
  recommendingByID
) => {
  var username = '';
  if (recommendingByType === 'Spotify' || recommendingByType === 'Steam')
    username = localStorage.getItem('userName');
  const response = await axios.post(
    `${URL}/recommend/neural`,
    { recommendingType, recommendingByType, recommendingBy, username, recommendingByID },
    authHeader()
  );
  return response.data;
};

export const rateRecommendation = async (
  recommendingType,
  recommendingByType,
  recommending,
  recommendingBy,
  ratingInt
) => {
  const rating = Boolean(ratingInt);
  const username = localStorage.getItem('userName');
  const response = await axios.post(
    `${URL}/recommend/rate`,
    { recommendingType, recommendingByType, recommending, recommendingBy, rating, username },
    authHeader()
  );
  return response.data;
};

export const getRecentRecommendations = async () => {
  const username = localStorage.getItem('userName');
  const response = await axios.post(
    `${URL}/recommend/recent`,
    { username: username },
    authHeader()
  );
  return response.data;
};
