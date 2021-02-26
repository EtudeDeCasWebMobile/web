package com.amboucheba.etudeDeCasWeb.Models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Map;

public class AuthDetails implements UserDetails {

    private Long userId;
    private String email;
    private String password;

    private Map<Long, String> authorities;

    public AuthDetails(Long userId, String email, String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
    }

    public AuthDetails() {
    }

    public Boolean hasAuthority(Long serieTemporelleId, String action ){

        String authority = authorities.get(serieTemporelleId);
        if (authority == null) return false;
        if (action.equals("r") ) return true;
        return authority.equals(action);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // Using email instead of password
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
