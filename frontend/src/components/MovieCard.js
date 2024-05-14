import { useNavigate } from 'react-router-dom';
import { Card, Text, Image, Box, Heading, CardBody } from '@chakra-ui/react';

export default function MovieCard({ movie }) {
  const Navigate = useNavigate();
  return (
    <Card
      height={'100%'}
      transition="transform 0.2s ease-in-out, box-shadow 0.3s ease"
      _hover={{
        transform: 'scale(1.05)',
        boxShadow: '0 0 20px rgba(0,0,0,0.5)'
      }}
      cursor="pointer"
      onClick={() => {
        Navigate(`/movies/${movie.id}`);
      }}>
      <Box width="100%" overflow="hidden">
        <Image src={movie.Poster} alt={`${movie.Title} poster`} width="100%" objectFit="cover" />
      </Box>
      <CardBody>
        <Heading as="h4" size="md">
          {movie.Title}
        </Heading>
        <Text variant="body2" color="text.secondary">
          {movie.Plot?.length > 100 ? movie.Plot.slice(0, 100) + '...' : movie.Plot}
        </Text>
      </CardBody>
    </Card>
  );
}
