package com.example.AestiqueClothing.OrderItem;

import com.example.AestiqueClothing.Cart.Cart;
import com.example.AestiqueClothing.Cart.CartRepository;
import com.example.AestiqueClothing.Product.Product;
import com.example.AestiqueClothing.Product.ProductRepository;
import com.example.AestiqueClothing.Size.ProductSize;
import com.example.AestiqueClothing.Size.ProductSizeRepository;
import com.example.AestiqueClothing.User.UserRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.Objects;

@Service
public class OrderItemService {
    private OrderItemRepository orderItemRepository;
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private ProductSizeRepository productSizeRepository;
    private UserRepository userRepository;

    public OrderItemService(OrderItemRepository orderItemRepository, CartRepository cartRepository, ProductRepository productRepository, ProductSizeRepository productSizeRepository, UserRepository userRepository) {
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.productSizeRepository = productSizeRepository;
        this.userRepository = userRepository;
    }

    public String submitOrderItem(Long productId, Long productSizeId, Model model, Principal principal) {
        Product product = productRepository.findById(productId).get();
        ProductSize productSize = productSizeRepository.findById(productSizeId).get();
        Cart cart = userRepository.getUserByUsername(principal.getName()).getCart();

        if(orderItemRepository.findOrderItemInCartByProductIdAndSize(cart.getId(), product.getId(), productSize.getId()) != null &&
                Objects.equals(orderItemRepository.findOrderItemInCartByProductIdAndSize(cart.getId(), product.getId(), productSize.getId()).getProductSize().getId(), productSizeId)) {
            OrderItem orderItem = orderItemRepository.findOrderItemInCartByProductIdAndSize(cart.getId(), product.getId(), productSize.getId());
            orderItem.setQuantity(orderItem.getQuantity() + 1);
            orderItem.setTotalPrice(orderItem.getTotalPrice() + product.getPrice());
            orderItemRepository.save(orderItem);
        }
        else {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setProductSize(productSize);
            orderItem.setQuantity(1);
            orderItem.setTotalPrice(product.getPrice());
            cart.addItem(orderItem);
            cartRepository.save(cart);
        }

        productSize.setQuantity(productSize.getQuantity() - 1);
        productSizeRepository.save(productSize);
        return "redirect:/products/" + productId;
    }
}