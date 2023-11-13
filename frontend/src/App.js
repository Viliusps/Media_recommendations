import './App.css';
import { Navigate, Route, Routes, useLocation } from 'react-router-dom';
import { useEffect, useState } from 'react';
import Movies from './pages/Movies';
import Movie from './pages/Movie';
import Songs from './pages/Songs';
import Login from './pages/Login';
import Navbar from './components/Navbar';
import { getRole } from './api/auth-axios';

function App() {
  const [role, setRole] = useState(null);
  const location = useLocation();

  useEffect(() => {
    getRole().then((data) => {
      setRole(data);
    });
  }, [location.pathname]);

  if (role === null) {
    return <div>Loading...</div>;
  }

  return (
    <div className="App">
      <Navbar />
      <Routes>
        <Route path="/movies" element={<Movies />} />
        <Route path="/movies/:id" element={<Movie />} />
        <Route path="/songs" element={<Songs />} />

        {role === 'GUEST' && <Route path="/login" element={<Login />} />}

        <Route path="*" element={<Navigate to="/movies" replace />} />
      </Routes>
    </div>
  );
}

export default App;
