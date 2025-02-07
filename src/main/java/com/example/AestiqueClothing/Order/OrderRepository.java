package com.example.AestiqueClothing.Order;

import com.example.AestiqueClothing.User.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    public List<Order> findAllByUser(User user);
}
