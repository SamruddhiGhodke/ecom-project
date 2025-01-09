package com.example.ecommerce.Service.Impl;

import com.example.ecommerce.Config.CustomUserConfig;
import com.example.ecommerce.Entity.UserDetailsEntity;
import com.example.ecommerce.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetailsEntity userDetails=userRepo.findByEmail(username);
        if(userDetails==null){
            throw new UsernameNotFoundException("user not found");
        }
        return new CustomUserConfig(userDetails);
    }
}
