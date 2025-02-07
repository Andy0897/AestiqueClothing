package com.example.AestiqueClothing.User;

import com.example.AestiqueClothing.Cart.Cart;
import com.example.AestiqueClothing.Cart.CartRepository;
import com.example.AestiqueClothing.OrderItem.OrderItem;
import com.example.AestiqueClothing.OrderItem.OrderItemRepository;
import com.example.AestiqueClothing.Product.Product;
import com.example.AestiqueClothing.Product.ProductRepository;
import com.example.AestiqueClothing.Size.ProductSize;
import com.example.AestiqueClothing.Size.ProductSizeRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import java.util.*;

import java.security.Principal;

@Service
public class
UserService {
    private BCryptPasswordEncoder encoder;
    private UserRepository userRepository;
    private CartRepository cartRepository;
    private OrderItemRepository orderItemRepository;
    private ProductSizeRepository productSizeRepository;

    public UserService(BCryptPasswordEncoder encoder, UserRepository userRepository, CartRepository cartRepository, OrderItemRepository orderItemRepository, ProductSizeRepository productSizeRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderItemRepository = orderItemRepository;
        this.productSizeRepository = productSizeRepository;
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

    @Transactional
    public String submitDeleteUser(Principal principal) {
        User user = userRepository.getUserByUsername(principal.getName());

        if (user.getCart() != null && !user.getCart().getItems().isEmpty()) {
            for (OrderItem orderItem : user.getCart().getItems()) {
                Product product = orderItem.getProduct();
                ProductSize productSize = ((List<ProductSize>)productSizeRepository.findAll()).stream()
                        .filter(size -> size.getProduct().getId().equals(product.getId())
                                && size.getId().equals(orderItem.getProductSize().getId()))
                        .findFirst()
                        .orElse(null);

                if (productSize != null) {
                    productSize.setQuantity(productSize.getQuantity() + orderItem.getQuantity());
                    productSizeRepository.save(productSize);
                }
            }

            orderItemRepository.deleteAll(user.getCart().getItems());


            cartRepository.delete(user.getCart());
        }

        userRepository.delete(user);

        return "redirect:/sign-in";
    }


    private boolean checkIfExistsUserByUsername(String username) {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            return false;
        }
        return true;
    }

    private boolean checkIfExistsUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            return false;
        }
        return true;
    }
}
