package com.example.AestiqueClothing.Product;

import com.example.AestiqueClothing.Size.ProductSize;
import com.example.AestiqueClothing.Brand.Brand;
import com.example.AestiqueClothing.Category.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.*;


@Entity
@Table(name = "products")
public class Product {
    @Column(name = "product_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 6, message = "Заглавието трябва да съдържа поне 6 символа")
    @Size(max = 50, message = "Заглавието не трябва да надвишава 50 символа.")
    private String title;

    @Size(min = 6, message = "Описанието трябва да съдържа поне 6 символа")
    @Size(max = 500, message = "Описание не трябва да надвишава 500 символа.")
    private String description;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    @NotNull(message = "Полето е задължително")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "Полето е задължително")
    private Category category;

    @NotEmpty(message = "Полето е задължително")
    private String material;

    @NotEmpty(message = "Полето е задължително")
    private String color;

    @NotEmpty(message = "Полето е задължително")
    private String gender;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductSize> sizes = new ArrayList<>();

    @ElementCollection
    @Lob
    private List<byte[]> images = new ArrayList<>();

    private Integer mainImageIndex;

    @Min(value = 1, message = "Цената трябва да бъде поне 1 лв.")
    private double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<ProductSize> getSizes() {
        return sizes;
    }

    public void setSizes(List<ProductSize> sizes) {
        this.sizes = sizes;
    }

    public List<byte[]> getImages() {
        return images;
    }

    public void setImages(List<byte[]> images) {
        this.images = images;
    }

    public Integer getMainImageIndex() {
        return mainImageIndex;
    }

    public void setMainImageIndex(Integer mainImageIndex) {
        this.mainImageIndex = mainImageIndex;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void addSize(ProductSize size) {
        this.sizes.add(size);
    }
}
