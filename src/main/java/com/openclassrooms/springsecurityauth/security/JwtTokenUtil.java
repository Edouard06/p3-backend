package com.openclassrooms.springsecurityauth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import com.openclassrooms.springsecurityauth.service.CustomUserDetails;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    // Génère une clé sécurisée pour HS512 (512 bits ou plus)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // Durée de validité du token en millisecondes (ici 24 heures)
    private long tokenValidity = 86400000;

    @PostConstruct
    public void init() {
        // Log temporaire pour vérifier la taille de la clé
        System.out.println("Taille de la clé (bits) : " + key.getEncoded().length * 8);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    public String getUserEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    public String generateToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenValidity);
        return Jwts.builder()
                   .setSubject(userDetails.getUsername())
                   .setIssuedAt(now)
                   .setExpiration(expiryDate)
                   .signWith(key)
                   .compact();
    }

    public Boolean validateToken(String token, CustomUserDetails userDetails) {
        final String username = getUserEmailFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
