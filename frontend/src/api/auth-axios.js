import axios from 'axios';
import authHeader from '../auth/auth-header';
// eslint-disable-next-line no-undef
const URL = process.env.REACT_APP_API_URL;

export const login = async (username, password) => {
  const response = await axios.post(`${URL}/auth/authenticate`, {
    username: username,
    password: password
  });
  localStorage.setItem('token', response.data.token);
  return response.data;
};

export const register = async (username, email, password) => {
  const response = await axios.post(`${URL}/auth/register`, {
    username: username,
    email: email,
    password: password
  });
  return response.data;
};

export const getRole = async () => {
  try {
    const token = localStorage.getItem('token');
    if (token != null) {
      const response = await axios.post(`${URL}/users/decode`, token, authHeader());
      return response.data;
    } else return 'GUEST';
  } catch {
    localStorage.clear();
    return 'GUEST';
  }
};
