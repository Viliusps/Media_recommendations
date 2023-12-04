import { CardActionArea, Card, CardContent, Typography, styled } from '@mui/material';

const StyledCard = styled(Card)`
  text-align: center;
`;

const StyledCardActionArea = styled(CardActionArea)`
  height: 100%;
`;

export default function CustomCard({ title, image, handleOpen, cardWidth, cardHeight }) {
  return (
    <StyledCard style={{ width: cardWidth, height: cardHeight }}>
      <StyledCardActionArea
        onClick={() => {
          handleOpen();
        }}>
        <img style={{ height: '200px', width: '200px' }} src={image} />

        <CardContent>
          <Typography gutterBottom variant="h5" component="div">
            {title}
          </Typography>
        </CardContent>
      </StyledCardActionArea>
    </StyledCard>
  );
}
