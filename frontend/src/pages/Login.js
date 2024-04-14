import * as React from 'react';
import { login } from '../api/auth-axios';
import { useState, useRef } from 'react';
import styled from 'styled-components';
import {
  FormLabel,
  Button,
  Flex,
  Stack,
  Heading,
  Text,
  useColorModeValue,
  Box,
  FormControl,
  Input,
  Link
} from '@chakra-ui/react';
import { useNavigate } from 'react-router-dom';

const StyledText = styled(Text)`
  color: red;
`;

export default function SignIn() {
  const [incorrect, setIncorrect] = useState(false);
  const formRef = useRef(null);
  const Navigate = useNavigate();

  const handleSubmit = (event) => {
    event.preventDefault();
    const formData = new FormData(formRef.current);
    login(formData.get('username'), formData.get('password'))
      .then(() => {
        localStorage.setItem('userName', formData.get('username'));
        setIncorrect(false);
        window.location.href = '/movies';
      })
      .catch(() => {
        setIncorrect(true);
      })
      .catch((error) => {
        if (error.response && error.response.status === 403) {
          console.error('Wrong login data:', error);
        } else {
          console.error('An error occurred:', error);
        }
      });
  };

  return (
    <Flex>
      <Stack spacing={8} mx={'auto'} maxW={'lg'} py={12} px={6}>
        <Stack align={'center'}>
          <Heading fontSize={'4xl'}>Sign in to your account</Heading>
        </Stack>
        <Box rounded={'lg'} bg={useColorModeValue('white', 'gray.700')} boxShadow={'lg'} p={8}>
          <form onSubmit={handleSubmit} ref={formRef}>
            <Stack spacing={4}>
              <FormControl isRequired>
                <FormLabel>Username</FormLabel>
                <Input type="username" id="username" name="username" />
              </FormControl>
              <FormControl isRequired>
                <FormLabel>Password</FormLabel>
                <Input type="password" id="password" name="password" />
              </FormControl>
              {incorrect && <StyledText>Incorrect username or password.</StyledText>}
              <Stack spacing={10}>
                <Button
                  type="submit"
                  px={8}
                  bg={useColorModeValue('#151f21', 'gray.900')}
                  color={'white'}
                  rounded={'md'}
                  _hover={{
                    transform: 'translateY(-2px)',
                    boxShadow: 'lg'
                  }}>
                  Sign in
                </Button>
              </Stack>
              <Stack pt={6}>
                <Text align={'center'}>
                  Don&apos;t have an account yet?{' '}
                  <Link color={'blue.400'} onClick={() => Navigate('/register')}>
                    Sign up
                  </Link>
                </Text>
              </Stack>
            </Stack>
          </form>
        </Box>
      </Stack>
    </Flex>
  );
}
