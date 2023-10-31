import axios from 'axios';
const URL = process.env.REACT_APP_API_URL;

export const login = async (username, password) => {
  const response = await axios.post(URL + '/auth/authenticate', {
    username: username,
    password: password
  });
  localStorage.setItem('token', response.data.token);
  return response.data;
};
