package com.example.AestiqueClothing.Cart;

import com.example.AestiqueClothing.ImageEncoder;
import com.example.AestiqueClothing.OrderItem.OrderItem;
import com.example.AestiqueClothing.OrderItem.OrderItemRepository;
import com.example.AestiqueClothing.User.User;
import com.example.AestiqueClothing.User.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/cart")
public class CartController {
    private UserRepository userRepository;
    private CartService cartService;

    public CartController(UserRepository userRepository, CartService cartService) {
        this.userRepository = userRepository;
        this.cartService = cartService;
    }

    @GetMapping
    public String getShowCart(Model model, Principal principal) {
        User user = userRepository.getUserByUsername(principal.getName());
        model.addAttribute("cart", user.getCart());
        model.addAttribute("encoder", new ImageEncoder());
        return "cart/show";
    }

    @PostMapping("/remove-item/{orderItemId}")
    public String getSubmitRemoveItem(@PathVariable("orderItemId") Long orderItemId, Principal principal) {
        return cartService.submitRemoveItemFromCart(orderItemId, principal);
    }
}
