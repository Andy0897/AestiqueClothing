package com.example.AestiqueClothing.User;

import com.example.AestiqueClothing.Cart.Cart;
import com.example.AestiqueClothing.Cart.CartRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
public class
UserService {
    private BCryptPasswordEncoder encoder;
    private UserRepository userRepository;
    private CartRepository cartRepository;

    public UserService(BCryptPasswordEncoder encoder, UserRepository userRepository, CartRepository cartRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public String submitUser(User user, BindingResult bindingResult, Model model) {
        System.out.println(bindingResult.hasFieldErrors("agreeToTerms"));
        if(bindingResult.hasErrors() || checkIfExistsUserByUsername(user.getUsername()) || checkIfExistsUserByEmail(user.getEmail())) {
            model.addAttribute("user", user);
            model.addAttribute("existsUserByUsername", checkIfExistsUserByUsername(user.getUsername()));
            model.addAttribute("existsUserByEmail", checkIfExistsUserByEmail(user.getEmail()));
            return "sign-up";
        }
        user.setEnable(true);
        user.setRole("USER");
        user.setPassword(encoder.encode(user.getPassword()));
        Cart cart = new Cart();
        user.setCart(cart);

        cartRepository.save(cart);
        userRepository.save(user);
        return "redirect:/sign-in";
    }

    public boolean checkIfExistsUserByUsername(String username) {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            return false;
        }
        return true;
    }

    public boolean checkIfExistsUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            return false;
        }
        return true;
    }
}
