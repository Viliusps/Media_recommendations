package com.media.recommendations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.media.recommendations.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class RecommendationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecommendationsApplication.class, args);
	}

}
