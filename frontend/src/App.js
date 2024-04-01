import './App.css';
import { Navigate, Route, Routes } from 'react-router-dom';
import { useEffect, useState } from 'react';
import Movies from './pages/Movies';
import Movie from './pages/Movie';
import Songs from './pages/Songs';
import Games from './pages/Games';
import Login from './pages/Login';
import Register from './pages/Register';
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

function App() {
  const [role, setRole] = useState('');
  const location = useLocation();

  useEffect(() => {
    getRole().then((data) => {
      setRole(data);
    });
  }, [location.pathname]);

  if (!role) return null;
  else
    return (
      <div className="App">
        <Navbar />
        <ToastContainer />
        <Routes>
          <Route path="/recommendation" element={<Recommendation />} />
          <Route path="/movies" element={<Movies />} />
          <Route path="/movies/:id" element={<Movie />} />
          <Route path="/songs" element={<Songs />} />
          <Route path="/games" element={<Games />} />

          <Route
            path="/recommendationResults/:recommendingType/:recommendingBy/:recommendingByType"
            element={<RecommendationResults />}
          />

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
              <Route path="/playlistRecommendation/:type" element={<RecentlyPlayedSongs />} />
              <Route path="/games/:type" element={<RecentlyPlayedGames />} />
            </>
          )}

          <Route path="*" element={<Navigate to="/movies" replace />} />
        </Routes>
      </div>
    );
}

export default App;
