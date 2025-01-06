package com.example.ecommerce.Service.Impl;

import com.example.ecommerce.Entity.ProductEntity;
import com.example.ecommerce.Repository.ProductRepo;
import com.example.ecommerce.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepo productRepo;

    @Override
    public ProductEntity saveProduct(ProductEntity productEntity) {
        return productRepo.save(productEntity);
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepo.findAll();
    }

    @Override
    public Boolean deleteProduct(Integer productId) {
        ProductEntity product = productRepo.findById(productId).orElse(null);
        if (!ObjectUtils.isEmpty(product)) {
            productRepo.delete(product);
            return true;
        }
        return false;
    }

    @Override
    public ProductEntity getProductId(Integer id) {
        ProductEntity product = productRepo.findById(id).orElse(null);
        return product;
    }

    @Override
    public ProductEntity updateProduct(ProductEntity product, MultipartFile file) {
        ProductEntity dbProduct = getProductId(product.getId());
        String imageName = file.isEmpty() ? dbProduct.getImage() : file.getOriginalFilename();
        dbProduct.setTitle(product.getTitle());
        dbProduct.setDescription(product.getDescription());
        dbProduct.setCategory(product.getCategory());
        dbProduct.setPrice(product.getPrice());
        dbProduct.setStock(product.getStock());
        dbProduct.setImage(imageName);
        dbProduct.setIsActive(product.getIsActive());

        dbProduct.setDiscount(product.getDiscount());
        //5=100*(5/100); 100-5=95
        Double discount= product.getPrice()*((product.getDiscount()/100.0));
        Double discountPrice= product.getPrice()-discount;
        dbProduct.setDiscountPrice(discountPrice);

        ProductEntity updateProduct = productRepo.save(dbProduct);

        if (!ObjectUtils.isEmpty(updateProduct)) {
            if (!file.isEmpty()) {
                try {
                    File saveFile = new ClassPathResource("static/img").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "productImg" + File.separator + file.getOriginalFilename());
                    //System.out.println(path);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return product;

            }

        return null;
    }

    @Override
    public List<ProductEntity> getAllActiveProducts(String category) {
        List<ProductEntity> product= null;
        if(ObjectUtils.isEmpty(category)){
            product=productRepo.findByIsActiveTrue();
        }
        else{
            product=productRepo.findByCategory(category);
        }

        return product;
    }

}



