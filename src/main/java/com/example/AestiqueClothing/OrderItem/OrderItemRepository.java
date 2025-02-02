package com.example.AestiqueClothing.OrderItem;

import org.hibernate.query.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
    @Query(value = "SELECT oi.* FROM order_items oi " +
            "JOIN products p ON p.product_id = oi.product_id " +
            "JOIN carts_items ci ON ci.items_order_item_id = oi.order_item_id " +
            "JOIN carts c ON c.cart_id = ci.cart_cart_id " +
            "WHERE c.cart_id = :cartId AND p.product_id = :productId AND oi.size_id = :sizeId",
            nativeQuery = true)
    public OrderItem findOrderItemInCartByProductIdAndSize(@Param("cartId") Long cartId, @Param("productId") Long productId, @Param("sizeId") Long sizeId);
}
