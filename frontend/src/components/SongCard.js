import React from 'react';
import { Card, CardContent, Typography, IconButton, Tooltip, styled } from '@mui/material';
import PlayCircleOutlineIcon from '@mui/icons-material/PlayCircleOutline';
import songImage from '../images/song.png';

const StyledCard = styled(Card)`
  height: 300px;
  width: 300px;
  display: flex;
  flex-direction: column;
`;

const StyledCardContent = styled(CardContent)`
  flex: 1;
  display: flex;
  flex-direction: column;
`;

export default function SongCard({ title, artist, spotifyUrl }) {
  return (
    <StyledCard>
      <img
        src={songImage || 'placeholder-image-url'}
        alt="Album Cover"
        style={{
          width: '100%',
          height: '200px',
          objectFit: 'cover',
          borderRadius: '4px'
        }}
      />
      <StyledCardContent>
        <Typography variant="h6">{title}</Typography>
        <Typography variant="body2">{artist}</Typography>
        <Tooltip title="Listen on Spotify">
          <IconButton
            color="primary"
            onClick={() => {
              const spotifyUri = `spotify:track:${spotifyUrl}`;

              window.location.href = spotifyUri;
            }}
            style={{
              width: 'fit-content',
              height: 'fit-content',
              margin: 'auto'
            }}>
            <PlayCircleOutlineIcon />
          </IconButton>
        </Tooltip>
      </StyledCardContent>
    </StyledCard>
  );
}
