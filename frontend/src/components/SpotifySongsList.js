import { Tooltip, Text, Icon } from '@chakra-ui/react';
import styled from 'styled-components';
import PlayCircleOutlineIcon from '@mui/icons-material/PlayCircleOutline';

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

export default function SpotifySongsList({ songs }) {
  return (
    <>
      <SongListContainer style={{ display: 'flex' }}>
        {songs.map((song, index) => (
          <SongCard key={index}>
            <SongImg src={song.imageUrl || 'placeholder-image-url'} alt="Album Cover" />
            <SongCardContent>
              <Text variant="h6">{song.title}</Text>
              <Text variant="body2">{song.artist}</Text>
              <Tooltip hasArrow label="Listen on Spotify">
                <Icon
                  cursor={'pointer'}
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
    </>
  );
}
