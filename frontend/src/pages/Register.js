import { useNavigate } from 'react-router-dom';
import { register } from '../api/auth-axios';
import { useState, useRef } from 'react';
import {
  Flex,
  Box,
  FormControl,
  FormLabel,
  Input,
  InputGroup,
  HStack,
  InputRightElement,
  Stack,
  Button,
  Heading,
  Text,
  useColorModeValue,
  Link
} from '@chakra-ui/react';
import { ViewIcon, ViewOffIcon } from '@chakra-ui/icons';
import styled from 'styled-components';
import { toast } from 'react-toastify';

const StyledText = styled(Text)`
  color: red;
`;

export default function SignUp() {
  const Navigate = useNavigate();
  const [error, setError] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [showRepeatPassword, setShowRepeatPassword] = useState(false);
  const formRef = useRef(null);

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(formRef.current);

    if (data.get('username') == '' || data.get('email') == '' || data.get('password') == '')
      setError('All fields are required.');
    else if (data.get('password') !== data.get('repeatPassword'))
      setError('Passwords do not match.');
    else if (!/\S+@\S+\.\S+/.test(data.get('email'))) setError('Incorrect email format.');
    else {
      setError('');
      register(data.get('username'), data.get('email'), data.get('password')).then((data) => {
        if (data) {
          showToastMessage();
          Navigate('/login');
        } else setError('User already registerd.');
      });
    }
  };

  const showToastMessage = () => {
    toast.success('Account created successfully! Please sign in.', {
      position: 'top-center',
      autoClose: 1500,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: false,
      draggable: false,
      progress: undefined,
      theme: 'light'
    });
  };

  return (
    <Flex>
      <Stack spacing={8} mx={'auto'} maxW={'lg'} py={12} px={6}>
        <Stack align={'center'}>
          <Heading fontSize={'4xl'} textAlign={'center'}>
            Sign up
          </Heading>
        </Stack>
        <Box rounded={'lg'} bg={useColorModeValue('white', 'gray.700')} boxShadow={'lg'} p={8}>
          <form onSubmit={handleSubmit} ref={formRef}>
            <Stack spacing={4}>
              <Box>
                <FormControl isRequired>
                  <FormLabel>Username</FormLabel>
                  <Input type="text" id="username" name="username" />
                </FormControl>
              </Box>
              <Box>
                <FormControl isRequired>
                  <FormLabel>Email</FormLabel>
                  <Input type="email" id="email" name="email" />
                </FormControl>
              </Box>
              <HStack>
                <FormControl isRequired>
                  <FormLabel>Password</FormLabel>
                  <InputGroup>
                    <Input
                      type={showPassword ? 'text' : 'password'}
                      id="password"
                      name="password"
                    />
                    <InputRightElement h={'full'}>
                      <Button
                        variant={'ghost'}
                        onClick={() => setShowPassword((showPassword) => !showPassword)}>
                        {showPassword ? <ViewIcon /> : <ViewOffIcon />}
                      </Button>
                    </InputRightElement>
                  </InputGroup>
                </FormControl>
                <FormControl isRequired>
                  <FormLabel>Repeat password</FormLabel>
                  <InputGroup>
                    <Input
                      type={showRepeatPassword ? 'text' : 'password'}
                      id="repeatPassword"
                      name="repeatPassword"
                    />
                    <InputRightElement h={'full'}>
                      <Button
                        variant={'ghost'}
                        onClick={() =>
                          setShowRepeatPassword((showRepeatPassword) => !showRepeatPassword)
                        }>
                        {showRepeatPassword ? <ViewIcon /> : <ViewOffIcon />}
                      </Button>
                    </InputRightElement>
                  </InputGroup>
                </FormControl>
              </HStack>
              <Stack spacing={10} pt={2}>
                <StyledText>{error}</StyledText>
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
                  Sign up
                </Button>
              </Stack>
              <Stack pt={6}>
                <Text align={'center'}>
                  Already a user?{' '}
                  <Link color={'blue.400'} onClick={() => Navigate('/login')}>
                    Login
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
