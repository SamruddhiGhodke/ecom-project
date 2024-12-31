package com.example.ecommerce.Service;

import com.example.ecommerce.Entity.CategoryEntity;
import com.example.ecommerce.Repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CategoryService {

    public CategoryEntity saveCategory(CategoryEntity categoryEntity);

    public Boolean existCategory(String name);

    public List<CategoryEntity> getAllCategory();


}
