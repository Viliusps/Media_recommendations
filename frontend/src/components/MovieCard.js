import { CardActionArea, Card, CardContent, CardMedia, Typography, styled } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const StyledCard = styled(Card)`
  max-width: 345px;
`;

const StyledCardActionArea = styled(CardActionArea)`
  height: 100%;
`;

export default function DisplayCard({ movie }) {
  const Navigate = useNavigate();
  return (
    <StyledCard>
      <StyledCardActionArea
        onClick={() => {
          Navigate(`/movies/${movie.id}`);
        }}>
        <CardMedia component="img" height="140" image={movie.imageUrl} />
        <CardContent>
          <Typography gutterBottom variant="h5" component="div">
            {movie.title}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            {movie.overview}
          </Typography>
        </CardContent>
      </StyledCardActionArea>
    </StyledCard>
  );
}
