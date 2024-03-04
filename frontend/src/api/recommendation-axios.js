import axios from 'axios';
import authHeader from '../auth/auth-header';
// eslint-disable-next-line no-undef
const URL = process.env.REACT_APP_API_URL;

export const recommend = async (recommendingType, recommendingByType, recommendingBy) => {
  const response = await axios.post(
    `${URL}/recommend`,
    { recommendingType, recommendingByType, recommendingBy },
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
  const response = await axios.post(
    `${URL}/recommend/rate`,
    { recommendingType, recommendingByType, recommending, recommendingBy, rating },
    authHeader()
  );
  return response.data;
};
