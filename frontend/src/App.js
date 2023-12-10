import './App.css';
import { Navigate, Route, Routes, useLocation } from 'react-router-dom';
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

function App() {
  const [role, setRole] = useState(null);
  const location = useLocation();

  useEffect(() => {
    getRole().then((data) => {
      setRole(data);
    });
  }, [location.pathname]);

  return (
    <div className="App">
      <Navbar />
      <ToastContainer />
      <Routes>
        <Route path="/recommendation" element={<Recommendation />} />
        <Route path="/movies" element={<Movies />} />
        <Route path="/movies/:id" element={<Movie />} />
        <Route path="/songs" element={<Songs />} />
        <Route path="/playlistRecommendation" element={<RecommendationFromPlaylist />} />
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

        <Route path="*" element={<Navigate to="/movies" replace />} />
      </Routes>
    </div>
  );
}

export default App;
