import axios from 'axios';
import authHeader from '../auth/auth-header';
// eslint-disable-next-line no-undef
const URL = process.env.REACT_APP_API_URL;

export const getSongs = async () => {
  const response = await axios.get(`${URL}/songs`, authHeader());
  return response.data;
};

export const getPageSongs = async (page, size) => {
  const response = await axios.get(`${URL}/songs/page?page=${page}&size=${size}`, authHeader());
  return response.data;
};

export const getUserSpotifySongs = async () => {
  const token = localStorage.getItem('spotifyAuthToken');
  const username = localStorage.getItem('userName');
  const response = await axios.post(
    `${URL}/songs/spotify`,
    { token: token, username: username },
    authHeader()
  );
  return response.data;
};

export const searchSongs = async (search) => {
  const response = await axios.post(`${URL}/songs/search`, { search: search }, authHeader());
  return response.data;
};

export const checkIfSongExists = async (name) => {
  const response = await axios.post(`${URL}/songs/check`, { name: name }, authHeader());
  return response.data;
};
