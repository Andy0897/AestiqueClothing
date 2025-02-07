package com.example.AestiqueClothing.Order;

import com.example.AestiqueClothing.ImageEncoder;
import com.example.AestiqueClothing.OrderItem.OrderItem;
import com.example.AestiqueClothing.User.User;
import com.example.AestiqueClothing.User.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private OrderRepository orderRepository;
    private OrderService orderService;
    private UserRepository userRepository;

    @Value("${stripe.public.key}")
    private String stripePublicKey;

    public OrderController(OrderRepository orderRepository, OrderService orderService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @GetMapping("/checkout")
    public String getCheckout(Principal principal, Model model) {
        User user = userRepository.getUserByUsername(principal.getName());
        List<OrderItem> orderItems = user.getCart().getItems();
        if(orderItems.isEmpty()) {
            return "redirect:/cart";
        }
        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(orderItems);
        order.setTotalPrice(orderItems.stream().mapToDouble(OrderItem::getTotalPrice).sum());
        order.setOrderStatus("В изчакване");

        model.addAttribute("order", order);
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("amount", order.getTotalPrice());
        model.addAttribute("paymentFailure", false);
        model.addAttribute("currency", "BGN");
        return "order/checkout";
    }

    @PostMapping("/submit")
    public String getSubmitOrder(@RequestParam(value = "stripeToken", required = false) String stripeToken, @ModelAttribute @Valid Order order, BindingResult bindingResult, Principal principal, Model model) {
        if(stripeToken == null) {
            order.setPaymentOption("Наложен платеж");
        }
        else {
            order.setPaymentOption("Карта");
        }
        System.out.println(stripeToken);
        return orderService.submitOrder(stripeToken, order, bindingResult, principal, model);
    }

    @GetMapping("/thank-you")
    public String getThankYou() {
        return "order/thank-you";
    }

    @GetMapping
    public String getShowAllOrders(Model model) {
        List<Order> orders = (List<Order>)orderRepository.findAll();
        Collections.reverse(orders);
        model.addAttribute("orders", orders);
        return "order/show-all";
    }

    @GetMapping("/my-orders")
    public String getShowUserOrders(Principal principal, Model model) {
        User user = userRepository.getUserByUsername(principal.getName());
        List<Order> orders = orderRepository.findAllByUser(user);
        Collections.reverse(orders);
        model.addAttribute("orders", orders);
        return "order/show-user-orders";
    }

    @GetMapping("/details/{orderId}")
    public String getShowOrderDetails(@PathVariable("orderId") Long orderId, Model model) {
        Order order = orderRepository.findById(orderId).get();
        model.addAttribute("order", order);
        model.addAttribute("encoder", new ImageEncoder());
        return "order/show-single-details";
    }

    @PostMapping("/update-status/{orderId}")
    public String getSubmitOrderStatus(@PathVariable("orderId") Long orderId,
                                       @RequestParam("status") String status) {
        return orderService.submitUpdateStatus(orderId, status);
    }
}