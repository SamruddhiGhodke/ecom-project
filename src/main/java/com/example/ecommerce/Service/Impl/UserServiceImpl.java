package com.example.ecommerce.Service.Impl;

import com.example.ecommerce.Entity.UserDetailsEntity;
import com.example.ecommerce.Repository.UserRepo;
import com.example.ecommerce.Service.UserService;
import com.example.ecommerce.Util.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        userDetails.setAccountNonLocked(true);
        userDetails.setFailedAttempt(0);

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

    @Override
    public void increaseFailedAttempts(UserDetailsEntity userDetails) {
        int attempt = userDetails.getFailedAttempt()+1;
        userDetails.setFailedAttempt(attempt);
        userRepo.save(userDetails);

    }

    @Override
    public void userAccountLock(UserDetailsEntity userDetails) {
        userDetails.setAccountNonLocked(false);
        userDetails.setLockTime(new Date());
        userRepo.save(userDetails);

    }

    @Override
    public boolean unlockAccountTimeExpired(UserDetailsEntity userDetails) {
        long lockTime = userDetails.getLockTime().getTime();
        long unlockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;
        long currentTime = System.currentTimeMillis();

        if(unlockTime<currentTime){
            userDetails.setAccountNonLocked(true);
            userDetails.setFailedAttempt(0);
            userDetails.setLockTime(null);
            userRepo.save(userDetails);
            return true;
        }
        return false;
    }

    @Override
    public void resetAttempts(int userId) {

    }

    @Override
    public void updateUserResetToken(String email, String resetToken) {
        UserDetailsEntity findEmail=userRepo.findByEmail(email);
        findEmail.setResetToken(resetToken);
        userRepo.save(findEmail);

    }

    @Override
    public UserDetailsEntity getUsersByToken(String token) {
        return userRepo.findByResetToken(token);
    }

    @Override
    public UserDetailsEntity updateUser(UserDetailsEntity userDetails) {
        return userRepo.save(userDetails);
    }
}
