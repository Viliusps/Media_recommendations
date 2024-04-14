import { useColorModeValue, Center, Box, Image, Stack, Heading } from '@chakra-ui/react';
import { useState } from 'react';

export default function CustomCard({ title, image, handleOpen, cardWidth, cardHeight, zIndex }) {
  const [isHovered, setIsHovered] = useState(false);
  return (
    <Center py={12}>
      <Box
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
        zIndex={isHovered ? zIndex : 0}
        onClick={() => handleOpen()}
        role={'group'}
        p={6}
        w={'full'}
        bg={useColorModeValue('white', 'gray.800')}
        boxShadow={'2xl'}
        rounded={'lg'}
        pos={'relative'}
        height={cardHeight}
        width={cardWidth}
        cursor={'pointer'}>
        <Stack pt={10} align={'center'}>
          <Box rounded={'lg'} mt={-12} pos={'relative'} height={'230px'}>
            <Center height="full">
              <Image
                rounded={'lg'}
                height={160}
                width={160}
                objectFit={'cover'}
                src={image}
                alt="#"
                _groupHover={{
                  filter: 'blur(5px)'
                }}
                transition="filter 0.3s ease-in-out"
              />
            </Center>
          </Box>

          <Heading fontSize={'2xl'} fontFamily={'body'} fontWeight={500}>
            {title}
          </Heading>
        </Stack>
      </Box>
    </Center>
  );
}
