import { useNavigate } from 'react-router-dom';
import { Card, CardBody, Image, Heading, Box } from '@chakra-ui/react';

export default function GameCard({ game }) {
  const Navigate = useNavigate();
  return (
    <Card
      height={200}
      transition="transform 0.2s ease-in-out, box-shadow 0.3s ease"
      _hover={{
        transform: 'scale(1.05)',
        boxShadow: '0 0 20px rgba(0,0,0,0.5)'
      }}
      cursor={'pointer'}
      onClick={() => {
        Navigate(`/games/${game.id}`);
      }}>
      <Box width="100%" height="225px" overflow="hidden">
        <Image src={game.backgroundImage} width="100%" objectFit="cover" />
      </Box>
      <CardBody>
        <Heading as="h4" size="md">
          {game.name}
        </Heading>
      </CardBody>
    </Card>
  );
}
