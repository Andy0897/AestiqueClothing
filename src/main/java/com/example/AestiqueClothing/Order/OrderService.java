package com.example.AestiqueClothing.Order;

import com.example.AestiqueClothing.Cart.Cart;
import com.example.AestiqueClothing.Cart.CartRepository;
import com.example.AestiqueClothing.Payments.StripeService;
import com.example.AestiqueClothing.User.UserRepository;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.ArrayList;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private StripeService stripeService;
    private UserRepository userRepository;
    private CartRepository cartRepository;

    @Value("${stripe.public.key}")
    private String stripePublicKey;

    public OrderService(OrderRepository orderRepository, StripeService stripeService, UserRepository userRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.stripeService = stripeService;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public String submitOrder(String stripeToken, Order order, BindingResult bindingResult, Principal principal, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("order", order);
            model.addAttribute("stripePublicKey", stripePublicKey);
            model.addAttribute("amount", order.getTotalPrice());
            model.addAttribute("paymentFailure", false);
            model.addAttribute("currency", "BGN");
            return "order/checkout";
        }
        if(order.getPaymentOption().equals("Карта")) {
            try {
                stripeService.chargeCreditCard(stripeToken, order.getTotalPrice());
            } catch (StripeException e) {
                model.addAttribute("order", order);
                model.addAttribute("stripePublicKey", stripePublicKey);
                model.addAttribute("amount", order.getTotalPrice());
                model.addAttribute("paymentFailure", true);
                model.addAttribute("paymentFailure", true);
                return "order/checkout";
            }
        }
        Cart cart = userRepository.getUserByUsername(principal.getName()).getCart();
        cart.setItems(new ArrayList<>());

        cartRepository.save(cart);
        orderRepository.save(order);
        return "redirect:/cart";
    }

    public String submitUpdateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).get();
        order.setOrderStatus(status);
        orderRepository.save(order);
        return "redirect:/orders/details/" + orderId;
    }
}
