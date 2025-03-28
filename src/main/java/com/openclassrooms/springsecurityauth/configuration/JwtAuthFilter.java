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

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    private static final String AUTHORIZATION_PREFIX = "Bearer ";

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtAuthFilter(JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * Bypass the filter for public endpoints (e.g., login, registration, static assets).
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/auth/login") ||
               path.startsWith("/auth/register") ||
               path.startsWith("/images/");
    }

    /**
     * Filter to authenticate user based on JWT token in the Authorization header.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        // Check if the Authorization header contains a Bearer token
        if (requestTokenHeader != null && requestTokenHeader.startsWith(AUTHORIZATION_PREFIX)) {
            jwtToken = requestTokenHeader.substring(AUTHORIZATION_PREFIX.length());
            try {
                // Validate and decode the token
                DecodedJWT jwt = jwtTokenUtil.verifyToken(jwtToken);
                username = jwtTokenUtil.getUserEmailFromToken(jwtToken);
                logger.info("JWT Token valid for user: {}", username);
            } catch (Exception e) {
                logger.error("Invalid or expired JWT token", e);
            }
        } else {
            logger.warn("Authorization header missing or malformed");
        }

        // Set authentication context if token is valid and no authentication exists
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails userDetails = jwtUserDetailsService.loadUserByUserEmail(username);
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                logger.info("Authentication successful for user: {}", username);
            }
        }

        // Continue filter chain
        chain.doFilter(request, response);
    }
}
