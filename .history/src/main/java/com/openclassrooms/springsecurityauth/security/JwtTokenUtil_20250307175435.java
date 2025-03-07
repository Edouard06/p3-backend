package com.openclassrooms.springsecurityauth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import com.openclassrooms.springsecurityauth.service.CustomUserDetails;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    // Clé secrète (à mettre dans un fichier de propriétés dans un projet réel)
    private String secret = "YOUR_SECRET_KEY";

    // Récupère tous les claims du token
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    // Extrait le sujet (ici, l'email de l'utilisateur) du token
    public String getUserEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Méthode générique pour extraire un claim spécifique
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Vérifie si le token est expiré
    private Boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    // Valide le token par rapport aux détails de l'utilisateur
    public Boolean validateToken(String token, CustomUserDetails userDetails) {
        final String username = getUserEmailFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
