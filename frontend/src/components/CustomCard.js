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
        maxW={'330px'}
        w={'full'}
        bg={useColorModeValue('white', 'gray.800')}
        boxShadow={'2xl'}
        rounded={'lg'}
        pos={'relative'}
        height={cardHeight}
        width={cardWidth}>
        <Box
          rounded={'lg'}
          mt={-12}
          pos={'relative'}
          height={'230px'}
          _after={{
            transition: 'all .3s ease',
            content: '""',
            w: 'full',
            h: 'full',
            pos: 'absolute',
            top: 3,
            left: 0,
            backgroundImage: `url(${image})`,
            filter: 'blur(15px)',
            zIndex: -1
          }}
          _groupHover={{
            _after: {
              filter: 'blur(20px)'
            }
          }}>
          <Image
            rounded={'lg'}
            height={155}
            width={145}
            objectFit={'cover'}
            src={image}
            alt="#"
            marginTop={30}
          />
        </Box>
        <Stack pt={10} align={'center'}>
          <Heading fontSize={'2xl'} fontFamily={'body'} fontWeight={500}>
            {title}
          </Heading>
        </Stack>
      </Box>
    </Center>
  );
}
