import React, { useEffect, useState } from 'react';
import { getUserSpotifySongs } from '../api/songs-axios';
import PlayCircleOutlineIcon from '@mui/icons-material/PlayCircleOutline';
import styled from 'styled-components';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Text, Tooltip, Icon } from '@chakra-ui/react';

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

const ContinueButton = styled(Button)`
  margin-top: 20px;
`;

const StyledH1 = styled.h1`
  font-size: 24px;
  text-align: center;
  text-transform: uppercase;
`;

export default function RecentlyPlayedSongs() {
  const [userSongs, setUserSongs] = useState([]);
  const [loggedin, setLoggedin] = useState(false);
  const [showSongs, setShowSongs] = useState(false);
  const params = useParams();
  const { type } = params;
  const navigate = useNavigate();

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
  }, []);

  const getRecommendation = () => {
    const songNames = userSongs.map((song) => song.title);
    const resultString = songNames.join(', ');
    navigate(`/recommendationResults/${type}/${resultString}/${'Spotify'}`);
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
                  <Text variant="h6">{song.title}</Text>
                  <Text variant="body2">{song.artist}</Text>
                  <Tooltip title="Listen on Spotify">
                    <Icon
                      onClick={() => {
                        const spotifyUri = `spotify:track:${song.spotifyId}`;
                        window.location.href = spotifyUri;
                      }}
                      as={PlayCircleOutlineIcon}
                    />
                  </Tooltip>
                </SongCardContent>
              </SongCard>
            ))}
          </SongListContainer>
          <ContinueButton onClick={() => getRecommendation()}>Continue</ContinueButton>
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
