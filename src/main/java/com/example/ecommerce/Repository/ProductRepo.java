package com.example.ecommerce.Repository;

import com.example.ecommerce.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity, Integer> {

   public List<ProductEntity> findByIsActiveTrue();

   public List<ProductEntity> findByCategory(String category);
}
