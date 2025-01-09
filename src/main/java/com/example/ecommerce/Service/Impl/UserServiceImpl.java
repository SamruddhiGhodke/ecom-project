package com.example.ecommerce.Service.Impl;

import com.example.ecommerce.Entity.UserDetailsEntity;
import com.example.ecommerce.Repository.UserRepo;
import com.example.ecommerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetailsEntity saveUser(UserDetailsEntity userDetails) {
        userDetails.setRole("ROLE_USER");
        userDetails.setIsEnable(true);
        String encode=passwordEncoder.encode(userDetails.getPassword());
        userDetails.setPassword(encode);
        UserDetailsEntity saveUser=userRepo.save(userDetails);
        return saveUser;
    }

    @Override
    public UserDetailsEntity getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public List<UserDetailsEntity> getUsers(String role) {
        return userRepo.findByRole(role);
    }

    @Override
    public Boolean updateUserStatus(Integer id, Boolean status) {
        Optional<UserDetailsEntity> findByUser = userRepo.findById(id);
        if(findByUser.isPresent()){
            UserDetailsEntity userDetails = findByUser.get();
            userDetails.setIsEnable(status);
            userRepo.save(userDetails);
            return true;
        }
        return false;
    }
}
