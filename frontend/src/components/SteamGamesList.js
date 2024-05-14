import { Grid, Image, Text, GridItem } from '@chakra-ui/react';
import styled from 'styled-components';

const GameCard = styled('div')`
  background-color: #f0f0f0;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  text-align: center;
  transition: transform 0.2s;
  height: 100px;
  width: 150px;
  &:hover {
    transform: scale(1.05);
  }
`;

export default function SteamGamesList({ games }) {
  return (
    <Grid templateColumns={`repeat(${games.length}, 1fr)`} gap={6} marginTop={5} width={900}>
      {games.map((game, index) => (
        <GridItem key={index}>
          <GameCard>
            <Image
              src={
                game.appid
                  ? `https://steamcdn-a.akamaihd.net/steam/apps/${game.appid}/header.jpg`
                  : game.backgroundImage
              }
              alt={game.name}
            />
            <Text>{game.name}</Text>
          </GameCard>
        </GridItem>
      ))}
    </Grid>
  );
}
