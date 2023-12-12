import axios from 'axios';
import authHeader from '../auth/auth-header';
// eslint-disable-next-line no-undef
const URL = process.env.REACT_APP_API_URL;

export const getUsers = async () => {
  const response = await axios.get(`${URL}/users`, authHeader());
  return response.data;
};

export const deleteUser = async (index) => {
  const response = await axios.delete(URL + `/users/${index}`, authHeader());
  return response.data;
};

export const updateUser = async (index, username, email, password) => {
  const response = await axios.put(
    URL + `/users/${index}`,
    {
      username: username,
      email: email,
      password: password
    },
    authHeader()
  );
  return response.data;
};
