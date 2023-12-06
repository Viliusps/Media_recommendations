import { CardActionArea, Card, CardContent, Typography } from '@mui/material';
import styled from 'styled-components';

const StyledCard = styled(Card)`
  text-align: center;
`;

const StyledCardActionArea = styled(CardActionArea)`
  height: 100%;
`;

const StyledImg = styled.img`
  height: 200px;
  width: 200px;
`;

export default function CustomCard({ title, image, handleOpen, cardWidth, cardHeight }) {
  return (
    <StyledCard style={{ width: cardWidth, height: cardHeight }}>
      <StyledCardActionArea
        onClick={() => {
          handleOpen();
        }}>
        <StyledImg src={image} />

        <CardContent>
          <Typography gutterBottom variant="h5" component="div">
            {title}
          </Typography>
        </CardContent>
      </StyledCardActionArea>
    </StyledCard>
  );
}
