package com.example.AestiqueClothing.User;

import com.example.AestiqueClothing.Cart.Cart;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    BCryptPasswordEncoder passwordEncoder;
    public UserMapper(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setAgreeToTerms(userDTO.getAgreeToTerms());
        user.setEnable(true);
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Cart cart = new Cart();
        user.setCart(cart);
        return user;
    }
}
