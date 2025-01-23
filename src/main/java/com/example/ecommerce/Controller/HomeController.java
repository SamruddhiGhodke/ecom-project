package com.example.ecommerce.Controller;
import com.example.ecommerce.Entity.CategoryEntity;
import com.example.ecommerce.Entity.ProductEntity;
import com.example.ecommerce.Entity.UserDetailsEntity;
import com.example.ecommerce.Repository.ProductRepo;
import com.example.ecommerce.Service.CartService;
import com.example.ecommerce.Service.CategoryService;
import com.example.ecommerce.Service.ProductService;
import com.example.ecommerce.Service.UserService;
import com.example.ecommerce.Util.CommonUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {

    @Autowired
    public CategoryService categoryService;

    @Autowired
    public ProductService productService;

    @Autowired
    public UserService userService;

    @Autowired
    public CommonUtil commonUtil;

    @Autowired
    public BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public CartService cartService;

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
    @GetMapping("/forgotPassword")
    public String forgotPassword(){
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String sendForgotPassword(@RequestParam String email, HttpSession session, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        UserDetailsEntity userDetails = userService.getUserByEmail(email);
        if(ObjectUtils.isEmpty(userDetails)){
            session.setAttribute("errorMsg", "Invalid email");
        }
        else{
            String resetToken = UUID.randomUUID().toString();
            userService.updateUserResetToken(email,resetToken);
            String url = commonUtil.generateUrl(request)+"/resetPassword?token="+resetToken;
            Boolean sendMail=commonUtil.sendMail(url, email);
            if(sendMail){
                session.setAttribute("succMsg", "pls check your mail... password reset message sent to your email Id");
            }
            else {
                session.setAttribute("errorMsg", "Something wrong on server ! Email not send");
            }

        }
        return "redirect:/forgotPassword";
    }

    @GetMapping("/resetPassword")
    public String resetPassword(@RequestParam String token, HttpSession session, Model m){
        UserDetailsEntity userDetails = userService.getUsersByToken(token);
        if(userDetails==null){
            m.addAttribute("message", "Your link is invalid or expired");
            return "msg";
        }
        m.addAttribute("token", token);

        return "resetPassword";
    }

    @PostMapping("/resetPassword")
    public String showResetPassword(@RequestParam String token, @RequestParam String password, HttpSession session, Model m){
        UserDetailsEntity usersByToken = userService.getUsersByToken(token);
        if(usersByToken==null){
            m.addAttribute("errorMsg", "Your link is invalid or expired");
            return "msg";
        }
        else {
            usersByToken.setPassword(passwordEncoder.encode(password));
            usersByToken.setResetToken(null);
            userService.updateUser(usersByToken);
           // session.setAttribute("succMsg", "password changed successfully");
            m.addAttribute("message", "password changed successfully");
            return "msg";
        }


    }
}
