import { CardActionArea, Card, CardContent, Typography, styled } from '@mui/material';

const StyledCard = styled(Card)`
  width: 600px;
  height: 700px;
`;

const StyledCardActionArea = styled(CardActionArea)`
  height: 100%;
`;

export default function CustomCard({ title, image, handleOpen }) {
  return (
    <StyledCard>
      <StyledCardActionArea
        onClick={() => {
          handleOpen();
        }}>
        <img src={image} />

        <CardContent>
          <Typography gutterBottom variant="h5" component="div">
            {title}
          </Typography>
        </CardContent>
      </StyledCardActionArea>
    </StyledCard>
  );
}
