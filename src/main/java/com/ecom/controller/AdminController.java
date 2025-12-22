package com.ecom.controller;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.UserDetails;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
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

@Controller
@RequestMapping("/admin")
public class AdminController {


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



    @GetMapping("/")
    public String index(){
        return "admin/index";
    }

    @GetMapping("/loadAddProduct")
    public String loadAddProduct(Model m){
        List<Category> allCategory = categoryService.getAllCategory();
        m.addAttribute("categories",allCategory);
        return "admin/add_product";
    }


    @GetMapping("/category")
    public String category(Model m){
        m.addAttribute("categorys",categoryService.getAllCategory());
        return "admin/category";
    }


    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {

        String imageName = file!=null ? file.getOriginalFilename(): "default.jpg";

        category.setImageName(imageName);

        boolean existCategory = categoryService.existCategory(category.getName());

        if(existCategory){
            session.setAttribute("errorMsg","Category Name Already Exists");
        }else{
            Category saveCategory = categoryService.saveCategory(category);
            if(ObjectUtils.isEmpty(saveCategory)){

                session.setAttribute("errorMsg","Not saved ! internal server error");

            }else{

                File saveFile = new ClassPathResource("static/image").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator + file.getOriginalFilename());

//                System.out.println(path);
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

                session.setAttribute("succMsg","Save successfully");
            }
        }



        return "redirect:/admin/category";

    }



    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id,HttpSession session){

        boolean b = categoryService.deleteCategory(id);
        if(b){
            session.setAttribute("succMsg","Category Delete");

        }else {
            session.setAttribute("errorMsg","Something Wrong On Server");
        }
        return "redirect:/admin/category";
    }


    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id, Model m){

        m.addAttribute("category",categoryService.getCategoryById(id));

            return "/admin/edit_category";
    }

    @PostMapping("/updateCategory")
        public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,HttpSession session) throws IOException {

        Category oldCategory = categoryService.getCategoryById(category.getId());
        String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

        if(!ObjectUtils.isEmpty(category)){
            oldCategory.setName(category.getName());
            oldCategory.setActive(category.isActive());
            oldCategory.setImageName(imageName);

        }

        Category updateCategory = categoryService.saveCategory(oldCategory);

        if(!ObjectUtils.isEmpty(updateCategory)){

            if (!file.isEmpty()){

                File saveFile = new ClassPathResource("static/image").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator + file.getOriginalFilename());

//                System.out.println(path);
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            }

        session.setAttribute("succMsg","Category update successfully");
        }else {
            session.setAttribute("errorMsg","Something Wrong On Server");
        }
        return "redirect:/admin/loadEditCategory/"+ category.getId();


        }

        @PostMapping("/saveProduct")
        public String saveProduct(@ModelAttribute Product product,@RequestParam("file") MultipartFile image,HttpSession session) throws IOException {

            String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();
            product.setImage(imageName);
            product.setDiscount(0);
            product.setDiscountPrice(product.getPrice());

            Product saveProduct = productService.saveProduct(product);


            if (!ObjectUtils.isEmpty(saveProduct)){

                File saveFile = new ClassPathResource("static/image").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
                        + image.getOriginalFilename());

//                System.out.println(path);
                Files.copy(image.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

                session.setAttribute("succMsg","Product Save Successfully");
            }else {
                session.setAttribute("errorMsg","Something Wrong On Server");
            }


            return "redirect:/admin/loadAddProduct";
        }

        @GetMapping("/products")
        public String loadViewProduct(Model m){
        m.addAttribute("products",productService.getALLProducts());
        return "/admin/products";
        }



    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id,HttpSession session){
        boolean dp = productService.deleteProduct(id);
        if(dp){
        session.setAttribute("succMsg","Product Delete Success");
        }else {
        session.setAttribute("errorMsg","Something Wrong On Server");
        }

        return "redirect:/admin/products";
    }


    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable int id,Model m){
        m.addAttribute("product",productService.getProductById(id));
        m.addAttribute("categories",categoryService.getAllCategory());
        return "/admin/edit_product";
    }


    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product,@RequestParam("file") MultipartFile image ,HttpSession session,Model m){

        if(product.getDiscount()<0 || product.getDiscount()>100){
            session.setAttribute("errorMsg","Invalid Discount");

        }

        else {

            Product updateProduct = productService.updateProduct(product, image);
            if (!ObjectUtils.isEmpty(updateProduct)) {
                session.setAttribute("succMsg", "Product update success");
            } else {
                session.setAttribute("errorMsg", "Something Wrong on Server");
            }
        }

        return "redirect:/admin/editProduct/"+ product.getId();
    }


    @GetMapping("/users")
    public String getAllUsers(Model m){

        List<UserDetails> users = userService.getUsers("ROLE_USER");
        m.addAttribute("users",users);
        return "/admin/users";
    }





}



