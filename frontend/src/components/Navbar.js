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
  MenuItem
} from '@mui/material';
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
  });

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
          <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
            <IconButton
              size="large"
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleOpenNavMenu}
              color="inherit"></IconButton>
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
              <MenuItem onClick={() => Navigate('/recommendation')}>
                <Typography textAlign="center">Recommendation</Typography>
              </MenuItem>
              <MenuItem onClick={() => Navigate('/movies')}>
                <Typography textAlign="center">Movies</Typography>
              </MenuItem>
              <MenuItem onClick={() => Navigate('/songs')}>
                <Typography textAlign="center">Songs</Typography>
              </MenuItem>
            </Menu>
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
            {role === 'GUEST' && (
              <>
                <Button
                  onClick={() => Navigate('/login')}
                  sx={{ my: 2, color: 'white', display: 'block' }}>
                  Login
                </Button>
                <Button
                  onClick={() => Navigate('/spotify')}
                  sx={{ my: 2, color: 'white', display: 'block' }}>
                  Spotify
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
                  <Avatar alt="Remy Sharp" src="/static/images/avatar/2.jpg" />
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
