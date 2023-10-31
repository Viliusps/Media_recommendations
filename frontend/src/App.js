import './App.css';
import { Navigate, Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import Movies from './pages/Movies';
import Songs from './pages/Songs';
import Login from './pages/Login';

function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/movies" element={<Movies />} />
          <Route path="/songs" element={<Songs />} />
          <Route path="/login" element={<Login />} />
          <Route path="*" element={<Navigate to="/movies" replace />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
