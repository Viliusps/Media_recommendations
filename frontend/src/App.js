import './App.css';
import { Navigate, Route, Routes } from 'react-router-dom';
import { useEffect, useState } from 'react';
import Movies from './pages/Movies';
import Movie from './pages/Movie';
import Songs from './pages/Songs';
import Games from './pages/Games';
import Login from './pages/Login';
import Register from './pages/Register';
import Profile from './pages/Profile';
import Navbar from './components/Navbar';
import { getRole } from './api/auth-axios';
import Recommendation from './pages/Recommendation';
import RecentlyPlayedSongs from './pages/RecentlyPlayedSongs';
import RecommendationResults from './pages/RecommendationResults';
import RecentlyPlayedGames from './pages/RecentlyPlayedGames';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import AdminPanel from './pages/AdminPanel';
import { useLocation } from 'react-router-dom';
import Game from './pages/Game';
import Song from './pages/Song';
import { ChakraProvider, extendTheme } from '@chakra-ui/react';
import { ThemeProvider, createTheme, THEME_ID } from '@mui/material';
import { green, purple } from '@mui/material/colors';

function App() {
  const [role, setRole] = useState('');
  const location = useLocation();

  const chakraTheme = extendTheme();
  const materialTheme = createTheme({
    palette: {
      primary: {
        main: purple[500]
      },
      secondary: {
        main: green[500]
      }
    }
  });

  useEffect(() => {
    getRole().then((data) => {
      setRole(data);
    });
  }, [location.pathname]);

  if (!role) return null;
  else
    return (
      <ChakraProvider theme={chakraTheme}>
        <ThemeProvider theme={{ [THEME_ID]: materialTheme }}>
          <div className="App">
            <ToastContainer />
            <Navbar>
              <Routes>
                <Route path="/movies" element={<Movies />} />
                <Route path="/movies/:id" element={<Movie />} />
                <Route path="/games/:id" element={<Game />} />
                <Route path="/songs" element={<Songs />} />
                <Route path="/songs/:id" element={<Song />} />
                <Route path="/games" element={<Games />} />

                {role === 'GUEST' && (
                  <>
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                  </>
                )}

                {role === 'ADMIN' && (
                  <>
                    <Route path="/admin" element={<AdminPanel />} />
                  </>
                )}

                {role !== 'GUEST' && (
                  <>
                    <Route path="/recommendation" element={<Recommendation />} />

                    <Route
                      path="/recommendationResults/:recommendingType/:recommendingBy/:recommendingByType/:recommendingByID"
                      element={<RecommendationResults />}
                    />
                    <Route path="/playlistRecommendation/:type" element={<RecentlyPlayedSongs />} />
                    <Route path="/gamesPlaylist/:type" element={<RecentlyPlayedGames />} />
                    <Route path="/profile" element={<Profile />} />
                  </>
                )}

                <Route path="*" element={<Navigate to="/movies" replace />} />
              </Routes>
            </Navbar>
          </div>
        </ThemeProvider>
      </ChakraProvider>
    );
}

export default App;
