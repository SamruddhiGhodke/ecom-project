package com.example.ecommerce.Repository;

import com.example.ecommerce.Entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CartRepo extends JpaRepository<CartEntity, Integer> {
    public CartEntity findByProductEntityIdAndUserDetailsEntityId(Integer productId, Integer userId);


    public Integer countByUserDetailsEntityId(Integer userId);
}
