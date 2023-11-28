import { CardActionArea, Card, CardContent, CardMedia, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import movieImage from '../images/movie.png';

export default function DisplayCard({ movie }) {
  const Navigate = useNavigate();
  return (
    <Card sx={{ maxWidth: 345 }}>
      <CardActionArea
        onClick={() => {
          Navigate(`/movies/${movie.id}`);
        }}>
        <CardMedia component="img" height="140" image={movieImage} />
        <CardContent>
          <Typography gutterBottom variant="h5" component="div">
            {movie.originalTitle}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            {movie.overview}
          </Typography>
        </CardContent>
      </CardActionArea>
    </Card>
  );
}
