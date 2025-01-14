package com.example.ecommerce.Controller;

import com.example.ecommerce.Entity.CategoryEntity;
import com.example.ecommerce.Entity.UserDetailsEntity;
import com.example.ecommerce.Service.CategoryService;
import com.example.ecommerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute
    public void getUserDetails(Principal p, Model m){
        if(p!=null){
            String email = p.getName();
            UserDetailsEntity userDetails=userService.getUserByEmail(email);
            m.addAttribute("user", userDetails);
        }
        List<CategoryEntity> allActiveCategory=categoryService.getAllActiveCategory();
        m.addAttribute("categorys", allActiveCategory);
    }

    @GetMapping("/")
    public String home(){
        return "user/home";
    }
}
