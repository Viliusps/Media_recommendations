import axios from 'axios';
import authHeader from '../auth/auth-header';
// eslint-disable-next-line no-undef
const URL = process.env.REACT_APP_API_URL;

export const GetRecentlyPlayedGames = async (userId) => {
  const response = await axios.post(
    `${URL}/games/getRecentlyPlayedGames`,
    { userId: userId },
    authHeader()
  );
  return response.data;
};
