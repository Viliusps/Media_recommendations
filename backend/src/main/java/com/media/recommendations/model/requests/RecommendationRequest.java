package com.media.recommendations.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequest {
    private String recommendingType;
    private String recommendingByType;
    private String recommendingBy;
}
