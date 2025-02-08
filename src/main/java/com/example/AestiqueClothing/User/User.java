package com.example.AestiqueClothing.User;

import com.example.AestiqueClothing.Cart.Cart;
import com.example.AestiqueClothing.Encryption.EncryptDecryptConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users")
public class User {
    @Column(name = "user_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = EncryptDecryptConverter.class)
    private String firstName;

    @Convert(converter = EncryptDecryptConverter.class)
    private String lastName;

    @Convert(converter = EncryptDecryptConverter.class)
    private String username;

    @Convert(converter = EncryptDecryptConverter.class)
    private String email;

    private String password;

    private Boolean agreeToTerms;

    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'USER'")
    private String role;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean enable;

    @OneToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAgreeToTerms() {
        return agreeToTerms;
    }

    public void setAgreeToTerms(Boolean agreeToTerms) {
        this.agreeToTerms = agreeToTerms;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}