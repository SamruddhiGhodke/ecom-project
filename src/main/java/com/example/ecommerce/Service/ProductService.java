package com.example.ecommerce.Service;

import com.example.ecommerce.Entity.ProductEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ProductService {

    public ProductEntity saveProduct(ProductEntity productEntity);

    public List<ProductEntity> getAllProducts();

    public Boolean deleteProduct(Integer id);

    public ProductEntity getProductId(Integer id);

    public ProductEntity updateProduct(ProductEntity product, MultipartFile file);

    public List<ProductEntity> getAllActiveProducts(String category);
}
