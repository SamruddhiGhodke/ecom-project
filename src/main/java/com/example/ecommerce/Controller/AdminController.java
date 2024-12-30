package com.example.ecommerce.Controller;

import com.example.ecommerce.Entity.CategoryEntity;
import com.example.ecommerce.Service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    public CategoryService categoryService;

    @GetMapping("/")
    public String index(){
        return "admin/index";
    }

    @GetMapping("/addProduct")
    public String addProduct(){
        return "admin/addProduct";
    }

    @GetMapping("/category")
    public String category(){
        return "admin/category";
    }
    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute CategoryEntity categoryEntity, HttpSession session){
        categoryService.saveCategory(categoryEntity);
        return "redirect:/category";
    }
}
