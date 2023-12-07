package com.media.recommendations.auth;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.media.recommendations.config.JwtService;
import com.media.recommendations.model.Role;
import com.media.recommendations.model.User;
import com.media.recommendations.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public boolean register(RegisterRequest request) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(request.getEmail());
        boolean isValidEmail = matcher.matches();
        if(!isValidEmail) throw new IllegalArgumentException("Invalid email address");

        var user = User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
        Optional<User> temp = repository.userByUsername(user.getUsername());
        if(!temp.isPresent()) temp = repository.userByEmail(user.getEmail());
        if (temp.isPresent()) {
            return false;
        }
        repository.save(user);
        return true;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws IllegalAccessException {
        var user = repository.userByUsername(request.getUsername())
                .orElseThrow();

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

    public AuthenticationResponse refreshToken(AuthenticationRequest request) throws IllegalAccessException {
        var user = repository.userByUsername(request.getUsername())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }


    public String extractRole(String request) {
        return jwtService.extractRole(request);
    }

    public String extractId(String request) {
        return jwtService.extractId(request);
    }
}