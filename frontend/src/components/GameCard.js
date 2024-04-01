import { CardActionArea, Card, CardContent, CardMedia, Typography } from '@mui/material';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';

const StyledCard = styled(Card)`
  width: 345px;
`;

const StyledCardActionArea = styled(CardActionArea)`
  height: 100%;
`;

export default function GameCard({ game }) {
  const Navigate = useNavigate();
  return (
    <StyledCard>
      <StyledCardActionArea
        onClick={() => {
          Navigate(`/games/${game.id}`);
        }}>
        <CardMedia component="img" height="140" image={game.backgroundImage} />
        <CardContent>
          <Typography gutterBottom variant="h5" component="div">
            {game.name}
          </Typography>
        </CardContent>
      </StyledCardActionArea>
    </StyledCard>
  );
}
