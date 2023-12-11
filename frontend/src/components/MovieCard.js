import { CardActionArea, Card, CardContent, CardMedia, Typography } from '@mui/material';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';

const StyledCard = styled(Card)`
  width: 345px;
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
        <CardMedia component="img" height="140" image={movie.Poster} />
        <CardContent>
          <Typography gutterBottom variant="h5" component="div">
            {movie.Title}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            {movie.Plot?.length > 200 ? movie.Plot.slice(0, 200) + '...' : movie.Plot}
          </Typography>
        </CardContent>
      </StyledCardActionArea>
    </StyledCard>
  );
}
