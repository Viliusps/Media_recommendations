package com.media.recommendations.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRatingRequest {
    private String recommendingType;
    private String recommendingByType;
    private Object recommending;
    private Object recommendingBy;
    private Boolean rating;
}
