import axios from 'axios';
import authHeader from '../auth/auth-header';
// eslint-disable-next-line no-undef
const URL = process.env.REACT_APP_API_URL;

export const getRecentlyPlayedGames = async (userId) => {
  const username = localStorage.getItem('userName');
  const response = await axios.post(
    `${URL}/games/getRecentlyPlayedGames`,
    { userId: userId, username: username },
    authHeader()
  );
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

export const getSteamHistory = async () => {
  const username = localStorage.getItem('userName');
  const response = await axios.post(
    `${URL}/games/steam-history`,
    { username: username },
    authHeader()
  );
  return response.data.games;
};

export const getGame = async (id) => {
  const response = await axios.get(`${URL}/games/${id}`, authHeader());
  return response.data;
};

export const getGameSuggestions = async (name) => {
  const response = await axios.post(`${URL}/games/suggestions`, { name: name }, authHeader());
  return response.data;
};
