import React, { useEffect, useState } from 'react';
import { GetRecentlyPlayedGames } from '../api/games-axios';

const SteamLogin = () => {
  const [recentGames, setRecentGames] = useState([]);

  useEffect(() => {
    getGames();
  }, []);

  const getGames = () => {
    GetRecentlyPlayedGames()
      .then((result) => {
        setRecentGames(result.response.games);
        console.log(result);
      })
      .catch((error) => {
        console.error('Error fetching recently played games:', error);
      });
  };

  return (
    <div>
      <h1>Steam Login</h1>
      {recentGames.length > 0 ? (
        <div>
          <h3>Recently Played Games:</h3>
          <ul>
            {recentGames.map((game) => (
              <li key={game.appid}>{game.name}</li>
            ))}
          </ul>
        </div>
      ) : (
        <button onClick={getGames}>Get games</button>
      )}
    </div>
  );
};

export default SteamLogin;
