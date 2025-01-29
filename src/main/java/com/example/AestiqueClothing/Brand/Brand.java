package com.example.AestiqueClothing.Brand;

import jakarta.persistence.*;

@Entity
@Table(name = "brands")
public class Brand {
    @Column(name = "brand_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
