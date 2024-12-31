package com.example.ecommerce.Controller;
//import com.example.ecommerce.Entity.CategoryEntity;
import com.example.ecommerce.Entity.CategoryEntity;
//import com.example.ecommerce.Service.CategoryService;
import com.example.ecommerce.Service.CategoryService;
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
                System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                session.setAttribute("succMsg","Saved successfully");
            }
        }


        return "redirect:/admin/category";
    }

}
