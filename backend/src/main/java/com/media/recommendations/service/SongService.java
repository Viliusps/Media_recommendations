package com.media.recommendations.service;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.media.recommendations.model.Song;
import com.media.recommendations.model.responses.SongPageResponse;
import com.media.recommendations.model.responses.SpotifyAccessTokenResponse;
import com.media.recommendations.repository.SongRepository;

@Service
public class SongService {
    @Value("${spotify.api.tokenUrl}")
    private String spotifyTokenUrl;

    @Value("${spotify.api.url}")
    private String spotifyUrl;

    @Value("${spotify.api.clientId}")
    private String spotifyClientId;

    @Value("${spotify.api.clientSecret}")
    private String spotifyClientSecret;
    
    private final SongRepository songRepository;

    public SongService(@Value("${spotify.api.tokenUrl}") String spotifyTokenUrl, @Value("${spotify.api.url}") String spotifyUrl,
        @Value("${spotify.api.clientId}") String spotifyClientId, @Value("${spotify.api.clientSecret}") String spotifyClientSecret, SongRepository songRepository) {
        this.spotifyTokenUrl = spotifyTokenUrl;
        this.spotifyUrl = spotifyUrl;
        this.spotifyClientId = spotifyClientId;
        this.spotifyClientSecret = spotifyClientSecret;
        this.songRepository = songRepository;
    }

    public List<Song> getAllSongs() {
        return songRepository.findAllByOrderByIdAsc();
    }

    public SongPageResponse getPageSongs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Song> songsPage = songRepository.findAll(pageable);

        List<Song> songs = songsPage.getContent();
        long totalSongs = songRepository.count();

