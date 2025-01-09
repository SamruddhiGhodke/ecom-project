package com.example.ecommerce.Service;

import com.example.ecommerce.Entity.UserDetailsEntity;

import java.util.List;

public interface UserService {

    public UserDetailsEntity saveUser(UserDetailsEntity userDetails);

    public UserDetailsEntity getUserByEmail(String email);

    public List<UserDetailsEntity>getUsers(String role);

    public Boolean updateUserStatus(Integer id, Boolean status);
}
