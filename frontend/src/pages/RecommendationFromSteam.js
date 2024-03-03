import React, { useState } from 'react';
import { GetRecentlyPlayedGames } from '../api/games-axios';
import { Button, TextField, Typography } from '@mui/material';
import styled from 'styled-components';
import LoadingWrapper from '../components/LoadingWrapper';
import { recommend } from '../api/recommendation-axios';
import { useParams } from 'react-router-dom';

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

const GameImage = styled('img')`
  width: 100%;
  height: auto;
  border-radius: 8px 8px 0 0;
`;

const GameName = styled('span')`
  margin: 10px 0;
  text-align: center;
`;

const Section = styled.div`
  margin: 20px 0;
`;

const RecommendationBox = styled.div`
  background-color: #ffffff;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;

  &:hover {
    box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
  }
`;

const RecommendationText = styled.p`
  font-size: 18px;
  font-weight: bold;
  color: #27ae60;
  cursor: pointer;
`;

const ContinueButton = styled(Button)`
  margin-top: 20px;
`;

const StyledH2 = styled.h2`
  margin-bottom: 10px;
  color: #333333;
`;

const RecommendationFromSteam = () => {
  const [recentGames, setRecentGames] = useState([]);
  const [userId, setUserId] = useState('');
  const [incorrect, setIncorrect] = useState(false);
  const [nonNumeric, setNonNumeric] = useState(false);
  const [error, setError] = useState(false);
  const [recommendation, setRecommendation] = useState('');
  const [loading, setLoading] = useState(false);
  const params = useParams();
  const { type } = params;

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
    console.log(recentGames);
    const gameNames = recentGames.map((game) => game.name);
    const resultString = gameNames.join(', ');
    console.log(resultString);
    setLoading(true);
    recommend(type, 'Steam', resultString)
      .then((result) => {
        if (result.type == 'Movie') setRecommendation(result.movie);
        else if (result.type == 'Song') setRecommendation(result.song);
        else if (result.type == 'Game') setRecommendation(result.game);
      })
      .catch((error) => {
        console.error(error);
        setError(true);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  return (
    <>
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
            <GamesGrid>
              {recentGames.map((game) => (
                <GameCard key={game.appid}>
                  <GameImage
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
      <ContinueButton onClick={() => getRecommendation()}>Continue</ContinueButton>
      <Section>
        <RecommendationBox>
          <StyledH2>Your Recommendation:</StyledH2>
          <LoadingWrapper loading={loading} error={error}>
            {recommendation && (
              <>
                {type === 'Song' && recommendation.spotifyId.length > 22 ? (
                  <RecommendationText>{recommendation.spotifyId}</RecommendationText>
                ) : (
                  type === 'Song' && (
                    <RecommendationText
                      onClick={() => {
                        const spotifyUri = `spotify:track:${recommendation.spotifyId}`;
                        window.location.href = spotifyUri;
                      }}>
                      Click here!
                    </RecommendationText>
                  )
                )}
                {type === 'Movie' && recommendation.imdbID.length > 9 ? (
                  <RecommendationText>{recommendation.imdbID}</RecommendationText>
                ) : (
                  type === 'Movie' && (
                    <RecommendationText
                      onClick={() =>
                        window.open(`https://www.imdb.com/title/${recommendation.imdbID}`, '_blank')
                      }>
                      Click here!
                    </RecommendationText>
                  )
                )}
              </>
            )}
          </LoadingWrapper>
        </RecommendationBox>
      </Section>
    </>
  );
};

export default RecommendationFromSteam;