        return new SongPageResponse(songs, totalSongs);
    }

     public Song getSongById(long id) {
        Optional<Song> optionalSong = songRepository.findById(id);
        if (optionalSong.isPresent()) {
            return optionalSong.get();
        }
        return null;
    }

    public Song createSong(Song song) {
        Song newSong = new Song();
        newSong.setGenre(song.getGenre());
        newSong.setTitle(song.getTitle());
        newSong.setSinger(song.getSinger());
        newSong.setSpotifyId(song.getSpotifyId());
        newSong.setMovies(song.getMovies());
        return songRepository.save(newSong);
    }

    public boolean existsSong(long id) {
        return songRepository.existsById(id);
    }

    public Song updateSong(Long id, Song song) {
        Song songFromDb = songRepository.findById(id).get();
        songFromDb.setGenre(song.getGenre());
        songFromDb.setTitle(song.getTitle());
        songFromDb.setSinger(song.getSinger());
        songFromDb.setSpotifyId(song.getSpotifyId());
        songFromDb.setMovies(song.getMovies());
        return songRepository.save(songFromDb);
    }

    public void deleteSong(Long id) {
        songRepository.deleteById(id);
    }


    public String getAccessToken() {
        String authHeader = "Basic " + getBase64ClientIdAndSecret();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "grant_type=client_credentials";

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<SpotifyAccessTokenResponse> responseEntity =
                new RestTemplate().postForEntity(spotifyTokenUrl, request, SpotifyAccessTokenResponse.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            SpotifyAccessTokenResponse responseBody = responseEntity.getBody();
            if (responseBody != null) {
                return responseBody.getAccessToken();
            }
        }

        return null;
    }

    private String getBase64ClientIdAndSecret() {
        String clientIdAndSecret = spotifyClientId + ":" + spotifyClientSecret;
        return java.util.Base64.getEncoder().encodeToString(clientIdAndSecret.getBytes());
    }


    public List<Song> getUserSongs(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = spotifyUrl + "/v1/me/player/recently-played";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return extractSongData(response.getBody());
    }

    private List<Song> extractSongData(String jsonResponse) {
        List<Song> songs = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);

            JsonNode items = root.path("items");
            for (JsonNode item : items) {
                JsonNode track = item.path("track");
                if (track.has("name")) {
                    String songName = track.get("name").asText();
                    String artistName = track.path("artists").path(0).path("name").asText();

                    Song song = Song.builder()
                            .title(songName)
                            .singer(artistName)
                            .spotifyId(track.get("id").asText())
                            .imageUrl(track.path("album").path("images").path(0).path("url").asText())
                            .isrc(track.path("external_ids").path("isrc").asText(""))
                            .build();
                    songs.add(song);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //getMultipleSongsFeatures(songs);
        //after getting a list with isrc, iterate and find the correct name and author
        getSongFeatures(songs.get(4).getSpotifyId());
        return songs;
    }

    public SongPageResponse search(String search) {
        List<Song> found = songRepository.findByTitleContaining(search);
        SongPageResponse response = new SongPageResponse(found, found.size());
        return response;
    }

    public String getCoverImage(String spotifyId) {
        String accessToken = getAccessToken();
        String apiUrl = spotifyUrl + "/v1/tracks/" + spotifyId;
        String authorizationHeader = "Bearer " + accessToken;

        RestTemplate restTemplate = new RestTemplate();

        // Set authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);

        // Make the request to Spotify API
        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

        // Extract the cover image URL from the response
        Map<String, Object> trackInfo = response.getBody();
        if (trackInfo != null && trackInfo.containsKey("album")) {
            Map<String, Object> albumInfo = (Map<String, Object>) trackInfo.get("album");
            List<Map<String, Object>> images = (List<Map<String, Object>>) albumInfo.get("images");
            if (images != null && !images.isEmpty()) {
                return (String) images.get(0).get("url");
            }
        }

        return null;
    }

    public Boolean checkIfSongExists(String name) {
        String accessToken = getAccessToken();

        if (accessToken != null) {
            String apiUrl = spotifyUrl + "/v1/search";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            String searchQuery = "%" + name + "%";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("q", searchQuery)
                    .queryParam("type", "track")
                    .queryParam("limit", 1);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = new RestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("tracks")) {
                Map<String, Object> tracks = (Map<String, Object>) responseBody.get("tracks");
                Integer total = (Integer) tracks.get("total");

                return total > 0;
            }
        }

        return false;
    }

    public String getSongIdByName(String songName) {
        String accessToken = getAccessToken();

        if (accessToken != null) {
            String apiUrl = spotifyUrl + "/v1/search";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            String searchQuery = "%" + songName + "%";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("q", searchQuery)
                    .queryParam("type", "track")
                    .queryParam("limit", 1);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = new RestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("tracks")) {
                Map<String, Object> tracks = (Map<String, Object>) responseBody.get("tracks");
                JsonNode items = new ObjectMapper().convertValue(tracks.get("items"), JsonNode.class);

                if (items.isArray() && items.size() > 0) {
                    JsonNode firstItem = items.get(0);
                    if (firstItem.has("id")) {
                        return firstItem.get("id").asText();
                    }
                }
            }
        }

        return null;
    }

    public String getISRCBySongName(String songName) {
        String spotifyId = getSongIdByName(songName);

        if (spotifyId != null) {
            return getISRCByTrackId(spotifyId);
        }

        return null;
    }

    public void getSongFeatures(String trackId) {
        String isrc = getISRCByTrackId(trackId);
        System.out.println(isrc);
        if(isrc != null) {
            String mbid = getMBIDByISRC(isrc);
            System.out.println(mbid);
            if(mbid != null) {
                String features = getSongFeaturesByMBID(mbid);
                System.out.println(features);
            }
        }
    }

    public void getMultipleSongsFeatures(List<Song> songs) {

        String joinedMBIDS = "";
        for(Song song : songs) {
            String mbid = getMBIDByISRC(song.getIsrc());
            if(mbid != null) joinedMBIDS += mbid + ";";
        }

        if(joinedMBIDS != null) {
            String features = getSongFeaturesByMBID(joinedMBIDS);
            System.out.println(features);
        }

    }

    public String getSongFeaturesByMBID(String mbid) {
        String acousticBrainzUrl = "https://acousticbrainz.org/api/v1/high-level";
        RestTemplate restTemplate = new RestTemplate();

        // Construct the URL with the recording_ids parameter
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(acousticBrainzUrl)
                .queryParam("recording_ids", mbid);

        // Make the API request
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                String.class
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            return null;
        }
    }

    private String getISRCByTrackId(String trackId) {
        String apiUrl = spotifyUrl + "/v1/tracks/" + trackId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = new RestTemplate().exchange(apiUrl, HttpMethod.GET, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, String> externalIds = (Map<String, String>) response.getBody().get("external_ids");
            return externalIds.get("isrc");
        } else {
            // Handle error cases
            return null;
        }
    }

    public String getMBIDByISRC(String isrc) {
        String musicBrainzUrl = "http://musicbrainz.org/ws/2/recording/";

        // Construct the URL with the ISRC parameter
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(musicBrainzUrl)
                .queryParam("query", "isrc:" + isrc)
                .queryParam("fmt", "json");

        // Set up headers or any other configuration if needed
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT, "MyUniProject/0.0.1 (viliusps@gmail.com)");
        // Create a RequestEntity with headers
        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, builder.build().toUri());

        // Make the API request
        ResponseEntity<Map> responseEntity = new RestTemplate().exchange(requestEntity, Map.class);

        // Extract the MBID from the response
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            List<Map<String, String>> recordings = (List<Map<String, String>>) responseEntity.getBody().get("recordings");

            if (!recordings.isEmpty()) {
                // Assuming you want the MBID of the first recording in the list
                return recordings.get(0).get("id");
            }
        } else {
            // Handle error cases
            // You might want to log or throw an exception depending on your use case
            System.err.println("MusicBrainz API request failed with status code: " + responseEntity.getStatusCodeValue());
        }

        return null;
    }

}
