import { login } from '../api/auth-axios';
import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import TextField from '@mui/material/TextField';
import { Button } from '@mui/material';

export default function Login() {
  const formRef = useRef();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const Navigate = useNavigate();

  function handleSubmit() {
    login(username, password)
      .then(() => {
        Navigate('/movies');
      })
      .catch((error) => {
        if (error.response && error.response.status === 403) {
          console.error('Wrong login data:', error);
        } else {
          console.error('An error occurred:', error);
        }
      });
  }

  function handleUsernameChange(event) {
    setUsername(event.target.value);
  }

  function handlePasswordChange(event) {
    setPassword(event.target.value);
  }

  return (
    <form ref={formRef}>
      <TextField label="Username" placeholder="Username" onChange={handleUsernameChange} required />
      <TextField
        label="Password"
        placeholder="Password"
        required
        mt="md"
        onChange={handlePasswordChange}
      />
      <Button
        fullWidth
        mt="xl"
        onClick={() => (formRef.current.reportValidity() ? handleSubmit() : null)}>
        Log in
      </Button>
    </form>
  );
}
