package com.example.ecommerce.Service.Impl;

import com.example.ecommerce.Entity.UserDetailsEntity;
import com.example.ecommerce.Repository.UserRepo;
import com.example.ecommerce.Service.UserService;
import com.example.ecommerce.Util.AppConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFailedHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String email=request.getParameter("username");
        UserDetailsEntity userDetails = userRepo.findByEmail(email);

        if(userDetails.getIsEnable()){
            if(userDetails.getAccountNonLocked()){
                if(userDetails.getFailedAttempt() < AppConstant.ATTEMPT_TIME){
                    userService.increaseFailedAttempts(userDetails);
                }
                else {
                    userService.userAccountLock(userDetails);
                    exception = new LockedException("your account is Locked!! failed attempt : 3");
                }

            }
            else {
                if(userService.unlockAccountTimeExpired(userDetails)){
                    exception = new LockedException("your account is unlocked!! please try to login");
                }
                else {
                    exception = new LockedException("your account is Locked!! Please try after sometime");
                }

            }

        }
        else {
            exception = new LockedException("your account is InActive");
        }
        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
