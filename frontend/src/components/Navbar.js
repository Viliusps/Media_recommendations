import {
  AppBar,
  Box,
  Toolbar,
  IconButton,
  Typography,
  Menu,
  Container,
  Avatar,
  Button,
  Tooltip,
  MenuItem,
  Hidden
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getRole } from '../api/auth-axios';

function Navbar() {
  const Navigate = useNavigate();
  const [anchorElNav, setAnchorElNav] = useState(null);
  const [anchorElUser, setAnchorElUser] = useState(null);
  const [role, setRole] = useState(null);

  useEffect(() => {
    getRole().then((data) => {
      setRole(data);
    });
  }, []);

  const handleOpenNavMenu = (event) => {
    setAnchorElNav(event.currentTarget);
  };
  const handleOpenUserMenu = (event) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseNavMenu = () => {
    setAnchorElNav(null);
  };

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  const logout = () => {
    localStorage.clear();
    window.location.href = '/movies';
  };

  return (
    <AppBar position="static">
      <Container maxWidth="xl">
        <Toolbar disableGutters>
          <Hidden mdUp>
            <IconButton size="large" aria-label="menu" color="inherit" onClick={handleOpenNavMenu}>
              <MenuIcon />
            </IconButton>
          </Hidden>
          <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
            {role === 'GUEST' && (
              <Menu
                id="menu-appbar"
                anchorEl={anchorElNav}
                anchorOrigin={{
                  vertical: 'bottom',
                  horizontal: 'left'
                }}
                keepMounted
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'left'
                }}
                open={Boolean(anchorElNav)}
                onClose={handleCloseNavMenu}
                sx={{
                  display: { xs: 'block', md: 'none' }
                }}>
                <MenuItem onClick={() => Navigate('/login')}>
                  <Typography textAlign="center">Login</Typography>
                </MenuItem>
                <MenuItem onClick={() => Navigate('/register')}>
                  <Typography textAlign="center">Register</Typography>
                </MenuItem>
              </Menu>
            )}
          </Box>
          <Hidden mdUp>
            <Menu
              id="menu-appbar"
              anchorEl={anchorElNav}
              anchorOrigin={{
                vertical: 'top',
                horizontal: 'left'
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'left'
              }}
              open={Boolean(anchorElNav)}
              onClose={handleCloseNavMenu}>
              <MenuItem onClick={() => Navigate('/recommendation')}>
                <Typography textAlign="center">Recommendation</Typography>
              </MenuItem>
              <MenuItem onClick={() => Navigate('/movies')}>
                <Typography textAlign="center">Movies</Typography>
              </MenuItem>
              <MenuItem onClick={() => Navigate('/songs')}>
                <Typography textAlign="center">Songs</Typography>
              </MenuItem>
              <MenuItem onClick={() => Navigate('/games')}>
                <Typography textAlign="center">Games</Typography>
              </MenuItem>
            </Menu>
          </Hidden>
          <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
            <Button
              onClick={() => Navigate('/recommendation')}
              sx={{ my: 2, color: 'white', display: 'block' }}>
              Recommendation
            </Button>
            <Button
              onClick={() => Navigate('/movies')}
              sx={{ my: 2, color: 'white', display: 'block' }}>
              Movies
            </Button>
            <Button
              onClick={() => Navigate('/songs')}
              sx={{ my: 2, color: 'white', display: 'block' }}>
              Songs
            </Button>
            <Button
              onClick={() => Navigate('/games')}
              sx={{ my: 2, color: 'white', display: 'block' }}>
              Games
            </Button>
            {role === 'ADMIN' && (
              <>
                <Button
                  onClick={() => Navigate('/admin')}
                  sx={{ my: 2, color: 'white', display: 'block' }}>
                  Admin
                </Button>
              </>
            )}
            {role === 'GUEST' && (
              <>
                <Button
                  onClick={() => Navigate('/login')}
                  sx={{ my: 2, color: 'white', display: 'block' }}>
                  Login
                </Button>
                <Button
                  onClick={() => Navigate('/register')}
                  sx={{ my: 2, color: 'white', display: 'block' }}>
                  Register
                </Button>
              </>
            )}
          </Box>

          {role !== 'GUEST' && (
            <Box sx={{ flexGrow: 0 }}>
              <Tooltip title="Open settings">
                <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                  <Avatar />
                </IconButton>
              </Tooltip>
              <Menu
                sx={{ mt: '45px' }}
                id="menu-appbar"
                anchorEl={anchorElUser}
                anchorOrigin={{
                  vertical: 'top',
                  horizontal: 'right'
                }}
                keepMounted
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'right'
                }}
                open={Boolean(anchorElUser)}
                onClose={handleCloseUserMenu}>
                <MenuItem
                  onClick={() => {
                    Navigate('/profile');
                    handleCloseUserMenu();
                  }}>
                  <Typography textAlign="center">Profile</Typography>
                </MenuItem>
                <MenuItem onClick={() => logout()}>
                  <Typography textAlign="center">Logout</Typography>
                </MenuItem>
              </Menu>
            </Box>
          )}
        </Toolbar>
      </Container>
    </AppBar>
  );
}
export default Navbar;
