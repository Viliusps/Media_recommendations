import {
  Container,
  Text,
  Accordion,
  AccordionItem,
  AccordionPanel,
  AccordionButton,
  AccordionIcon,
  Box,
  Stack
} from '@chakra-ui/react';
import GameCard from './GameCard';
import MovieCard from './MovieCard';
import SongCard from './SongCard';

export default function RecommendationsList({ recommendations }) {
  return (
    <>
      <h3>(Only recommendations you rated are shown)</h3>
      {recommendations != null && (
        <Accordion allowMultiple width={'50%'} margin={'auto'}>
          {recommendations.map((entry, index) => (
            <AccordionItem key={index}>
              <h2>
                <AccordionButton>
                  <Box as="span" flex={1} textAlign={'left'}>
                    {index + 1}. Input: <strong>{entry.originalType}</strong> Output:{' '}
                    <strong>{entry.type}</strong>
                  </Box>
                  <AccordionIcon />
                </AccordionButton>
              </h2>
              <AccordionPanel>
                <Stack display={'flex'} flexDirection={'row'}>
                  {entry.originalType && (
                    <Container display={'flex'} flexDirection={'column'}>
                      <Text>Requested {entry.originalType}:</Text>
                      {entry.originalType === 'Song' ? (
                        <SongCard song={entry.originalSong} />
                      ) : entry.originalType === 'Game' ? (
                        <GameCard game={entry.originalGame} />
                      ) : entry.originalType === 'Movie' ? (
                        <MovieCard movie={entry.originalMovie} />
                      ) : (
                        'N/A'
                      )}
                    </Container>
                  )}
                  {entry.type && (
                    <Container display={'flex'} flexDirection={'column'}>
                      <Text>Recommended {entry.type}:</Text>
                      {entry.type === 'Song' ? (
                        <SongCard song={entry.song} />
                      ) : entry.type === 'Game' ? (
                        <GameCard game={entry.game} />
                      ) : entry.type === 'Movie' ? (
                        <MovieCard movie={entry.movie} />
                      ) : (
                        'N/A'
                      )}
                    </Container>
                  )}
                </Stack>
              </AccordionPanel>
            </AccordionItem>
          ))}
        </Accordion>
      )}
    </>
  );
}
