package com.ecom.service;

import com.ecom.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ProductService {

    public Product saveProduct(Product product);

     public List<Product> getALLProducts();

     public boolean deleteProduct(Integer id);

     public Product getProductById(Integer id);

     public Product updateProduct(Product product, MultipartFile file);
}
