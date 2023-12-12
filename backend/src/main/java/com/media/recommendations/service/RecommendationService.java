package com.media.recommendations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.media.recommendations.model.requests.ChatRequest;
import com.media.recommendations.model.requests.RecommendationRequest;
import com.media.recommendations.model.responses.ChatResponse;
import com.media.recommendations.model.responses.RecommendationResponse;

@Service
public class RecommendationService {
    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${OAI.model}")
    private String model;
    
    @Value("${OAI.api.url}")
    private String apiUrl;

    public RecommendationService(@Qualifier("openaiRestTemplate")@Autowired RestTemplate restTemplate, @Value("${OAI.model}") String model, @Value("${OAI.api.url}") String apiUrl)
    {
        this.restTemplate = restTemplate;
        this.model = model;
        this.apiUrl = apiUrl;
    }

    public RecommendationResponse getRecommendation(RecommendationRequest originalRequest) {
        String prompt = "";

        switch(originalRequest.getRecommendingType()) {
            case "Song":
                prompt += "Recommend me a song, based on ";
                switch(originalRequest.getRecommendingByType()){
                    case "Song":
                        prompt += "a song that I like. The song's name is " + originalRequest.getRecommendingBy() + ". Reply only with a spotify id of your song, add nothing else.";
                        break;
                    case "Movie":
                        prompt += "a movie that I like. The movie's name is " + originalRequest.getRecommendingBy() + ". Reply only with a spotify id of your song, add nothing else.";
                        break;
                    case "Spotify":
                        break;
                }
                break;
            case "Movie":
                prompt += "Recommend me a movie, based on ";
                switch(originalRequest.getRecommendingByType()){
                        case "Song":
                            prompt += "a song that I like. The song's name is " + originalRequest.getRecommendingBy() + ". Reply only with an imdb id of your movie, add nothing else.";
                            break;
                        case "Movie":
                            prompt += "a movie that I like. The movie's name is " + originalRequest.getRecommendingBy() + ". Reply only with an imdb id of your movie, add nothing else.";
                            break;
                        case "Spotify":
                            break;
                    }
                    break;

        }
        System.out.println(prompt);
        ChatRequest request = new ChatRequest(model, prompt);
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
        String responseId = response.getChoices().get(0).getMessage().getContent();
        
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return null;
        }
        return new RecommendationResponse(originalRequest.getRecommendingType(), responseId);
    }
    
}
