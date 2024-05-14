package com.media.recommendations.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.media.recommendations.model.Game;
import com.media.recommendations.model.responses.SteamHistoryResponse;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    
    @InjectMocks
    private GameService gameService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testCalculateAverage() {
        Game game1 = new Game();
        game1.setName("Game1");
        game1.setGenres("Action");
        game1.setReleaseDate("2020-01-01");
        game1.setRating(8.5);
        game1.setPlaytime(20);

        Game game2 = new Game();
        game2.setName("Game2");
        game2.setGenres("Action");
        game2.setReleaseDate("2021-01-01");
        game2.setRating(7.0);
        game2.setPlaytime(10);

        Game game3 = new Game();
        game3.setName("Game3");
        game3.setGenres("Adventure");
        game3.setReleaseDate("2022-01-01");
        game3.setRating(9.0);
        game3.setPlaytime(15);

        List<Game> games = Arrays.asList(game1, game2, game3);
        SteamHistoryResponse mockResponse = new SteamHistoryResponse();
        mockResponse.setGames(games);
        mockResponse.setDate(LocalDate.now());

        GameService spyGameService = spy(gameService);
        doReturn(mockResponse).when(spyGameService).getSteamHistory("testUser");

        Game actualGame = spyGameService.calculateAverage("testUser");

        Game expectedGame = new Game();
        expectedGame.setGenres("Action");
        expectedGame.setReleaseDate("2021-01-01");
        expectedGame.setRating(8.166666666666666);
        expectedGame.setPlaytime(15);

        assertEquals(expectedGame.getGenres(), actualGame.getGenres());
        assertEquals(expectedGame.getReleaseDate(), actualGame.getReleaseDate());
        assertEquals(expectedGame.getRating(), actualGame.getRating(), 0.01);
        assertEquals(expectedGame.getPlaytime(), actualGame.getPlaytime());
    }
}
