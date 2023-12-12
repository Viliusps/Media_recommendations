import './App.css';
import { Navigate, Route, Routes } from 'react-router-dom';
import { useEffect, useState } from 'react';
import Movies from './pages/Movies';
import Movie from './pages/Movie';
import Songs from './pages/Songs';
import Login from './pages/Login';
import Register from './pages/Register';
import Navbar from './components/Navbar';
import { getRole } from './api/auth-axios';
import Recommendation from './pages/Recommendation';
import RecommendationFromPlaylist from './pages/RecommendationFromPlaylist';
import RecommendationFromChoice from './pages/RecommendationFromChoice';
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

          <Route
            path="/choiceRecommendation/:recommendingType/:recommendingBy/:recommendingByType"
            element={<RecommendationFromChoice />}
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
              <Route
                path="/playlistRecommendation/:type"
                element={<RecommendationFromPlaylist />}
              />
            </>
          )}

          <Route path="*" element={<Navigate to="/movies" replace />} />
        </Routes>
      </div>
    );
}

export default App;
