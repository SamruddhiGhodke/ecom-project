package com.example.ecommerce.Service.Impl;

import com.example.ecommerce.Entity.CartEntity;
import com.example.ecommerce.Entity.ProductEntity;
import com.example.ecommerce.Entity.UserDetailsEntity;
import com.example.ecommerce.Repository.CartRepo;
import com.example.ecommerce.Repository.ProductRepo;
import com.example.ecommerce.Repository.UserRepo;
import com.example.ecommerce.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Override
    public CartEntity saveCart(Integer productId, Integer userId) {
        UserDetailsEntity userDetails=userRepo.findById(userId).get();
        ProductEntity product=productRepo.findById(productId).get();
        CartEntity cartStatus = cartRepo.findByProductEntityIdAndUserDetailsEntityId(productId, userId);
        CartEntity cart = null;

        if(ObjectUtils.isEmpty(cartStatus)){
            cart=new CartEntity();
            cart.setProductEntity(product);
            cart.setUserDetailsEntity(userDetails);
            cart.setQuantity(1);
            cart.setTotalPrice(1*product.getDiscountPrice());
        }
        else {
            cart=cartStatus;
            cart.setQuantity(cart.getQuantity()+1);
            cart.setTotalPrice(cart.getQuantity()*cart.getProductEntity().getDiscountPrice());
        }
        CartEntity saveCart=cartRepo.save(cart);
        return saveCart;
    }

    @Override
    public List<CartEntity> getCartByUser(Integer userId) {
        List<CartEntity> carts=cartRepo.findByUserDetailsEntityId(userId);
        Double totalPrice=0.0;
        for(CartEntity c:carts){
            totalPrice = (c.getProductEntity().getDiscountPrice()*c.getQuantity())+totalPrice;

        }
        return carts;
    }

    @Override
    public Integer getCountCart(Integer userId) {
        Integer countByUserId =cartRepo.countByUserDetailsEntityId(userId);
        return countByUserId;
    }
}
