package com.example.pfebackfinal.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.pfebackfinal.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class JWTService {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String USERNAME_CLAIM = "username";

    @Value("${jwt.secret:secret}")
    private String secret;

    @Value("${jwt.validity:60}")
    private int tokenValidity;

    public String generateToken(String username) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, tokenValidity);
            Date expiresAt = calendar.getTime();
            String signedJWT = JWT.create()
                    .withClaim(USERNAME_CLAIM, username)
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC256(secret));
            return TOKEN_PREFIX.concat(signedJWT);
        } catch (Exception e) {
            throw new ApplicationException("cannot_generate_token");
        }
    }

    public void validateTokenSignature(String authHeader) {
        try {
            String token = getTokenValue(authHeader);
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            jwtVerifier.verify(token);
        } catch (Exception e) {
            throw new ApplicationException("invalid_token_signature");
        }
    }

    public String getClaim(String authHeader, String name) {
        final String token = getTokenValue(authHeader);
        DecodedJWT decodedJWT = JWT.decode(token);
        final Claim claim = decodedJWT.getClaim(name);
        if (claim.isNull()) {
            throw new ApplicationException("invalid_token_signature");
        }
        return claim.asString();
    }

    private static String getTokenValue(String authHeader) {
        return authHeader.substring(TOKEN_PREFIX.length());
    }
}
