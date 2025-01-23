package com.example.ecommerce.Service;

import com.example.ecommerce.Entity.CartEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CartService {

    public CartEntity saveCart(Integer productId, Integer userId);

    public List<CartEntity> getCartByUser(Integer userId);

    public Integer getCountCart(Integer userId);
}
