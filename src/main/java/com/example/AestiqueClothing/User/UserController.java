package com.example.AestiqueClothing.User;

import com.example.AestiqueClothing.ImageEncoder;
import com.example.AestiqueClothing.Product.Product;
import com.example.AestiqueClothing.Product.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
    private UserService userService;
    private UserRepository userRepository;
    private ProductRepository productRepository;

    public UserController(UserService userService, UserRepository userRepository, ProductRepository productRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @GetMapping({"/", "/home"})
    public String getHome(Model model) {
        List<Product> menProducts = ((List<Product>) productRepository.findAll()).stream().filter(product -> product.getGender().equals("Мъжки")).toList();
        List<Product> womenProducts = ((List<Product>) productRepository.findAll()).stream().filter(product -> product.getGender().equals("Дамски")).toList();
        if(menProducts.size() > 3) {
            menProducts = menProducts.subList(0, 2);
        }
        if(womenProducts.size() > 3) {
            womenProducts = womenProducts.subList(0, 2);
        }
        model.addAttribute("menProducts", menProducts);
        model.addAttribute("womenProducts", womenProducts);
        model.addAttribute("encoder", new ImageEncoder());
        return "home";
    }

    @GetMapping("/sign-in")
    public String getSignIn(Principal principal) {
        if (principal != null) {
            return "redirect:/access-denied";
        }
        return "sign-in";
    }

    @GetMapping("/sign-up")
    public String getSignUp(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("userDTO", userDTO);
        return "sign-up";
    }

    @PostMapping("/submit")
    public String submitUser(@ModelAttribute @Valid UserDTO userDTO, BindingResult bindingResult, Model model) {
        return userService.submitUser(userDTO, bindingResult, model);
    }

    @PostMapping("/submit-delete")
    public String getSubmitDeleteProfile(Principal principal) {
        return userService.submitDeleteUser(principal);
    }

    @GetMapping("/access-denied")
    public String getAccessDenied() {
        return "accessDenied";
    }

    @GetMapping("/logout")
    public String fetchSignoutSite(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/sign-in?logout";
    }
}