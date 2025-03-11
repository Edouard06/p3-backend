package com.openclassrooms.springsecurityauth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.openclassrooms.springsecurityauth.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.token.validity}")
    private long tokenValidity;

    /**
     * Verifies the given JWT token.
     *
     * @param token 
     * @return a DecodedJWT object if the token is valid
     */
    public DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    /**
     * Extracts the user email (subject) from the JWT token.
     *
     * @param token 
     * @return the user email (subject)
     */
    public String getUserEmailFromToken(String token) {
        return verifyToken(token).getSubject();
    }

    /**
     * Generates a JWT token for the given user details.
     *
     * @param userDetails 
     * @return the generated JWT token
     */
    public String generateToken(CustomUserDetails userDetails) {
        Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenValidity);
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(algorithm);
    }

    /**
     * Validates the given JWT token against the provided user details.
     *
     * @param token 
     * @param userDetails 
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token, CustomUserDetails userDetails) {
        try {
            DecodedJWT jwt = verifyToken(token);
            return jwt.getSubject().equals(userDetails.getUsername()) &&
                   jwt.getExpiresAt().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
