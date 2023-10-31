import axios from 'axios';
import authHeader from '../auth/auth-header';

const URL = process.env.REACT_APP_API_URL;

export const getMovies = async () => {
  console.log(URL + `/movies`);
  const response = await axios.get(URL + `/movies`, authHeader());
  return response.data;
};
