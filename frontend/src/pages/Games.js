import React, { useState } from 'react';
import { GetRecentlyPlayedGames } from '../api/games-axios';
import { Button, TextField, Typography, styled } from '@mui/material';

const StyledText = styled(Typography)`
  color: red;
  margin-bottom: 10px;
`;

const Container = styled('div')`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
`;

const Games = () => {
  const [recentGames, setRecentGames] = useState([]);
  const [userId, setUserId] = useState('');
  const [incorrect, setIncorrect] = useState(false);
  const [nonNumeric, setNonNumeric] = useState(false);
  const [error, setError] = useState(false);

  const getGames = () => {
    const trimmedUserId = userId.trim();

    if (!trimmedUserId) {
      setIncorrect(true);
      setNonNumeric(false);
      return;
    }

    if (!/^\d+$/.test(trimmedUserId)) {
      setNonNumeric(true);
      setIncorrect(false);
      return;
    }

    GetRecentlyPlayedGames(trimmedUserId)
      .then((result) => {
        console.log(result);
        if (result.response.games.length == 0) setError(true);
        else {
          setRecentGames(result.response.games);
          setIncorrect(false);
          setNonNumeric(false);
        }
      })
      .catch(() => {
        setError(true);
      });
  };

  const handleInputChange = (e) => {
    setUserId(e.target.value);
    setIncorrect(false);
    setNonNumeric(false);
  };

  return (
    <Container>
      {error && (
        <StyledText>Error. Make sure the id is correct and profile is set to public.</StyledText>
      )}
      <TextField
        label="Enter Steam User ID"
        variant="outlined"
        value={userId}
        onChange={handleInputChange}
        margin="normal"
        error={incorrect || nonNumeric}
        helperText={
          (incorrect || nonNumeric) && 'User ID cannot be empty or contain non-numeric characters'
        }
        required
      />
      <Button variant="contained" color="primary" onClick={() => getGames()}>
        Get Games
      </Button>
      {recentGames.length > 0 && (
        <div>
          <h3>Recently Played Games:</h3>
          <ul>
            {recentGames.map((game) => (
              <li key={game.appid}>{game.name}</li>
            ))}
          </ul>
        </div>
      )}
    </Container>
  );
};

export default Games;
