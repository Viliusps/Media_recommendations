import axios from 'axios';
import authHeader from '../auth/auth-header';
// eslint-disable-next-line no-undef
const URL = process.env.REACT_APP_API_URL;

export const getMovies = async () => {
  const response = await axios.get(URL + `/movies`, authHeader());
  return response.data;
};
