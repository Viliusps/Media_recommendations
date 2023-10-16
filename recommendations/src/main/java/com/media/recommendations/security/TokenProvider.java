package com.media.recommendations.security;

import com.media.recommendations.config.AppProperties;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private AppProperties appProperties;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        byte[] decodedKey = Base64.getDecoder().decode(appProperties.getAuth().getTokenSecret());

        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA512");

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        byte[] decodedKey = Base64.getDecoder().decode(appProperties.getAuth().getTokenSecret());

        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA512");
        
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        byte[] decodedKey = Base64.getDecoder().decode(appProperties.getAuth().getTokenSecret());

        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA512");
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(authToken);
            return true;
        } catch (SecurityException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

}