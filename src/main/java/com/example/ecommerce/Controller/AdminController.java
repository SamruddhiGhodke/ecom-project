package com.example.ecommerce.Controller;
//import com.example.ecommerce.Entity.CategoryEntity;
import com.example.ecommerce.Entity.CategoryEntity;
//import com.example.ecommerce.Service.CategoryService;
import com.example.ecommerce.Entity.ProductEntity;
import com.example.ecommerce.Entity.UserDetailsEntity;
import com.example.ecommerce.Service.CategoryService;
import com.example.ecommerce.Service.ProductService;
import com.example.ecommerce.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.ObjectUtils;
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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    public CategoryService categoryService;

    @Autowired
    public ProductService productService;

    @Autowired
    public UserService userService;

    @GetMapping("/")
    public String index(){
        return "admin/index";
    }

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

    @GetMapping("/addProduct")
    public String addProduct(Model m){
        List<CategoryEntity> categories = categoryService.getAllCategory();
        m.addAttribute("categories", categories);
        return "admin/addProduct";
    }

    @GetMapping("/category")
    public String category(Model m){
        m.addAttribute("categories",categoryService.getAllCategory());
        return "admin/category";
    }
    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute CategoryEntity categoryEntity, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        String imageName = file!=null ?file.getOriginalFilename():"NA";
       // categoryEntity.setImageName(imageName);
         categoryEntity.setImageName(imageName);

        Boolean existCategory = categoryService.existCategory(categoryEntity.getName());
        if(existCategory){
            session.setAttribute("errorMsg","name is already exists");
        }
        else {
            CategoryEntity saveCategory=categoryService.saveCategory(categoryEntity);
            if(ObjectUtils.isEmpty(saveCategory)){
                session.setAttribute("errorMsg", "Not saved internal error");
            }
            else{
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"categoryImg"+File.separator+file.getOriginalFilename());
                //System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                session.setAttribute("succMsg","Saved successfully");
            }
        }


        return "redirect:/admin/category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session){
        Boolean deleteCategory = categoryService.deleteCategory(id);
        if(deleteCategory){
            session.setAttribute("succMsg","Deleted Successfully");
        }
        else {
            session.setAttribute("errorMsg", "something went wrong");
        }
        return "redirect:/admin/category";

    }

    @GetMapping("/editCategory/{id}")
    public String editCategory(@PathVariable int id, Model m){
        m.addAttribute("category", categoryService.getCategoryById(id));
        return "/admin/editCategory";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute CategoryEntity categoryEntity, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        CategoryEntity category = categoryService.getCategoryById(categoryEntity.getId());
        String fileName = file.isEmpty() ? category.getImageName() : file.getOriginalFilename();
        if (!ObjectUtils.isEmpty(category)) {
            category.setName(categoryEntity.getName());
            category.setIsActive(categoryEntity.getIsActive());
            category.setImageName(fileName);
        }
        CategoryEntity updateCategory = categoryService.saveCategory(category);
        if (!ObjectUtils.isEmpty(updateCategory)) {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "categoryImg" + File.separator + file.getOriginalFilename());

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }


            session.setAttribute("succMsg", "Category updated Successfully");
        } else {
            session.setAttribute("errorMsg", "something went wrong");
        }
        return "redirect:/admin/editCategory/" + categoryEntity.getId();
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute ProductEntity productEntity, @RequestParam("file") MultipartFile image, HttpSession session) throws IOException {
    String imageName=image.isEmpty()?"default.jpg":image.getOriginalFilename();
    productEntity.setImage(imageName);
    productEntity.setDiscount(0);
    productEntity.setDiscountPrice(productEntity.getPrice());
    ProductEntity product = productService.saveProduct(productEntity);
    if(!ObjectUtils.isEmpty(product)){
        File saveFile = new ClassPathResource("static/img").getFile();
        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "productImg" + File.separator + image.getOriginalFilename());

        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        session.setAttribute("succMsg", "Product saved successfully");
        }
    else{
        session.setAttribute("errorMsg", "something went wrong");
        }
    return "redirect:/admin/addProduct";
    }

    @GetMapping("/products")
    public String loadViewProduct(Model m){
        List<ProductEntity> products = productService.getAllProducts();
        m.addAttribute("products", products);
        return "/admin/products";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session){
        Boolean deleteProduct = productService.deleteProduct(id);
        if(deleteProduct){
            session.setAttribute("succMsg", "Deleted Successfully");
        }
        else {
            session.setAttribute("errorMsg", "something went wrong");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable int id, Model m){
        m.addAttribute("product",productService.getProductId(id));
        m.addAttribute("category",categoryService.getAllCategory());

        return "/admin/editProduct";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute ProductEntity productEntity, @RequestParam("file") MultipartFile file, HttpSession session){

        if(productEntity.getDiscount()<0 || productEntity.getDiscount()>100){
            session.setAttribute("errorMsg", "Invalid Discount");
        }
        else {


            ProductEntity product = productService.updateProduct(productEntity, file);
            if (!ObjectUtils.isEmpty(product)) {
                session.setAttribute("succMsg", "Product updated successfully");
            } else {
                session.setAttribute("errorMsg", "something went wrong");
            }
        }
            return "redirect:/admin/editProduct/" + productEntity.getId();

    }

    @GetMapping("/users")
    public String getAllUsers(Model m){
        List<UserDetailsEntity> users=userService.getUsers("ROLE_USER");
        m.addAttribute("users", users);
        return "/admin/users";

    }
    @GetMapping("/updateSts")
    public String updateUserAccountStatus(@RequestParam Boolean status, @RequestParam Integer id, HttpSession session){
        Boolean update=userService.updateUserStatus(id, status);
        if (update) {
            session.setAttribute("succMsg", "Account Status updated");
        }
        else{
            session.setAttribute("errorMsg", "something wrong on server");
        }
        return "redirect:/admin/users";
    }



}
