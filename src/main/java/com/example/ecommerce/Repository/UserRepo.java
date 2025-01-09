package com.example.ecommerce.Repository;

import com.example.ecommerce.Entity.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<UserDetailsEntity, Integer> {

    public UserDetailsEntity findByEmail(String email) throws UsernameNotFoundException;

    public List<UserDetailsEntity> findByRole(String role);
}
