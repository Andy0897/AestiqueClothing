package com.example.AestiqueClothing.Order;

import com.example.AestiqueClothing.OrderItem.OrderItem;
import com.example.AestiqueClothing.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Pattern(regexp = "^(\\+359|0)8[7-9]\\d{7}$", message = "Въведете валиден български телефонен номер")
    private String phoneNumber;

    @NotEmpty(message = "Градът е задължителен")
    private String city;

    @NotEmpty(message = "Полето е задължително")
    @Pattern(regexp = "\\d{4}", message = "Пощенският код трябва да бъде 4 цифри")
    private String postCode;

    @NotEmpty(message = "Полето е задължително")
    private String street;

    @NotEmpty(message = "Полето е задължително")
    @Pattern(regexp = "\\d{1,3}[A-Za-z]?", message = "Номерът на къщата трябва да бъде 1-3 цифри, може да има буква (напр. 15A, 103B)")
    private String houseNumber;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotEmpty(message = "Полето е задължително")
    private String orderStatus;

    @NotEmpty(message = "Полето е задължително")
    private String paymentOption;

    private LocalDate orderDate;

    private double totalPrice;

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(String paymentOption) {
        this.paymentOption = paymentOption;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}