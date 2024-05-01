// package com.media.recommendations.service;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.*;
// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.*;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.media.recommendations.model.Game;
// import com.media.recommendations.model.Movie;
// import com.media.recommendations.model.Recommendation;
// import com.media.recommendations.model.Song;
// import com.media.recommendations.model.User;
// import com.media.recommendations.model.requests.RecommendationRatingRequest;
// import com.media.recommendations.repository.RecommendationRepository;

// public class RecommendationServiceTest {

//     @Mock private MovieService movieService;
//     @Mock private SongService songService;
//     @Mock private GameService gameService;
//     @Mock private UserService userService;
//     @Mock private RecommendationRepository recommendationRepository;

//     @InjectMocks private RecommendationService recommendationService;

//     private RecommendationRatingRequest request;
//     private ObjectMapper mapper;

//     @SuppressWarnings("deprecation")
//     @BeforeEach
//     public void setUp() {
//         MockitoAnnotations.initMocks(this);
//         mapper = new ObjectMapper();
//         request = new RecommendationRatingRequest();
//         request.setRecommendingType("Movie");
//         request.setRecommendingByType("Song");
//         request.setRating(true);
//         request.setUsername("testUser");
//     }

//     @Test
//     public void testRateNewMovieAndSong() {
//         Movie movie = new Movie();
//         movie.setImdbID("tt123456");
//         Movie movieBy = new Movie();
//         movieBy.setImdbID("tt654321");
//         Song songBy = new Song();
//         songBy.setSpotifyId("USRC17607839");

//         when(movieService.existsMovie(any(Movie.class))).thenReturn(false);
//         when(songService.existsSong(any(Song.class))).thenReturn(true);
//         when(songService.getByISRC("USRC17607839")).thenReturn(songBy);

//         Movie expectedMovie = new Movie();
//         expectedMovie.setImdbID("tt123456");
//         expectedMovie.setId(1l);
//         User expectedUser = new User();
//         expectedUser.setUsername("testUser");

//         when(movieService.createMovie(any(Movie.class))).thenReturn(expectedMovie);
//         when(movieService.getByIMDBId(any(String.class))).thenReturn(expectedMovie);
//         when(userService.userByUsername("testUser")).thenReturn(expectedUser);

//         recommendationService.rateRecommendation(request);

//         verify(recommendationRepository, times(1)).save(any(Recommendation.class));
//     }

//     @Test
//     public void testRateExistingGameAndSong() {
//         Game game = new Game();
//         game.setName("Dark Souls");
//         Song songBy = new Song();
//         songBy.setSpotifyId("USRC17607839");

//         when(gameService.existsGame(any(Game.class))).thenReturn(true);
//         when(songService.existsSong(any(Song.class))).thenReturn(false);
//         when(gameService.getByName("Dark Souls")).thenReturn(game);
//         when(songService.createSong(any(Song.class))).thenReturn(songBy);
//         User expectedUser = new User();
//         expectedUser.setUsername("testUser");
//         when(userService.userByUsername("testUser")).thenReturn(expectedUser);

//         recommendationService.rateRecommendation(request);

//         verify(recommendationRepository, times(1)).save(any(Recommendation.class));
//     }

//     @Test
//     public void testRatingUpdateForExistingRecommendation() {
//         Recommendation existingRecommendation = new Recommendation();
//         existingRecommendation.setRating(1);

//         when(recommendationRepository.getByFirstAndSecondAndFirstTypeAndSecondType(anyLong(), anyLong(), anyString(), anyString())).thenReturn(existingRecommendation);
//         when(recommendationRepository.existsByFirstAndSecondAndFirstTypeAndSecondType(any(Long.class), any(Long.class), any(String.class), any(String.class))).thenReturn(true);

//         recommendationService.rateRecommendation(request);

//         verify(recommendationRepository, times(1)).save(existingRecommendation);
//         assertEquals(2, existingRecommendation.getRating(), "Rating was not updated correctly.");
//     }
// }
