package com.example.ecommerce.Repository;

import com.example.ecommerce.Entity.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserDetailsEntity, Integer> {
}
