package com.openclassrooms.springsecurityauth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.openclassrooms.springsecurityauth.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utilitaire pour la gestion des tokens JWT en utilisant la bibliothèque Auth0.
 */
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.token.validity}")
    private long tokenValidity; 

    /**
     * Vérifie la validité d'un token JWT et retourne l'objet DecodedJWT.
     *
     * @param token 
     * @return 
     */
    public DecodedJWT verifyToken(String token) {
        // Création de l'algorithme avec la clé secrète
        Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
        // Crée le vérificateur de token
        JWTVerifier verifier = JWT.require(algorithm).build();
        // Vérifie et décode le token
        return verifier.verify(token);
    }

    /**
     * Extrait l'email (subject) du token JWT.
     *
     * @param token 
     * @return 
     */
    public String getUserEmailFromToken(String token) {
        return verifyToken(token).getSubject();
    }

    /**
     * Génère un token JWT pour un utilisateur donné.
     *
     * @param userDetails 
     * @return le token JWT généré
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
     * Valide le token en vérifiant son subject et sa date d'expiration.
     *
     * @param token 
     * @param userDetails 
     * @return true si le token est valide, false sinon
     */
    public boolean validateToken(String token, CustomUserDetails userDetails) {
        try {
            DecodedJWT jwt = verifyToken(token);
            String username = jwt.getSubject();
            Date expiration = jwt.getExpiresAt();
            return username.equals(userDetails.getUsername()) && expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
