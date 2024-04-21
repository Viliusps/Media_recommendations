package com.media.recommendations.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.media.recommendations.model.Song;
import com.media.recommendations.model.SpotifyHistory;
import com.media.recommendations.model.User;
import com.media.recommendations.model.responses.SongArtistName;
import com.media.recommendations.model.responses.SongPageResponse;
import com.media.recommendations.model.responses.SpotifyAccessTokenResponse;
import com.media.recommendations.model.responses.SpotifyHistoryResponse;
import com.media.recommendations.repository.SongRepository;
import com.media.recommendations.repository.SpotifyRepository;

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

    private final SpotifyRepository spotifyRepository;

    private final UserService userService;

    public SongService(@Value("${spotify.api.tokenUrl}") String spotifyTokenUrl, @Value("${spotify.api.url}") String spotifyUrl,
        @Value("${spotify.api.clientId}") String spotifyClientId, @Value("${spotify.api.clientSecret}") String spotifyClientSecret, SongRepository songRepository, SpotifyRepository spotifyRepository, UserService userService) {
        this.spotifyTokenUrl = spotifyTokenUrl;
        this.spotifyUrl = spotifyUrl;
        this.spotifyClientId = spotifyClientId;
        this.spotifyClientSecret = spotifyClientSecret;
        this.songRepository = songRepository;
        this.spotifyRepository = spotifyRepository;
        this.userService = userService;
    }

    public List<Song> getAllSongs() {
        return songRepository.findAllByOrderByIdAsc();
    }

    public SongPageResponse getPageSongs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("popularity").descending());
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

    public boolean existsSong(Song song) {
        if (song == null) {
            return false;
        }
        return songRepository.existsByisrc(song.getIsrc());
    }

    public Song getByISRC(String ISRC) {
        return songRepository.getByisrc(ISRC);
    }


    public Song createSong(Song song) {
        Song newSong = new Song();
        newSong.setTitle(song.getTitle());
        newSong.setSinger(song.getSinger());
        newSong.setSpotifyId(song.getSpotifyId());
        newSong.setIsrc(song.getIsrc());
        newSong.setImageUrl(song.getImageUrl());

        newSong.setMfccZeroMean(song.getMfccZeroMean());
        newSong.setDynamicComplexity(song.getDynamicComplexity());
        newSong.setAverageLoudness(song.getAverageLoudness());
        newSong.setOnsetRate(song.getOnsetRate());
        newSong.setBpmHistogramSecondPeakBpmMedian(song.getBpmHistogramSecondPeakBpmMedian());
        newSong.setBpmHistogramSecondPeakBpmMean(song.getBpmHistogramSecondPeakBpmMean());
        newSong.setBpmHistogramFirstPeakBpmMedian(song.getBpmHistogramFirstPeakBpmMedian());
        newSong.setBpmHistogramFirstPeakBpmMean(song.getBpmHistogramFirstPeakBpmMean());
        newSong.setBpm(song.getBpm());
        newSong.setDanceability(song.getDanceability());
        newSong.setTuningFrequency(song.getTuningFrequency());
        newSong.setTuningEqualTemperedDeviation(song.getTuningEqualTemperedDeviation());
        return songRepository.save(newSong);
    }

    public boolean existsSong(long id) {
        return songRepository.existsById(id);
    }

    public Song updateSong(Long id, Song song) {
        Song songFromDb = songRepository.findById(id).get();
        songFromDb.setTitle(song.getTitle());
        songFromDb.setSinger(song.getSinger());
        songFromDb.setSpotifyId(song.getSpotifyId());
        return songRepository.save(songFromDb);
    }

    public void deleteSong(Long id) {
        songRepository.deleteById(id);
    }

    public SpotifyHistoryResponse getSpotifyHistory(String username) {
        User user = userService.userByUsername(username);
        Long userId = user.getId();
        List<SpotifyHistory> history = spotifyRepository.findAllByUserId(userId);
        if(history.size() > 0) {
            List<Song> songs = history.stream()
                .map(SpotifyHistory::getSong)
                .collect(Collectors.toList());
            SpotifyHistoryResponse response = new SpotifyHistoryResponse(songs, history.get(0).getDate());
            return response;
        }
        return new SpotifyHistoryResponse();
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


    public List<Song> getUserSongs(String accessToken, String username) {
        RestTemplate restTemplate = new RestTemplate();
        String url = spotifyUrl + "/v1/me/player/recently-played";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        List<Song> songs = extractSongData(response.getBody());

        addSongsToHistory(songs, username);

        return songs;
    }

    private void addSongsToHistory(List<Song> songs, String username) {
        //Clean previous history
        User user = userService.userByUsername(username);
        spotifyRepository.deleteByUser(user);

        //Add new entries
        LocalDate currDate = LocalDate.now();
        for(Song song : songs) {
            if(!existsSong(song)) {
                song = createSong(song);
            } else {
                song = getByISRC(song.getIsrc());
            }
            SpotifyHistory entry = new SpotifyHistory();
            entry.setDate(currDate);
            entry.setSong(song);
            entry.setUser(user);
            spotifyRepository.save(entry);
        }
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
        
        return songs;
    }

    public Song calculateAverage(String username) {
        Song song = new Song();
        SpotifyHistoryResponse history = getSpotifyHistory(username);
        Map<String, String> features = getMultipleSongsFeatures(history.getSongs());
        song.setMfccZeroMean(features.get("mfccZeroMean"));
        song.setDynamicComplexity(features.get("dynamicComplexity"));
        song.setAverageLoudness(features.get("averageLoudness"));
        song.setOnsetRate(features.get("onsetRate"));
        song.setBpmHistogramSecondPeakBpmMedian(features.get("bpmHistogramSecondPeakBpmMedian"));
        song.setBpmHistogramSecondPeakBpmMean(features.get("bpmHistogramSecondPeakBpmMean"));
        song.setBpmHistogramFirstPeakBpmMedian(features.get("bpmHistogramFirstPeakBpmMedian"));
        song.setBpmHistogramFirstPeakBpmMean(features.get("bpmHistogramFirstPeakBpmMean"));
        song.setBpm(features.get("bpm"));
        song.setDanceability(features.get("danceability"));
        song.setTuningFrequency(features.get("tuningFrequency"));
        song.setTuningEqualTemperedDeviation(features.get("tuningEqualTemperedDeviation"));
        return song;
    }

    
    public Map<String, String> getMultipleSongsFeatures(List<Song> songs) {

        String joinedMBIDS = "";
        for(Song song : songs) {
            String mbid = getMBIDByISRC(song.getIsrc(), song.getTitle());
            if(mbid != null) joinedMBIDS += mbid + ";";
        }
        if(joinedMBIDS != "") {
            Map<String, String> features = getSongFeaturesByMBID(joinedMBIDS);
            return features;
        }
        return null;
    }

    public SongPageResponse search(String search) {
        List<Song> found = songRepository.findByTitleContaining(search);
        SongPageResponse response = new SongPageResponse(found, found.size());
        return response;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String getCoverImage(String spotifyId) {
        String accessToken = getAccessToken();
        String apiUrl = spotifyUrl + "/v1/tracks/" + spotifyId;
        String authorizationHeader = "Bearer " + accessToken;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);

        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

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

    @SuppressWarnings("unchecked")
    private Song parseSpotifyResponse(List<Map<String, Object>> tracks, int index) {
        Map<String, Object> track = tracks.get(index);
        String songName = (String) track.get("name");
        List<Map<String, Object>> artists = (List<Map<String, Object>>) track.get("artists");
        String artist = artists.isEmpty() ? "" : (String) artists.get(0).get("name");
        Map<String, Object> album = (Map<String, Object>) track.get("album");
        String spotifyId = (String) track.get("id");
        List<Map<String, Object>> images = (List<Map<String, Object>>) album.get("images");
        String imageUrl = images.isEmpty() ? "" : (String) images.get(0).get("url");
        Map<String, Object> externalIds = (Map<String, Object>) track.get("external_ids");
        String isrc = externalIds == null ? "" : (String) externalIds.get("isrc");
        
        Song song = Song.builder()
                    .title(songName)
                    .singer(artist)
                    .spotifyId(spotifyId)
                    .imageUrl(imageUrl)
                    .isrc(isrc)
                    .build();
        return song;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Song> getSongSuggestions(String name) {
        System.out.println("Suggestions for song: " + name);
        List<Song> songs = new ArrayList<>();
        String accessToken = getAccessToken();

        if (accessToken != null) {
            String apiUrl = spotifyUrl + "/v1/search";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("q", name)
                    .queryParam("type", "track")
                    .queryParam("limit", 5)
                    .queryParam("market", "US");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            System.out.println(builder.toUriString());
            ResponseEntity<Map> response = new RestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> tracks = (List<Map<String, Object>>) ((Map<String, Object>) responseBody.get("tracks")).get("items");
                if (!tracks.isEmpty()) {
                    for(int i = 0; i < Math.min(tracks.size(), 5); i++) {
                        Song song = parseSpotifyResponse(tracks, i);
                        songs.add(song);
                    }
                }
            }
        }
        return songs;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Song getSongByNameFromSpotify(String name) {
        System.out.println("Searching for this song on spotify: " + name);
        String accessToken = getAccessToken();
        Song song = null;

        if (accessToken != null) {
            String apiUrl = spotifyUrl + "/v1/search";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            String query = "track:" + name;

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("q", query)
                    .queryParam("type", "track")
                    .queryParam("limit", 2);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = new RestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> tracks = (List<Map<String, Object>>) ((Map<String, Object>) responseBody.get("tracks")).get("items");
                if (!tracks.isEmpty()) {
                    song = parseSpotifyResponse(tracks, 0);
                }
            }
        }
        return song;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Song getSongByISRCFromSpotify(String isrc) {
        System.out.println("Getting song by isrc: " + isrc);
        String accessToken = getAccessToken();
        Song song = null;

        if (accessToken != null) {
            String apiUrl = spotifyUrl + "/v1/search";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            String query = "isrc:" + isrc;

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("q", query)
                    .queryParam("type", "track")
                    .queryParam("limit", 1)
                    .queryParam("market", "US");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = new RestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> tracks = (List<Map<String, Object>>) ((Map<String, Object>) responseBody.get("tracks")).get("items");
                if (!tracks.isEmpty()) {
                    song = parseSpotifyResponse(tracks, 0);
                    System.out.println("Got song: " + song.getTitle());
                }
            }
        }
        return song;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
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

    public Song getSongFeatures(Song song) {
        String trackId = song.getSpotifyId();
        String title = song.getTitle();
        String isrc = getISRCByTrackId(trackId);
        if(isrc != null) {
            String mbid = getMBIDByISRC(isrc, title);
            if(mbid != null) {
                Map<String, String> features = getSongFeaturesByMBID(mbid);
                song.setMfccZeroMean(features.get("mfccZeroMean"));
                song.setDynamicComplexity(features.get("dynamicComplexity"));
                song.setAverageLoudness(features.get("averageLoudness"));
                song.setOnsetRate(features.get("onsetRate"));
                song.setBpmHistogramSecondPeakBpmMedian(features.get("bpmHistogramSecondPeakBpmMedian"));
                song.setBpmHistogramSecondPeakBpmMean(features.get("bpmHistogramSecondPeakBpmMean"));
                song.setBpmHistogramFirstPeakBpmMedian(features.get("bpmHistogramFirstPeakBpmMedian"));
                song.setBpmHistogramFirstPeakBpmMean(features.get("bpmHistogramFirstPeakBpmMean"));
                song.setBpm(features.get("bpm"));
                song.setDanceability(features.get("danceability"));
                song.setTuningFrequency(features.get("tuningFrequency"));
                song.setTuningEqualTemperedDeviation(features.get("tuningEqualTemperedDeviation"));
            }
        }
        return song;
    }

    public Map<String, String> getSongFeaturesByMBID(String mbid) {
        String acousticBrainzUrl = "https://acousticbrainz.org/api/v1/low-level?recording_ids=" + mbid;
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                acousticBrainzUrl,
                HttpMethod.GET,
                null,
                String.class
        );

        //Muy importante, no deleto por favor!!!!
        String rateLimitRemaining = responseEntity.getHeaders().getFirst("X-RateLimit-Remaining");
        System.out.println("X-RateLimit-Remaining: " + rateLimitRemaining);
        //----------------------------------------

        try {
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());
                if (!mbid.contains(";") && rootNode.get(mbid) != null) {
                    JsonNode mbidNode = rootNode.get(mbid);
                    if (mbidNode.size() == 1 && mbidNode.has("0")) {
                        mbidNode = mbidNode.get("0");
                    }
                    return extractFeaturesFromNode(mbidNode);
                } else {
                    // If multiple MBIDs are provided, calculate average features
                    return calculateAverageFeatures(rootNode);
                }
            } else {
                return null;
            }
        } catch (JsonProcessingException e) {
            System.err.println("Error processing JSON response: " + e.getMessage());
            return null;
        }
        
    }

    private Map<String, String> calculateAverageFeatures(JsonNode rootNode) {
        Map<String, Double> sumFeatures = new HashMap<>();
        Map<String, Integer> countFeatures = new HashMap<>();

        // Iterate over each MBID node
        Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            if (entry.getKey().equals("mbid_mapping")) {
                continue;
            }
            JsonNode mbidNode = entry.getValue();
            if (mbidNode.size() == 1 && mbidNode.has("0")) {
                mbidNode = mbidNode.get("0");
            }

            Map<String, String> features = extractFeaturesFromNode(mbidNode);

            // Accumulate feature values
            for (Map.Entry<String, String> featureEntry : features.entrySet()) {
                String featureName = featureEntry.getKey();
                double featureValue = Double.parseDouble(featureEntry.getValue());

                sumFeatures.put(featureName, sumFeatures.getOrDefault(featureName, 0.0) + featureValue);
                countFeatures.put(featureName, countFeatures.getOrDefault(featureName, 0) + 1);
            }
        }

        // Calculate averages
        Map<String, String> averageFeatures = new HashMap<>();
        for (Map.Entry<String, Double> sumEntry : sumFeatures.entrySet()) {
            String featureName = sumEntry.getKey();
            double sum = sumEntry.getValue();
            int count = countFeatures.get(featureName);
            double average = sum / count;
            averageFeatures.put(featureName, String.valueOf(average));
        }

        return averageFeatures;
    }

    private Map<String, String> extractFeaturesFromNode(JsonNode rootNode) {
        Map<String, String> selectedFeatures = new HashMap<>();

        JsonNode tonalNode = rootNode.path("tonal");
        selectedFeatures.put("tuningEqualTemperedDeviation", tonalNode.path("tuning_equal_tempered_deviation").asText());
        selectedFeatures.put("tuningFrequency", tonalNode.path("tuning_frequency").asText());

        JsonNode rhythmNode = rootNode.path("rhythm");
        selectedFeatures.put("danceability", rhythmNode.path("danceability").asText());
        selectedFeatures.put("bpm", rhythmNode.path("bpm").asText());
        selectedFeatures.put("bpmHistogramFirstPeakBpmMean", rhythmNode.path("bpm_histogram_first_peak_bpm").path("mean").asText());
        selectedFeatures.put("bpmHistogramFirstPeakBpmMedian", rhythmNode.path("bpm_histogram_first_peak_bpm").path("median").asText());
        selectedFeatures.put("bpmHistogramSecondPeakBpmMean", rhythmNode.path("bpm_histogram_second_peak_bpm").path("mean").asText());
        selectedFeatures.put("bpmHistogramSecondPeakBpmMedian", rhythmNode.path("bpm_histogram_second_peak_bpm").path("median").asText());
        selectedFeatures.put("onsetRate", rhythmNode.path("onset_rate").asText());

        JsonNode lowlevelNode = rootNode.path("lowlevel");
        selectedFeatures.put("averageLoudness", lowlevelNode.path("average_loudness").asText());
        selectedFeatures.put("dynamicComplexity", lowlevelNode.path("dynamic_complexity").asText());
        selectedFeatures.put("mfccZeroMean", lowlevelNode.path("mfcc").path("mean").get(0).asText());

        return selectedFeatures;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
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
            return null;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String getMBIDByISRC(String isrc, String title) {
        try {
            Thread.sleep(1500);
            System.out.println("Getting");
            String musicBrainzUrl = "http://musicbrainz.org/ws/2/recording/";

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(musicBrainzUrl)
                    .queryParam("query", "isrc:" + isrc)
                    .queryParam("fmt", "json");

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.USER_AGENT, "MyUniProject/0.0.1 (viliusps@gmail.com)");

            RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, builder.build().toUri());

            ResponseEntity<Map> responseEntity = new RestTemplate().exchange(requestEntity, Map.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                List<Map<String, String>> recordings = Optional.ofNullable(responseEntity.getBody())
                        .map(body -> (List<Map<String, String>>) body.get("recordings"))
                        .orElse(null);

                if (recordings != null && !recordings.isEmpty()) {
                    Optional<Map<String, String>> matchingRecording = recordings.stream()
                            .filter(entry -> isSubstring(entry.get("title"), title) || isSubstring(title, entry.get("title")))
                            .findFirst();

                    return matchingRecording.map(entry -> entry.get("id")).orElse(null);
                }
            } else {
                System.err.println("MusicBrainz API request failed with status code: " + responseEntity.getStatusCode());
            }

            return null;
        } catch(InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        
    }

    private static boolean isSubstring(String str1, String str2) {
        return str1 != null && str2 != null && (str1.contains(str2) || str2.contains(str1));
    }

    public String getClosestSongFromFeatures(float[] originalFeatures) {
        Float bpm = originalFeatures[6];
        Float averageLoudness = originalFeatures[10];
        Float dynamicComplexity = originalFeatures[11];
        Float mfccZeroMean = originalFeatures[8];
        Float bpmHistogramFirstPeakMean = originalFeatures[0];
        Float bpmHistogramFirstPeakMedian = originalFeatures[7];
        Float bpmHistogramSecondPeakMean = originalFeatures[5];
        Float bpmHistogramSecondPeakMedian = originalFeatures[2];
        Float danceability = originalFeatures[1];
        Float onsetRate = originalFeatures[9];
        Float tuningFrequency = originalFeatures[4];
        Float tuningEqualTemperedDeviation = originalFeatures[3];

        String closestRow = "";
        Double minDistance = Double.MAX_VALUE;
        String filePath = "songFeaturesFinal.csv";
        Double threshold = 0.1;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > 17 && 
                    !values[2].isEmpty() && !values[3].isEmpty() && !values[4].isEmpty() && 
                    !values[6].isEmpty() && !values[7].isEmpty() && !values[8].isEmpty() &&
                    !values[9].isEmpty() && !values[10].isEmpty() && !values[11].isEmpty() && 
                    !values[12].isEmpty() && !values[16].isEmpty() && !values[17].isEmpty()) {
                    Float rowAverageLoudness = Float.parseFloat(values[2]);
                    Float rowDynamicComplexity = Float.parseFloat(values[3]);
                    Float rowMfccZeroMean = Float.parseFloat(values[4]);
                    Float rowBpm = Float.parseFloat(values[6]);
                    Float rowBpmHistogramFirstPeakMean = Float.parseFloat(values[7]);
                    Float rowBpmHistogramFirstPeakMedian = Float.parseFloat(values[8]);
                    Float rowBpmHistogramSecondPeakMean = Float.parseFloat(values[9]);
                    Float rowBpmHistogramSecondPeakMedian = Float.parseFloat(values[10]);
                    Float rowDanceability = Float.parseFloat(values[11]);
                    Float rowOnsetRate = Float.parseFloat(values[12]);
                    Float rowTuningFrequency = Float.parseFloat(values[16]);
                    Float rowTuningTemperedDeviation = Float.parseFloat(values[17]);



                    Double distance = Math.sqrt(Math.pow(bpm - rowBpm, 2) +
                                                Math.pow(averageLoudness - rowAverageLoudness, 2) +
                                                Math.pow(dynamicComplexity - rowDynamicComplexity, 2) +
                                                Math.pow(mfccZeroMean - rowMfccZeroMean, 2) +
                                                Math.pow(bpmHistogramFirstPeakMean - rowBpmHistogramFirstPeakMean, 2) +
                                                Math.pow(bpmHistogramFirstPeakMedian - rowBpmHistogramFirstPeakMedian, 2) +
                                                Math.pow(bpmHistogramSecondPeakMean - rowBpmHistogramSecondPeakMean, 2) +
                                                Math.pow(bpmHistogramSecondPeakMedian - rowBpmHistogramSecondPeakMedian, 2) +
                                                Math.pow(danceability - rowDanceability, 2) +
                                                Math.pow(onsetRate - rowOnsetRate, 2) +
                                                Math.pow(tuningFrequency - rowTuningFrequency, 2) +
                                                Math.pow(tuningEqualTemperedDeviation - rowTuningTemperedDeviation, 2));

                    if (distance < minDistance) {
                        minDistance = distance;
                        closestRow = line;
                        if (minDistance <= threshold) {
                            break;
                        }
                    }
                } else {
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading the CSV file.";
        }

        return closestRow;
    }

    public void increasePopularity(Song song) {
        Song songFromDb = getByISRC(song.getIsrc());
        songFromDb.setPopularity(songFromDb.getPopularity() + 1);
        songRepository.save(songFromDb);
    }
    // efd4ed21-3374-4b8f-9fe8-271ec9852074,0,0.743034541607,4.40243434906,-688.681518555,0,109.252311707,110,110,105,105,1.03447961807,3.24817299843,0,C,major,436.708374023,0.0884697809815
    public Song getSongFromSearchResults(String searchResult) {
        String[] values = searchResult.split(",");
        Song resultSong = new Song();

        resultSong.setAverageLoudness(values[2]);
        resultSong.setDynamicComplexity(values[3]);
        resultSong.setMfccZeroMean(values[4]);
        resultSong.setBpm(values[4]);
        resultSong.setBpmHistogramFirstPeakBpmMean(values[7]);
        resultSong.setBpmHistogramFirstPeakBpmMedian(values[8]);
        resultSong.setBpmHistogramSecondPeakBpmMean(values[9]);
        resultSong.setBpmHistogramSecondPeakBpmMedian(values[10]);
        resultSong.setDanceability(values[11]);
        resultSong.setOnsetRate(values[12]);
        resultSong.setTuningFrequency(values[16]);
        resultSong.setTuningEqualTemperedDeviation(values[17]);

        SongArtistName songAndArtistName = getSongAndArtistNameFromMbid(values[0]);
        resultSong.setTitle(songAndArtistName.getSongName());
        resultSong.setSinger(songAndArtistName.getArtistName());

        // Song songFromSpotify = getSongByNameAndArtistFromSpotify(songAndArtistName);
        // System.out.println("Got song from spotify");
        // resultSong.setImageUrl(songFromSpotify.getImageUrl());
        // resultSong.setIsrc(songFromSpotify.getIsrc());
        // resultSong.setSinger(songFromSpotify.getSinger());
        // resultSong.setSpotifyId(songFromSpotify.getSpotifyId());
        // resultSong.setTitle(songFromSpotify.getTitle());

        return resultSong;
    }
    public SongArtistName getSongAndArtistNameFromMbid(String mbid) {
        System.out.println("Getting song and artist from mbid: " + mbid);
        String url = "https://musicbrainz.org/ws/2/recording/?query=mbid:" + mbid + "&fmt=json&inc=isrcs";
         try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.addHeader("User-Agent", "MyUniProject/0.0.1 (viliusps@gmail.com)");
            HttpResponse response = client.execute(request);
            String jsonResponse = EntityUtils.toString(response.getEntity());
            System.out.println(jsonResponse);
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);
            JsonNode recordingNode = rootNode.path("recordings").get(0);
            String songName = recordingNode.path("title").asText();
            JsonNode artistCreditNode = recordingNode.path("artist-credit").get(0);
            String artistName = artistCreditNode.path("name").asText();

            System.out.println("Song Name: " + songName);
            System.out.println("Artist Name: " + artistName);
            SongArtistName songArtistName = new SongArtistName();
            songArtistName.setSongName(songName);
            songArtistName.setArtistName(artistName);
            return songArtistName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}