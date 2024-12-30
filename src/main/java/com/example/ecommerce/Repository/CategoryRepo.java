package com.example.ecommerce.Repository;

import com.example.ecommerce.Entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

public interface CategoryRepo extends JpaRepository<CategoryEntity, Integer> {
}
