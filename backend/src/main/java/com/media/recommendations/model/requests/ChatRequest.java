package com.media.recommendations.model.requests;

import java.util.ArrayList;
import java.util.List;

import com.media.recommendations.model.Message;

import lombok.Data;

@Data
public class ChatRequest {

    private String model;
    private List<Message> messages;
    private int n;
    private double temperature;

    public ChatRequest(String model, String prompt) {
        this.model = model;
        this.n = 100;
        
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }

}
