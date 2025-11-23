package com.ecom.controller;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.UserDetails;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;


    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/register")
    public String register(){
        return "register";
    }

    @RequestMapping("/products")
    public String products(Model m, @RequestParam(value = "category",defaultValue = "")String category){
//        System.out.println("category"+category);
        List<Category> categories = categoryService.getAllActiveCategory();
        List<Product> products = productService.getALLActiveProducts(category);
        m.addAttribute("categories",categories);
        m.addAttribute("products",products);
        m.addAttribute("paramValue",category);
        return "product";
    }

    @RequestMapping("/product/{id}")
    public String product(@PathVariable int id,Model m){
        Product productDetails = productService.getProductById(id);
        m.addAttribute("product", productDetails);

        return "view_product";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute UserDetails user,@RequestParam("img") MultipartFile file){

        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfileImage(imageName);

        UserDetails saveUser  =userService.saveUser(user);

        if (!ObjectUtils.isEmpty(saveUser)){




        }


        return "redirect:/register";
    }

}
