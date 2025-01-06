package com.example.ecommerce.Service.Impl;

import com.example.ecommerce.Entity.CategoryEntity;
import com.example.ecommerce.Repository.CategoryRepo;
import com.example.ecommerce.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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

    @Override
    public Boolean deleteCategory(int id) {
        CategoryEntity categoryEntity = categoryRepo.findById(id).orElse(null);
        if(!ObjectUtils.isEmpty(categoryEntity)){
             categoryRepo.delete(categoryEntity);
             return true;
        }

        return false;
    }

    @Override
    public CategoryEntity getCategoryById(int id) {
        CategoryEntity category=categoryRepo.findById(id).orElse(null);
        return category;
    }

    @Override
    public List<CategoryEntity> getAllActiveCategory() {
        List<CategoryEntity> categoryEntities=categoryRepo.findByIsActiveTrue();
        return categoryEntities;
    }
}
