import React from 'react';
import { Card, CardContent, CardActionArea, Typography, IconButton, Tooltip } from '@mui/material';
import styled from 'styled-components';
import PlayCircleOutlineIcon from '@mui/icons-material/PlayCircleOutline';
import { useNavigate } from 'react-router-dom';

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

const StyledCardActionArea = styled(CardActionArea)`
  height: 100%;
`;

export default function SongCard({ song }) {
  const Navigate = useNavigate();
  return (
    <StyledCard>
      <StyledCardActionArea
        onClick={() => {
          Navigate(`/songs/${song.id}`);
        }}>
        <StyledImg src={song.imageUrl || 'placeholder-image-url'} alt="Album Cover" />
        <StyledCardContent>
          <Typography variant="h6">{song.title}</Typography>
          <Typography variant="body2">{song.singer}</Typography>
          <Tooltip title="Listen on Spotify">
            <IconButton
              color="primary"
              onClick={() => {
                const spotifyUri = `spotify:track:${song.spotifyUrl}`;

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
      </StyledCardActionArea>
    </StyledCard>
  );
}
