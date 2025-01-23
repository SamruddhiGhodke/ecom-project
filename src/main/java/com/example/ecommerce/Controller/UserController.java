package com.example.ecommerce.Controller;

import com.example.ecommerce.Entity.CartEntity;
import com.example.ecommerce.Entity.CategoryEntity;
import com.example.ecommerce.Entity.UserDetailsEntity;
import com.example.ecommerce.Service.CartService;
import com.example.ecommerce.Service.CategoryService;
import com.example.ecommerce.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @ModelAttribute
    public void getUserDetails(Principal p, Model m){
        if(p!=null){
            String email = p.getName();
            UserDetailsEntity userDetails=userService.getUserByEmail(email);
            m.addAttribute("user", userDetails);
            Integer countCart = cartService.getCountCart(userDetails.getId());
            m.addAttribute("countCart", countCart);
        }
        List<CategoryEntity> allActiveCategory=categoryService.getAllActiveCategory();
        m.addAttribute("categorys", allActiveCategory);
    }

    @GetMapping("/")
    public String home(){
        return "user/home";
    }

    @GetMapping("/addCart")
    public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session){
        CartEntity saveCart = cartService.saveCart(pid, uid);
        if(ObjectUtils.isEmpty(saveCart)){
            session.setAttribute("errorMsg", "product add to cart failed");
        }
        else{
            session.setAttribute("succMsg", "product added to cart");
        }
        return "redirect:/product/"+pid;
    }
}
