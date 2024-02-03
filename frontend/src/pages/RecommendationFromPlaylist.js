import React, { useEffect, useState } from 'react';
import { getUserSpotifySongs } from '../api/songs-axios';
import { Button, Typography, Tooltip, IconButton } from '@mui/material';
import PlayCircleOutlineIcon from '@mui/icons-material/PlayCircleOutline';
import styled from 'styled-components';
import { recommend } from '../api/recommendation-axios';
import { useParams } from 'react-router-dom';
import LoadingWrapper from '../components/LoadingWrapper';

// eslint-disable-next-line no-undef
const clientId = process.env.REACT_APP_SPOTIFY_CLIENT_ID;

const PageContainer = styled.div`
  padding: 20px;
  text-align: center;
`;

const SongListContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  margin-top: 20px;
`;

const SongCard = styled.div`
  background-color: #f0f0f0;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin: 10px;
  overflow: hidden;
  width: 200px;
  text-align: center;
  transition: transform 0.2s;

  &:hover {
    transform: scale(1.05);
  }
`;

const SongImg = styled.img`
  width: 100%;
  height: 120px;
  object-fit: cover;
`;

const SongCardContent = styled.div`
  padding: 10px;
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

const StyledH1 = styled.h1`
  font-size: 24px;
  text-align: center;
  text-transform: uppercase;
`;

export default function RecommendationFromPlaylist() {
  const [userSongs, setUserSongs] = useState([]);
  const [loggedin, setLoggedin] = useState(false);
  const [showSongs, setShowSongs] = useState(false);
  const [recommendation, setRecommendation] = useState('');
  const params = useParams();
  const { type } = params;
  const [error, setError] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleShowSongs = () => {
    setShowSongs(!showSongs);
  };

  const handleLogin = () => {
    const redirectUri = `http://localhost:3000/playlistRecommendation/${type}`;
    const authUrl = `https://accounts.spotify.com/authorize?client_id=${clientId}&redirect_uri=${encodeURIComponent(
      redirectUri
    )}&response_type=token&scope=user-read-recently-played`;
    window.location.href = authUrl;
  };

  useEffect(() => {
    const params = new URLSearchParams(window.location.hash.substring(1));
    const token = params.get('access_token');

    if (token) {
      localStorage.setItem('spotifyAuthToken', token);
    }
    if (token || localStorage.getItem('spotifyAuthToken')) {
      setLoggedin(true);
      getUserSpotifySongs().then((data) => {
        setUserSongs(data);
      });
    }
  }, [recommendation]);

  const getRecommendation = () => {
    const songNames = userSongs.map((song) => song.title);
    const resultString = songNames.join(', ');
    setLoading(true);
    recommend(type, 'Spotify', resultString)
      .then((result) => {
        setRecommendation(result);
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
    <PageContainer>
      {loggedin ? (
        <>
          <StyledH1>Recommending based on a playlist.</StyledH1>
          <ContinueButton variant="contained" onClick={handleShowSongs}>
            {showSongs ? 'Hide songs' : 'Show my most recent songs'}
          </ContinueButton>
          <SongListContainer style={{ display: showSongs ? 'flex' : 'none' }}>
            {userSongs.map((song, index) => (
              <SongCard key={index}>
                <SongImg src={song.imageUrl || 'placeholder-image-url'} alt="Album Cover" />
                <SongCardContent>
                  <Typography variant="h6">{song.title}</Typography>
                  <Typography variant="body2">{song.artist}</Typography>
                  <Tooltip title="Listen on Spotify">
                    <IconButton
                      color="primary"
                      onClick={() => {
                        const spotifyUri = `spotify:track:${song.spotifyId}`;
                        window.location.href = spotifyUri;
                      }}>
                      <PlayCircleOutlineIcon />
                    </IconButton>
                  </Tooltip>
                </SongCardContent>
              </SongCard>
            ))}
          </SongListContainer>
          <ContinueButton onClick={() => getRecommendation()}>Continue</ContinueButton>
          <Section>
            <RecommendationBox>
              <StyledH2>Your Recommendation:</StyledH2>
              <LoadingWrapper loading={loading} error={error}>
                {recommendation && (
                  <>
                    {type === 'Song' && recommendation.id.length > 22 ? (
                      <RecommendationText>{recommendation.id}</RecommendationText>
                    ) : (
                      type === 'Song' && (
                        <RecommendationText
                          onClick={() => {
                            const spotifyUri = `spotify:track:${recommendation.id}`;
                            window.location.href = spotifyUri;
                          }}>
                          Click here!
                        </RecommendationText>
                      )
                    )}
                    {type === 'Movie' && recommendation.id.length > 9 ? (
                      <RecommendationText>{recommendation.id}</RecommendationText>
                    ) : (
                      type === 'Movie' && (
                        <RecommendationText
                          onClick={() =>
                            window.open(`https://www.imdb.com/title/${recommendation.id}`, '_blank')
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
      ) : (
        <>
          <h1>Not logged in</h1>
          <Button variant="contained" onClick={() => handleLogin()}>
            Login with Spotify
          </Button>
        </>
      )}
    </PageContainer>
  );
}
