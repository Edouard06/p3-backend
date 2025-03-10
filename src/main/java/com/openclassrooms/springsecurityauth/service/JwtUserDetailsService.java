package com.openclassrooms.springsecurityauth.service;

import com.openclassrooms.springsecurityauth.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if ("user".equals(email)) {
            return new CustomUserDetails(User.of("test@test.com", "password", "TestUser"));
        } else {
            throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email);
        }
    }

    public CustomUserDetails loadUserByUserEmail(String email) throws UsernameNotFoundException {
        return loadUserByUsername(email);
    }
}
