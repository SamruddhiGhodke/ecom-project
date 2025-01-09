package com.example.ecommerce.Controller;
import com.example.ecommerce.Entity.CategoryEntity;
import com.example.ecommerce.Entity.ProductEntity;
import com.example.ecommerce.Entity.UserDetailsEntity;
import com.example.ecommerce.Repository.ProductRepo;
import com.example.ecommerce.Service.CategoryService;
import com.example.ecommerce.Service.ProductService;
import com.example.ecommerce.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    public CategoryService categoryService;

    @Autowired
    public ProductService productService;

    @Autowired
    public UserService userService;

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
        return "index";
    }

    @GetMapping("/signin")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/products")
    public String products(Model m, @RequestParam(value = "category", defaultValue="") String category){
        List<CategoryEntity> categories = categoryService.getAllActiveCategory();
        List<ProductEntity> products = productService.getAllActiveProducts(category);
        m.addAttribute("categories",categories);
        m.addAttribute("products", products);
        m.addAttribute("paramValue", category);
        return "product";
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable int id, Model m){
        ProductEntity productId = productService.getProductId(id);
        m.addAttribute("product", productId);
        return "viewProduct";
    }
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute UserDetailsEntity userDetails, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {

        String imageName =file.isEmpty()?"default.jpg":file.getOriginalFilename();
        userDetails.setProfileImage(imageName);
        UserDetailsEntity saveUser = userService.saveUser(userDetails);
        if(!ObjectUtils.isEmpty(saveUser)){
            if(!file.isEmpty()){
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"profileImg"+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg", "Registered Successfully");
        }
        else{
            session.setAttribute("errorMsg", "something went wrong");
        }

        return "redirect:/register";
    }
}
