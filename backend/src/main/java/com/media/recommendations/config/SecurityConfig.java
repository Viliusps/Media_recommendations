package com.media.recommendations.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String songs = "/api/v1/songs/**";
        String movies = "/api/v1/movies/**";
        String comments = "/api/v1/comments/**";
        String auth = "/api/v1/auth/**";
        String admin = "/api/v1/users/admin/**";
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf
                    .disable())
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.GET, comments, songs, movies).permitAll()
                    .requestMatchers(auth).permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/movies/omdb").permitAll()
                    .requestMatchers(HttpMethod.POST, movies, songs).hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.PUT, movies, songs).hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, movies, songs).hasAuthority("ADMIN")
                    .requestMatchers(admin).hasAuthority("ADMIN")
                    .anyRequest()
                    .authenticated())
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));

        return http.build();
    }
}