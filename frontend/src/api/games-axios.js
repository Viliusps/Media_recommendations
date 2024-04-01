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

export const getPageGames = async (page, size) => {
  const response = await axios.get(`${URL}/games/page?page=${page}&size=${size}`, authHeader());
  return response.data;
};

export const searchGames = async (search) => {
  const response = await axios.post(`${URL}/games/search`, { search: search }, authHeader());
  return response.data;
};
