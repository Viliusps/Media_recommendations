import React, { useState } from 'react';
import { getRecentlyPlayedGames } from '../api/games-axios';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Text, Input, Container, Image } from '@chakra-ui/react';
import styled from 'styled-components';

const GamesGrid = styled('div')`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
`;

const GameCard = styled('div')`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`;

const GameName = styled('span')`
  margin: 10px 0;
  text-align: center;
`;

const RecentlyPlayedGames = () => {
  const [recentGames, setRecentGames] = useState([]);
  const [userId, setUserId] = useState('');
  const [incorrect, setIncorrect] = useState(false);
  const [nonNumeric, setNonNumeric] = useState(false);
  const [error, setError] = useState(false);
  const params = useParams();
  const { type } = params;
  const navigate = useNavigate();

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

    getRecentlyPlayedGames(trimmedUserId)
      .then((result) => {
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

  const getRecommendation = () => {
    const gameNames = recentGames.map((game) => game.name);
    const resultString = gameNames.join(', ');
    navigate(`/recommendationResults/${type}/${resultString}/${'Steam'}`);
  };

  return (
    <>
      <Container>
        {error && <Text>Error. Make sure the id is correct and profile is set to public.</Text>}
        <Input
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
            <GamesGrid>
              {recentGames.map((game) => (
                <GameCard key={game.appid}>
                  <Image
                    src={`https://steamcdn-a.akamaihd.net/steam/apps/${game.appid}/header.jpg`}
                    alt={game.name}
                  />
                  <GameName>{game.name}</GameName>
                </GameCard>
              ))}
            </GamesGrid>
          </div>
        )}
      </Container>
      <Button onClick={() => getRecommendation()}>Continue</Button>
    </>
  );
};

export default RecentlyPlayedGames;
