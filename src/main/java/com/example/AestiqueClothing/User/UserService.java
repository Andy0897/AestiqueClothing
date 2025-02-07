package com.example.AestiqueClothing.User;

import com.example.AestiqueClothing.Cart.Cart;
import com.example.AestiqueClothing.Cart.CartRepository;
import com.example.AestiqueClothing.Order.Order;
import com.example.AestiqueClothing.Order.OrderRepository;
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
    private OrderRepository orderRepository;

    public UserService(BCryptPasswordEncoder encoder, UserRepository userRepository, CartRepository cartRepository, OrderItemRepository orderItemRepository, ProductSizeRepository productSizeRepository, OrderRepository orderRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderItemRepository = orderItemRepository;
        this.productSizeRepository = productSizeRepository;
        this.orderRepository = orderRepository;
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
        Cart cart = user.getCart();

        System.out.println("Delete Cart: ");

        if (cart != null && !cart.getItems().isEmpty()) {
            for (OrderItem orderItem : cart.getItems()) {
                System.out.println(orderItem != null);
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
            orderItemRepository.deleteAll(cart.getItems());

            user.setCart(null);
            userRepository.save(user);

            cartRepository.delete(cart);
        }

        if(!orderRepository.findAllByUser(user).isEmpty()) {
            List<Order> userOrders = orderRepository.findAllByUser(user);
            for (Order order : userOrders) {
                System.out.println(order.getOrderItems().stream().anyMatch(Objects::isNull));
                orderItemRepository.deleteAll(order.getOrderItems());
                order.setOrderItems(null);
                orderRepository.save(order);
            }
            orderRepository.deleteAll(userOrders);
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
