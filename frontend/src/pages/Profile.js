import { useEffect, useState } from 'react';
import { getSpotifyHistory } from '../api/songs-axios';
import { getSteamHistory } from '../api/games-axios';
import { getRecentRecommendations } from '../api/recommendation-axios';
import { Grid, GridItem, Heading } from '@chakra-ui/react';

export default function Profile() {
  const [steamHistory, setSteamHistory] = useState([]);
  const [spotifyHistory, setSpotifyHistory] = useState([]);
  const [recentRecommendations, setRecentRecommendations] = useState([]);

  useEffect(() => {
    getSpotifyHistory().then((results) => {
      setSpotifyHistory(results);
    });
    getSteamHistory().then((results) => {
      setSteamHistory(results);
    });
    getRecentRecommendations().then((results) => {
      setRecentRecommendations(results);
      console.log(results);
    });
  }, []);
  return (
    <>
      <Heading>Profile</Heading>
      <Grid templateRows="repeat(2, 1fr)" templateColumns="repeat(5, 1fr)" gap={4}>
        <GridItem rowSpan={2} colSpan={2}>
          <Heading as="h3" size="md">
            Your recently listened songs:{' '}
          </Heading>
          {spotifyHistory != null &&
            spotifyHistory.map((entry, index) => <p key={index}>{entry.title}</p>)}
        </GridItem>
        <GridItem rowSpan={2} colSpan={2}>
          <Heading as="h3" size="md">
            Your recently played games:{' '}
          </Heading>
          {steamHistory != null &&
            steamHistory.map((entry, index) => <p key={index}>{entry.name}</p>)}
        </GridItem>
        <GridItem rowSpan={1} colSpan={1}>
          <Heading as="h3" size="md">
            Your recent recommendations:{' '}
          </Heading>
          <h4>(only recommendations you rated show up)</h4>
          {recentRecommendations != null &&
            recentRecommendations.map((entry, index) => (
              <div key={index}>
                {entry.type && (
                  <p>
                    Recommended {entry.type}:{' '}
                    {entry.type === 'Song'
                      ? entry.song.title
                      : entry.type === 'Game'
                      ? entry.game.name
                      : entry.type === 'Movie'
                      ? entry.movie.Title
                      : 'N/A'}
                  </p>
                )}
                {entry.originalType && (
                  <p>
                    Requested {entry.originalType}:{' '}
                    {entry.originalType === 'Song'
                      ? entry.originalSong.title
                      : entry.originalType === 'Game'
                      ? entry.originalGame.name
                      : entry.originalType === 'Movie'
                      ? entry.originalMovie.Title
                      : 'N/A'}
                  </p>
                )}
                <p>------------------------------------------------------------</p>
              </div>
            ))}
        </GridItem>
      </Grid>
    </>
  );
}
