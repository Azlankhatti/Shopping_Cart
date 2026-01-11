package com.ecom.controller;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.UserDetails;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;
import com.ecom.util.CommonUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.UUID;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void getUserDetails(Principal p, Model m){

        if(p!=null){
            String email = p.getName();
            UserDetails userDtls = userService.getUserByEmail(email);
            m.addAttribute("user",userDtls);
        }

        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        m.addAttribute("categorys",allActiveCategory);
    }

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/signin")
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
    public String saveUser(@ModelAttribute UserDetails user, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {

        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfileImage(imageName);

        UserDetails saveUser  =userService.saveUser(user);

        if (!ObjectUtils.isEmpty(saveUser)){
            if(!file.isEmpty()){

                File saveFile = new ClassPathResource("static/image").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
                        + file.getOriginalFilename());

                System.out.println(path);
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                session.setAttribute("succMsg","Register Successfully");
            }else {
                session.setAttribute("errorMsg","Something Wrong On Server");
            }


            }

        return "redirect:/register";
    }

    //Forget password

    @GetMapping("/forgot-password")
    public String showForgotPassword(){
        return "forgot_password.html";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, HttpSession session, HttpServletRequest request){

        UserDetails userByEmail = userService.getUserByEmail(email);

        if (ObjectUtils.isEmpty(userByEmail)){
            session.setAttribute("errorMsg","Invalid email");

        }else{

            String resetToken = UUID.randomUUID().toString();
            userService.updateUserResetToken(email,resetToken);

            String url= CommonUtil.generateUrl(request);


            boolean sendMail = CommonUtil.sendMail();
            if (sendMail){
                session.setAttribute("succMsg","please check your email..password Reset link sent");
            }else{
                session.setAttribute("errorMsg","Something wrong on server ! Email not send");
            }
        }
        return "redirect:/forgot-password";
    }




    @GetMapping("/reset-password")
    public String showResetPassword(){
        return "reset_password.html";
    }



}
