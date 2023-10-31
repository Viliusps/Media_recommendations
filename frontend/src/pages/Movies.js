import { useEffect, useState } from 'react';
import { getMovies } from '../api/movies-axios';
import { useNavigate } from 'react-router-dom';

export default function Movies() {
  const [movies, setMovies] = useState([]);
  const Navigate = useNavigate();

  useEffect(() => {
    getMovies()
      .then((data) => {
        setMovies(data);
      })
      .catch((error) => {
        if (error.response && error.response.status === 403) {
          Navigate('/login');
        } else {
          console.error('An error occurred:', error);
        }
      });
  }, []);

  return (
    <div>
      <h1>Movies page</h1>
      {movies.map((movie) => (
        <h3 key={movie.id}>
          {movie.id}: {movie.originalTitle}
        </h3>
      ))}
    </div>
  );
}
