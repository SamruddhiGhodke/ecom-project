package com.example.ecommerce.Service.Impl;

import com.example.ecommerce.Entity.UserDetailsEntity;
import com.example.ecommerce.Repository.UserRepo;
import com.example.ecommerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UserRepo userRepo;

    @Override
    public UserDetailsEntity saveUser(UserDetailsEntity userDetails) {
        UserDetailsEntity saveUser=userRepo.save(userDetails);
        return saveUser;
    }
}
