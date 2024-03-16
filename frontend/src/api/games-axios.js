import axios from 'axios';
import authHeader from '../auth/auth-header';
// eslint-disable-next-line no-undef
const URL = process.env.REACT_APP_API_URL;

export const getRecentlyPlayedGames = async (userId) => {
  const response = await axios.post(
    `${URL}/games/getRecentlyPlayedGames`,
    { userId: userId },
    authHeader()
  );
  return response.data;
};

export const checkIfGameExists = async (name) => {
  const response = await axios.post(`${URL}/games/check`, { name: name }, authHeader());
  return response.data;
};
