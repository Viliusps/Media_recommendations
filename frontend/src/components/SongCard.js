import React from 'react';
import { Card, CardContent, Typography, IconButton, Tooltip } from '@mui/material';
import styled from 'styled-components';
import PlayCircleOutlineIcon from '@mui/icons-material/PlayCircleOutline';

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

const StyledImg = styled.img`
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
`;
export default function SongCard({ title, artist, imageUrl, spotifyUrl }) {
  return (
    <StyledCard>
      <StyledImg src={imageUrl || 'placeholder-image-url'} alt="Album Cover" />
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
