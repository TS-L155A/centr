package com.ts2.centr.repo;

import com.ts2.centr.models.Cart;
import com.ts2.centr.models.CartItem;
import com.ts2.centr.models.Havka;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndHavka(Cart cart, Havka havka);
}
