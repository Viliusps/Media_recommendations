import {
  IconButton,
  Avatar,
  Box,
  CloseButton,
  Flex,
  HStack,
  VStack,
  Icon,
  useColorModeValue,
  Text,
  Drawer,
  DrawerContent,
  useDisclosure,
  Menu,
  MenuButton,
  MenuItem,
  MenuList,
  ChakraProvider,
  Stack,
  Button,
  Image
} from '@chakra-ui/react';
import { FiMenu, FiChevronDown, FiMusic } from 'react-icons/fi';
import { useNavigate } from 'react-router-dom';
import { getRole } from '../api/auth-axios';
import { useState, useEffect } from 'react';
import { IoGameControllerOutline } from 'react-icons/io5';
import { BiMovie } from 'react-icons/bi';
import { FaWandMagicSparkles } from 'react-icons/fa6';
import logo from '../images/logo.png';

const LinkItems = [
  { name: 'Movies', icon: BiMovie, link: '/movies' },
  { name: 'Songs', icon: FiMusic, link: '/songs' },
  { name: 'Games', icon: IoGameControllerOutline, link: '/games' },
  { name: 'Recommend', icon: FaWandMagicSparkles, link: '/recommendation' }
];

const SidebarContent = ({ onClose, ...rest }) => {
  const Navigate = useNavigate();
  return (
    <Box
      transition="3s ease"
      bg={useColorModeValue('white', 'gray.900')}
      borderRight="1px"
      borderRightColor={useColorModeValue('gray.200', 'gray.700')}
      w={{ base: 'full', md: 60 }}
      pos="fixed"
      h="full"
      {...rest}>
      <Flex h="20" alignItems="center" mx="8" justifyContent="space-between">
        <Image src={logo} boxSize="110px" />
        <CloseButton display={{ base: 'flex', md: 'none' }} onClick={onClose} />
      </Flex>
      {LinkItems.map((link) => (
        <NavItem
          key={link.name}
          icon={link.icon}
          onClick={() => {
            Navigate(link.link);
          }}>
          {link.name}
        </NavItem>
      ))}
    </Box>
  );
};

const NavItem = ({ icon, children, ...rest }) => {
  return (
    <Box as="a" href="#" style={{ textDecoration: 'none' }} _focus={{ boxShadow: 'none' }}>
      <Flex
        align="center"
        p="4"
        mx="4"
        borderRadius="lg"
        role="group"
        cursor="pointer"
        _hover={{
          bg: useColorModeValue('#151f21', 'gray.900'),
          color: 'white'
        }}
        {...rest}>
        {icon && (
          <Icon
            mr="4"
            fontSize="16"
            _groupHover={{
              color: 'white'
            }}
            as={icon}
          />
        )}
        {children}
      </Flex>
    </Box>
  );
};

const MobileNav = ({ onOpen, ...rest }) => {
  const Navigate = useNavigate();
  const [role, setRole] = useState(null);
  const [username, setUsername] = useState('');

  useEffect(() => {
    getRole().then((data) => {
      setRole(data);
      setUsername(localStorage.getItem('userName'));
    });
  }, []);

  const logout = () => {
    localStorage.clear();
    window.location.href = '/movies';
  };
  return (
    <Flex
      ml={{ base: 0, md: 60 }}
      px={{ base: 4, md: 4 }}
      height="20"
      alignItems="center"
      bg={useColorModeValue('white', 'gray.900')}
      borderBottomWidth="1px"
      borderBottomColor={useColorModeValue('gray.200', 'gray.700')}
      justifyContent={{ base: 'space-between', md: 'flex-end' }}
      {...rest}>
      <IconButton
        display={{ base: 'flex', md: 'none' }}
        onClick={onOpen}
        variant="outline"
        aria-label="open menu"
        icon={<FiMenu />}
      />

      <Image boxSize="100px" display={{ base: 'flex', md: 'none' }} src={logo} />
      {role == 'GUEST' && (
        <Stack flex={{ base: 1, md: 0 }} justify={'flex-end'} direction={'row'} spacing={6}>
          <Button
            fontSize={'sm'}
            fontWeight={500}
            variant={'link'}
            color={'black'}
            onClick={() => Navigate('/login')}>
            Sign In
          </Button>
          <Button
            px={8}
            fontSize={'sm'}
            fontWeight={600}
            bg={useColorModeValue('#151f21', 'gray.900')}
            color={'white'}
            rounded={'md'}
            _hover={{
              transform: 'translateY(-2px)',
              boxShadow: 'lg'
            }}
            onClick={() => Navigate('/register')}>
            Sign Up
          </Button>
        </Stack>
      )}

      {role != 'GUEST' && (
        <HStack spacing={{ base: '0', md: '6' }}>
          <Flex alignItems={'center'}>
            <Menu>
              <MenuButton py={2} transition="all 0.3s" _focus={{ boxShadow: 'none' }}>
                <HStack>
                  <Avatar size={'sm'} />
                  <VStack
                    display={{ base: 'none', md: 'flex' }}
                    alignItems="flex-start"
                    spacing="1px"
                    ml="2">
                    <Text fontSize="sm">{username}</Text>
                  </VStack>
                  <Box display={{ base: 'none', md: 'flex' }}>
                    <FiChevronDown />
                  </Box>
                </HStack>
              </MenuButton>
              <MenuList
                bg={useColorModeValue('white', 'gray.900')}
                borderColor={useColorModeValue('gray.200', 'gray.700')}>
                <MenuItem onClick={() => Navigate('/profile')}>Profile</MenuItem>
                <MenuItem onClick={() => logout()}>Sign out</MenuItem>
              </MenuList>
            </Menu>
          </Flex>
        </HStack>
      )}
    </Flex>
  );
};

const Navbar = ({ children }) => {
  const { isOpen, onOpen, onClose } = useDisclosure();

  return (
    <ChakraProvider>
      <Box minH="100vh" bg={useColorModeValue('gray.100', 'gray.900')}>
        <SidebarContent onClose={() => onClose} display={{ base: 'none', md: 'block' }} />
        <Drawer
          isOpen={isOpen}
          placement="left"
          onClose={onClose}
          returnFocusOnClose={false}
          onOverlayClick={onClose}
          size="full">
          <DrawerContent>
            <SidebarContent onClose={onClose} />
          </DrawerContent>
        </Drawer>
        {/* mobilenav */}
        <MobileNav onOpen={onOpen} />
        <Box ml={{ base: 0, md: 60 }} p="4">
          {children}
        </Box>
      </Box>
    </ChakraProvider>
  );
};

export default Navbar;
