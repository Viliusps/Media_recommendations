package com.media.recommendations.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.media.recommendations.model.User;
import com.media.recommendations.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        return null;
    }

    public User createUser(User user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        String hash = "";
        try {
            hash = toHexString(getSHA(user.getUsername() + ":" + user.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown for incorrect algorithm: " + e);
        }
        newUser.setPassword(hash);
        return userRepository.save(newUser);
    }

    public boolean existsUser(long id) {
        return userRepository.existsById(id);
    }
    public User userByUsername(String username) {
        return userRepository.userByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User updateUser(Long id, User user) {
        User userFromDb = userRepository.findById(id).get();
        if (!Objects.equals(userFromDb.getUsername(), user.getUsername()) &&
                userRepository.userByUsername(user.getUsername()).isPresent()) {
            return null;
        }

        userFromDb.setUsername(user.getUsername());
        userFromDb.setEmail(user.getEmail());
        userFromDb.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(userFromDb);
    }

    public User adminUpdateUser(Long id, User user) {
        User userFromDb = userRepository.findById(id).get();
        if (!Objects.equals(userFromDb.getUsername(), user.getUsername()) &&
                userRepository.userByUsername(user.getUsername()).isPresent()) {
            return null;
        }

        userFromDb.setUsername(user.getUsername());
        userFromDb.setEmail(user.getEmail());
        userFromDb.setPassword(user.getPassword());
        userFromDb.setRole(user.getRole());

        return userRepository.save(userFromDb);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    public boolean comparePasswords(String username, String password) {
        String hash = "";
        try {
            hash = toHexString(getSHA(username + ":" + password));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown for incorrect algorithm: " + e);
        }
        User userFromDb = userByUsername(username);
        return hash.equals(userFromDb.getPassword());
    }
}
