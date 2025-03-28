package com.openclassrooms.springsecurityauth.service;

import com.openclassrooms.springsecurityauth.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom implementation of Spring Security's UserDetails interface,
 * used to provide user authentication and authorization data.
 */
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Returns the user's roles.
     * Currently, all users have ROLE_USER.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Always active
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Never locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Always valid
    }

    @Override
    public boolean isEnabled() {
        return true; // Always enabled
    }

    /**
     * Custom method to expose the User's ID.
     */
    public Long getId() {
        return user.getId();
    }
}
