package com.ts2.centr.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Cart cart;

    @ManyToOne(optional = false)
    private Havka havka;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal priceAtAdd;

    @Column(nullable = false)
    private BigDecimal priceCurrent;

    @PrePersist
    public void prePersist() {
        if (quantity == null || quantity < 1){
            quantity = 1;
        }
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Havka getHavka() {
        return havka;
    }

    public void setHavka(Havka havka) {
        this.havka = havka;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceAtAdd() {
        return priceAtAdd;
    }

    public void setPriceAtAdd(BigDecimal priceAtAdd) {
        this.priceAtAdd = priceAtAdd;
    }

    public BigDecimal getPriceCurrent() {
        return priceCurrent;
    }

    public void setPriceCurrent(BigDecimal priceCurrent) {
        this.priceCurrent = priceCurrent;
    }
}
