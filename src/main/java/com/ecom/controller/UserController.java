package com.ecom.controller;

import com.ecom.model.Category;
import com.ecom.model.UserDetails;
import com.ecom.service.CategoryService;
import com.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;



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

    @GetMapping("/")
    public String home(){
        return "index";

    }



}
