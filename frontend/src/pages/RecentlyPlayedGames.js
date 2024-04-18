import React, { useState } from 'react';
import { getRecentlyPlayedGames } from '../api/games-axios';
import { useNavigate, useParams } from 'react-router-dom';
import {
  Button,
  Text,
  Input,
  Container,
  useColorModeValue,
  Heading,
  Tooltip,
  Flex
} from '@chakra-ui/react';
import { RxQuestionMarkCircled } from 'react-icons/rx';
import SteamGamesList from '../components/SteamGamesList';

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
      console.log('h');
      setIncorrect(true);
      setNonNumeric(false);
      return;
    }

    if (!/^\d+$/.test(trimmedUserId)) {
      console.log('h');
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
        <Heading as="h3" size="md">
          Enter your Steam user ID to get your recent games
        </Heading>
        {error && (
          <Text color={'red'}>
            Error. Make sure the id is correct and profile is set to public.
          </Text>
        )}
        {(incorrect || nonNumeric) && (
          <Text color={'red'}>
            Error. User ID cannot be empty or contain non-numeric characters.
          </Text>
        )}
        <Flex alignItems="center">
          <Input
            marginTop={2}
            marginBottom={2}
            placeholder="Steam User ID"
            variant="outlined"
            value={userId}
            onChange={handleInputChange}
            isInvalid={incorrect || nonNumeric}
            isRequired
          />
          <Tooltip
            hasArrow
            label="Your Steam user ID can be found within Steam by clicking your username in the top right and then clicking Account details."
            fontSize="md">
            <Button variant="ghost" size="sm" minHeight="1rem" minWidth="1rem" padding={0}>
              <RxQuestionMarkCircled />
            </Button>
          </Tooltip>
        </Flex>
        <Button
          px={8}
          bg={useColorModeValue('#151f21', 'gray.900')}
          color={'white'}
          rounded={'md'}
          _hover={{
            transform: 'translateY(-2px)',
            boxShadow: 'lg'
          }}
          onClick={() => getGames()}>
          Get Games
        </Button>
        {recentGames.length > 0 && (
          <div>
            <Heading as="h3" size="md" marginTop={5} marginBottom={3}>
              Recently Played Games:
            </Heading>
            <SteamGamesList games={recentGames} />
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
          </div>
        )}
      </Container>
    </>
  );
};

export default RecentlyPlayedGames;
