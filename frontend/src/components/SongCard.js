import React from 'react';
import PlayCircleOutlineIcon from '@mui/icons-material/PlayCircleOutline';
import { useNavigate } from 'react-router-dom';
import { Card, Image, Text, Tooltip, Button, CardBody, Heading } from '@chakra-ui/react';

export default function SongCard({ song }) {
  const Navigate = useNavigate();
  return (
    <Card
      height={'100%'}
      transition="transform 0.2s ease-in-out, box-shadow 0.3s ease"
      _hover={{
        transform: 'scale(1.05)',
        boxShadow: '0 0 20px rgba(0,0,0,0.5)'
      }}
      cursor={'pointer'}
      onClick={() => {
        Navigate(`/songs/${song.id}`);
      }}>
      <Image src={song.imageUrl} alt="Album Cover" />
      <CardBody>
        <Heading as="h4" size="md">
          {song.title}
        </Heading>
        <Text variant="body2">{song.singer}</Text>
        <Tooltip hasArrow label="Listen on Spotify">
          <Button
            variant="ghost"
            size="sm"
            minHeight="1rem"
            minWidth="1rem"
            padding={0}
            onClick={(e) => {
              e.stopPropagation();
              const spotifyUri = `spotify:track:${song.spotifyId}`;

              window.location.href = spotifyUri;
            }}
            style={{
              width: 'fit-content',
              height: 'fit-content',
              margin: 'auto',
              color: 'green'
            }}>
            <PlayCircleOutlineIcon />
          </Button>
        </Tooltip>
      </CardBody>
    </Card>
  );
}
