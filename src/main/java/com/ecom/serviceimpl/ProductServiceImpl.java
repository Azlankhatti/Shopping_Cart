package com.ecom.serviceimpl;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.repository.ProductRepository;
import com.ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getALLProducts() {
        return productRepository.findAll();
    }

    @Override
    public boolean deleteProduct(Integer id) {
        Product product = productRepository.findById(id).orElse(null);

        if (!ObjectUtils.isEmpty(product)){
            productRepository.delete(product);
            return true;
        }
        return false;
    }

    @Override
    public Product getProductById(Integer id) {
        Product product = productRepository.findById(id).orElse(null);

        return product;
    }

    @Override
    public Product updateProduct(Product product, MultipartFile image) {

        Product dbproduct = productRepository.getProductById(product.getId());

        String imageName = image.isEmpty() ? dbproduct.getImage() : image.getOriginalFilename();

        dbproduct.setTitle(product.getTitle());
        dbproduct.setDescription(product.getDescription());
        dbproduct.setCategory(product.getCategory());
        dbproduct.setPrice(product.getPrice());
        dbproduct.setStock(product.getStock());

        Product save = productRepository.save(dbproduct);
        if(!ObjectUtils.isEmpty(save)){
            if(!image.isEmpty()){


            }
        }

        return null;
    }
}
