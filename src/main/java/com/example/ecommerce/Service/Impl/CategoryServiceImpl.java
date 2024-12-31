package com.example.ecommerce.Service.Impl;

import com.example.ecommerce.Entity.CategoryEntity;
import com.example.ecommerce.Repository.CategoryRepo;
import com.example.ecommerce.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    public CategoryRepo categoryRepo;

    @Override
    public CategoryEntity saveCategory(CategoryEntity categoryEntity) {
        return categoryRepo.save(categoryEntity);
    }

    @Override
    public Boolean existCategory(String name) {
        return categoryRepo.existsByName(name);
    }

    @Override
    public List<CategoryEntity> getAllCategory() {
        return categoryRepo.findAll();
    }
}
