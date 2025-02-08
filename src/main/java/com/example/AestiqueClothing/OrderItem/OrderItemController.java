package com.example.AestiqueClothing.OrderItem;

import jakarta.validation.Valid;
import org.hibernate.internal.util.StringHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/order-items")
public class OrderItemController {
    private OrderItemService orderItemService;
    private OrderItemRepository orderItemRepository;

    public OrderItemController(OrderItemService orderItemService, OrderItemRepository orderItemRepository) {
        this.orderItemService = orderItemService;
        this.orderItemRepository = orderItemRepository;
    }

    @PostMapping("/submit/{productId}")
    public String getSubmitOrderItem(@PathVariable("productId") Long productId, @RequestParam("size") Long productSizeId, Model model, Principal principal) {
        return orderItemService.submitOrderItem(productId, productSizeId, model, principal);
    }
}
