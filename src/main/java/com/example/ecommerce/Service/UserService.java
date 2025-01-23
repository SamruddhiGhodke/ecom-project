package com.example.ecommerce.Service;

import com.example.ecommerce.Entity.UserDetailsEntity;

import java.util.List;

public interface UserService {

    public UserDetailsEntity saveUser(UserDetailsEntity userDetails);

    public UserDetailsEntity getUserByEmail(String email);

    public List<UserDetailsEntity>getUsers(String role);

    public Boolean updateUserStatus(Integer id, Boolean status);

    public void increaseFailedAttempts(UserDetailsEntity userDetails);

    public void userAccountLock(UserDetailsEntity userDetails);

    public boolean unlockAccountTimeExpired(UserDetailsEntity userDetails);

    public void resetAttempts(int userId);

    public void updateUserResetToken(String email, String resetToken);

    public UserDetailsEntity getUsersByToken(String token);

    public UserDetailsEntity updateUser(UserDetailsEntity userDetails);
}
