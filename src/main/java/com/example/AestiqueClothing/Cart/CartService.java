package com.example.AestiqueClothing.Cart;

import com.example.AestiqueClothing.OrderItem.OrderItem;
import com.example.AestiqueClothing.OrderItem.OrderItemRepository;
import com.example.AestiqueClothing.User.User;
import com.example.AestiqueClothing.User.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CartService {
    private CartRepository cartRepository;
    private UserRepository userRepository;
    private OrderItemRepository orderItemRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, OrderItemRepository orderItemRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public String submitRemoveItemFromCart(Long orderItemId, Principal principal) {
        User user = userRepository.getUserByUsername(principal.getName());
        Cart cart = user.getCart();
        OrderItem orderItem = orderItemRepository.findById(orderItemId).get();
        cart.removeItem(orderItem);
        cartRepository.save(cart);
        return "redirect:/cart";
    }
}
