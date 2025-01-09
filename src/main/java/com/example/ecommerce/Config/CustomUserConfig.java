package com.example.ecommerce.Config;

import com.example.ecommerce.Entity.UserDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CustomUserConfig implements UserDetails {


    private UserDetailsEntity userDetailsEntity;

    public CustomUserConfig(UserDetailsEntity userDetailsEntity) {
        super();
        this.userDetailsEntity = userDetailsEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userDetailsEntity.getRole());
        return Arrays.asList(authority);
    }

    @Override
    public String getPassword() {
        return userDetailsEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userDetailsEntity.getEmail();
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
        return userDetailsEntity.getIsEnable();
    }
}
