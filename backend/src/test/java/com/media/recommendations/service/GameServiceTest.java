package com.media.recommendations.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.media.recommendations.model.Game;
import com.media.recommendations.model.SteamHistory;
import com.media.recommendations.model.User;
import com.media.recommendations.repository.GameRepository;
import com.media.recommendations.repository.SteamRepository;
import com.media.recommendations.model.responses.GamePageResponse;
import com.media.recommendations.model.responses.SteamHistoryResponse;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserService userService;

    @Mock
    private SteamRepository steamRepository;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPageGames() {
        Page<Game> page = new PageImpl<>(Arrays.asList(new Game(), new Game()));
        when(gameRepository.findAll(any(PageRequest.class))).thenReturn(page);
        
        GamePageResponse response = gameService.getPageGames(1, 10);
        
        assertNotNull(response);
        assertEquals(2, response.getGames().size());
    }

    @Test
    public void testSearch() {
        List<Game> foundGames = Arrays.asList(new Game(), new Game());
        when(gameRepository.findByNameContaining(anyString())).thenReturn(foundGames);
        
        GamePageResponse response = gameService.search("test");
        
        assertNotNull(response);
        assertEquals(2, response.getGames().size());
        assertEquals(2, response.getTotalGames());
    }

    @Test
    public void testExistsGameByName() {
        when(gameRepository.existsByName(anyString())).thenReturn(true);
        Game testGame = new Game();
        testGame.setName("test");
        
        boolean exists = gameService.existsGame(testGame);
        
        assertTrue(exists);
    }

    @Test
    public void testGetByName() {
        Game game = new Game();
        game.setName("Test Game");
        when(gameRepository.getByName(anyString())).thenReturn(game);
        
        Game foundGame = gameService.getByName("Test Game");
        
        assertNotNull(foundGame);
        assertEquals("Test Game", foundGame.getName());
    }

    @Test
    public void testCreateGame() {
        Game game = new Game();
        game.setName("New Game");
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        
        Game createdGame = gameService.createGame(game);
        
        assertNotNull(createdGame);
        assertEquals("New Game", createdGame.getName());
    }

    @Test
    public void testGetGameById() {
        Game game = new Game();
        game.setId(1L);
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        
        Game foundGame = gameService.getGameById(1L);
        
        assertNotNull(foundGame);
        assertEquals(1L, foundGame.getId());
    }

    @Test
    void testGetSteamHistory() {
        User mockUser = new User();
        mockUser.setId(1L);
        SteamHistory historyEntry = new SteamHistory();
        Game game = new Game();
        historyEntry.setGame(game);
        historyEntry.setDate(LocalDate.now());
        List<SteamHistory> history = Arrays.asList(historyEntry);

        when(userService.userByUsername("testUser")).thenReturn(mockUser);
        when(steamRepository.findAllByUserId(1L)).thenReturn(history);

        SteamHistoryResponse response = gameService.getSteamHistory("testUser");

        assertNotNull(response);
        assertEquals(1, response.getGames().size());
        assertEquals(historyEntry.getDate(), response.getDate());
    }
}