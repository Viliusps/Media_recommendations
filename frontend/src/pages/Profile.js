import { useEffect, useState } from 'react';
import { getSpotifyHistory } from '../api/songs-axios';
import { getSteamHistory } from '../api/games-axios';
import { getRecentRecommendations } from '../api/recommendation-axios';
import { Heading, Tabs, TabList, TabPanels, Tab, TabPanel } from '@chakra-ui/react';
import SpotifySongsList from '../components/SpotifySongsList';
import SteamGamesList from '../components/SteamGamesList';
import RecommendationsList from '../components/RecommendationsList';

export default function Profile() {
  const [steamHistory, setSteamHistory] = useState([]);
  const [spotifyHistory, setSpotifyHistory] = useState([]);
  const [recentRecommendations, setRecentRecommendations] = useState([]);
  const [username, setUsername] = useState('');

  useEffect(() => {
    setUsername(localStorage.getItem('userName'));
    getSpotifyHistory().then((results) => {
      setSpotifyHistory(results);
    });
    getSteamHistory().then((results) => {
      setSteamHistory(results);
    });
    getRecentRecommendations().then((results) => {
      setRecentRecommendations(results);
    });
  }, []);
  return (
    <>
      <Heading>{username}&apos;s profile</Heading>
      <Tabs variant={'enclosed'}>
        <TabList>
          <Tab borderWidth={2}>Recent recommendations</Tab>
          <Tab borderWidth={2}>Recent songs from Spotify</Tab>
          <Tab borderWidth={2}>Recent games from Steam</Tab>
        </TabList>
        <TabPanels>
          <TabPanel>
            <Heading as="h3" size="md">
              Your recent recommendations:{' '}
            </Heading>
            <RecommendationsList recommendations={recentRecommendations} />
          </TabPanel>
          <TabPanel>
            <Heading as="h3" size="md">
              Your recently listened songs:{' '}
            </Heading>
            <SpotifySongsList songs={spotifyHistory} />
          </TabPanel>
          <TabPanel>
            <Heading as="h3" size="md">
              Your recently played games:{' '}
            </Heading>
            <SteamGamesList games={steamHistory} />
          </TabPanel>
        </TabPanels>
      </Tabs>
    </>
  );
}
