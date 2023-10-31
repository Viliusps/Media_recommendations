import axios from 'axios';
import authHeader from '../auth/auth-header';
const URL = process.env.REACT_APP_API_URL;

export const getSongs = async () => {
  const response = await axios.get(URL + `/songs`, authHeader());
  return response.data;
};
