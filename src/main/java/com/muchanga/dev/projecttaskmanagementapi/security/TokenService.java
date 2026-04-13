package com.muchanga.dev.projecttaskmanagementapi.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.muchanga.dev.projecttaskmanagementapi.entity.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    public String generateToken(User user){
        try {
            return JWT.create()
                    .withIssuer("auth")
                    .withSubject(user.getLogin())
                    .withExpiresAt(genExpirationDate())
                    .sign(getAlgorithm());

        }catch (JWTCreationException exception){
            throw new RuntimeException("Error while generating token",exception);

        }
    }
    public String validateToken(String token){
        try {
            return JWT.require(getAlgorithm())
                    .withIssuer("auth")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTCreationException exception){
            return "";
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.UTC);
    }
}
