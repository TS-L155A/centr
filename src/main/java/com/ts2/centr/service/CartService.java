package com.ts2.centr.service;

import com.ts2.centr.exceptions.NotFoundException;
import com.ts2.centr.models.Cart;
import com.ts2.centr.models.CartItem;
import com.ts2.centr.models.CartStatus;
import com.ts2.centr.models.Havka;
import com.ts2.centr.repo.CartItemRepository;
import com.ts2.centr.repo.CartRepository;
import com.ts2.centr.repo.HavkaRepository;
import com.ts2.centr.security.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final HavkaRepository havkaRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            HavkaRepository havkaRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.havkaRepository = havkaRepository;
    }

    public Cart getOrCreateActiveCart (User user) {
        //user
        if (user!=null){
            return cartRepository
                    .findByUserAndStatus(user, CartStatus.ACTIVE)
                    .orElseGet(() -> {
                       Cart cart = new Cart();
                       cart.setUser(user);
                       return cartRepository.save(cart);
                    });
        }

        //guest
        Cart cart = new Cart();
        return cartRepository.save(cart);
    }

    public void addItem(Long havkaId, User user) {
        Havka havka = havkaRepository.findById(havkaId)
                .orElseThrow(() -> new NotFoundException("ЧЕ ЗА ХУЕТА В КОРЗИНЕ"));

        Cart cart = getOrCreateActiveCart(user);

        CartItem cartItem = cartItemRepository.findByCartAndHavka(cart, havka)
                .orElse(null);

        if (cartItem != null){
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItemRepository.save(cartItem);
            return;
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setHavka(havka);
        newCartItem.setQuantity(1);
        newCartItem.setPriceAtAdd(havka.getPrice());

        cartItemRepository.save(newCartItem);
    }

}
