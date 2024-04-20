import React, { useEffect, useState } from 'react';
import { getUserSpotifySongs } from '../api/songs-axios';
import styled from 'styled-components';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, useColorModeValue, Heading, VStack } from '@chakra-ui/react';
import SpotifySongsList from '../components/SpotifySongsList';

// eslint-disable-next-line no-undef
const clientId = process.env.REACT_APP_SPOTIFY_CLIENT_ID;

const PageContainer = styled.div`
  padding: 20px;
  text-align: center;
`;

export default function RecentlyPlayedSongs() {
  const [userSongs, setUserSongs] = useState([]);
  const [loggedin, setLoggedin] = useState(false);
  const params = useParams();
  const { type } = params;
  const navigate = useNavigate();
  const [showSongs, setShowSongs] = useState(false);
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
          <Heading>Recommending based on your Spotify playlist.</Heading>
          <VStack>
            <Button
              marginTop={2}
              px={8}
              bg={useColorModeValue('#151f21', 'gray.900')}
              color={'white'}
              rounded={'md'}
              _hover={{
                transform: 'translateY(-2px)',
                boxShadow: 'lg'
              }}
              onClick={handleShowSongs}>
              {showSongs ? 'Hide songs' : 'Show my most recent songs'}
            </Button>
            {showSongs && <SpotifySongsList songs={userSongs} />}
            <Button
              marginTop={2}
              px={8}
              bg={useColorModeValue('#151f21', 'gray.900')}
              color={'white'}
              rounded={'md'}
              _hover={{
                transform: 'translateY(-2px)',
                boxShadow: 'lg'
              }}
              onClick={() => getRecommendation()}>
              Continue
            </Button>
          </VStack>
        </>
      ) : (
        <>
          <Button
            marginTop={2}
            px={8}
            bg={useColorModeValue('#151f21', 'gray.900')}
            color={'white'}
            rounded={'md'}
            _hover={{
              transform: 'translateY(-2px)',
              boxShadow: 'lg'
            }}
            onClick={() => handleLogin()}>
            Login with Spotify
          </Button>
        </>
      )}
    </PageContainer>
  );
}
