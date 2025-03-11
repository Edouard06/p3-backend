package com.openclassrooms.springsecurityauth.configuration;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.openclassrooms.springsecurityauth.security.JwtTokenUtil;
import com.openclassrooms.springsecurityauth.service.CustomUserDetails;
import com.openclassrooms.springsecurityauth.service.JwtUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that intercepts HTTP requests to verify the JWT token.
 * It extracts the token from the Authorization header, vérifie sa validité,
 * et si tout est OK, configure l'authentification dans le SecurityContext.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    private static final String AUTHORIZATION_PREFIX = "Bearer ";

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param jwtUserDetailsService le service pour charger les détails de l'utilisateur
     * @param jwtTokenUtil l'utilitaire de gestion du token JWT
     */
    public JwtAuthFilter(JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Exclure les endpoints de connexion et d'inscription du filtrage
        String path = request.getRequestURI();
        return path.startsWith("/auth/login") || path.startsWith("/auth/register");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith(AUTHORIZATION_PREFIX)) {
            jwtToken = requestTokenHeader.substring(AUTHORIZATION_PREFIX.length());
            try {
                // Vérifie le token et extrait le username
                DecodedJWT jwt = jwtTokenUtil.verifyToken(jwtToken);
                username = jwtTokenUtil.getUserEmailFromToken(jwtToken);
            } catch (Exception e) {
                logger.error("Unable to get JWT Token or JWT Token has expired", e);
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails userDetails = jwtUserDetailsService.loadUserByUserEmail(username);
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
