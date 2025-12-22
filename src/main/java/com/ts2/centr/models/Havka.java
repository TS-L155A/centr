package com.ts2.centr.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Random;

@Entity
public class Havka {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title, addits;
    private String unit;
    private String imagePath;
    private int quantity;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Quality quality;

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

    public String getAddits() {
        return addits;
    }

    public void setAddits(String addits) {
        this.addits = addits;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public Havka(String title, String addits, String unit, String imagePath, int quantity) {
        this.title = title;
        this.addits = addits;
        this.unit = unit;
        this.imagePath = imagePath;
        this.quantity = quantity;
    }

    public Havka(String title, String addits, String unit, String imagePath, int quantity, BigDecimal price) {
        this.title = title;
        this.addits = addits;
        this.unit = unit;
        this.imagePath = imagePath;
        this.quantity = quantity;
        this.price = price;
    }

    public Havka() {
    }

    private Quality randomQuality() {
        Quality[] values = Quality.values();
        return values[new Random().nextInt(values.length)];
    }

    @PrePersist
    public void prePersist() {
        if (quality == null) {
            this.quality = randomQuality();
        }
    }
}
