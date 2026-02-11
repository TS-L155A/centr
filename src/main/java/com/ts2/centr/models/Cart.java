package com.ts2.centr.models;

import com.ts2.centr.security.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;


@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = true)
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = CartStatus.ACTIVE;
        }
    }

    public BigDecimal getTotal() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return items.stream()
                .map(item -> {
                    BigDecimal price = item.getPriceCurrent() != null
                            ? item.getPriceCurrent()
                            : item.getPriceAtAdd();

                    return price.multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public CartStatus getStatus() {
        return status;
    }

    public void setStatus(CartStatus status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> items = new ArrayList<>();

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
