import axios from 'axios';
import authHeader from '../auth/auth-header';
// eslint-disable-next-line no-undef
const URL = process.env.REACT_APP_API_URL;

export const getMovies = async () => {
  const response = await axios.get(`${URL}/movies`, authHeader());
  return response.data;
};

export const getMovie = async (id) => {
  const response = await axios.get(`${URL}/movies/${id}`, authHeader());
  return response.data;
};

export const getPageMovies = async (page, size) => {
  const response = await axios.get(`${URL}/movies/page?page=${page}&size=${size}`, authHeader());
  return response.data;
};

export const getOmdbMovie = async (title) => {
  const response = await axios.post(`${URL}/movies/omdb`, { title }, authHeader());
  return response.data;
};

export const searchMovies = async (search) => {
  const response = await axios.post(`${URL}/movies/search`, { search: search }, authHeader());
  return response.data;
};
