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

import java.math.BigDecimal;

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
        //гость
        if (user == null) {
            throw new IllegalStateException("Гостевая корзина пока не реализована");
        }

        //юз
        return cartRepository
                .findByUserAndStatus(user, CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setStatus(CartStatus.ACTIVE);
                    return cartRepository.save(cart);
                });
    }

    public void addItem(Long havkaId, User user) {

        if (user == null) {
            throw new IllegalStateException("Добавление в корзину только для авторизованных");
        }

        Havka havka = havkaRepository.findById(havkaId)
                .orElseThrow(() -> new NotFoundException("ЧЕ ЗА ХУЕТА В КОРЗИНЕ"));

        Cart cart = getOrCreateActiveCart(user);

//        if (cart.getId() == null) {
//            cart = cartRepository.save(cart); // Cart точно с ID
//        }

        CartItem cartItem = cartItemRepository.findByCartAndHavka(cart, havka)
                .orElse(null);

        if (cartItem != null){
            cartItem.setQuantity(cartItem.getQuantity() + 1);
//            cartItemRepository.save(cartItem);
            return;
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setHavka(havka);
        newCartItem.setQuantity(1);

        //обьясняем что бесценно и бесплатно это норм
        BigDecimal price = havka.getPrice();
        if (price == null){
            price = BigDecimal.ZERO;
        }
        newCartItem.setPriceAtAdd(price);
        newCartItem.setPriceCurrent(price);
        CartItem saved = cartItemRepository.save(newCartItem);

        // 🔥 ВОТ ЭТО БЫЛО ПРОПУЩЕНО
        cart.getItems().add(newCartItem);

        cart.getItems().add(saved); // теперь cart.getItems() вернёт новый элемент

        System.out.println("Cart ID: " + cart.getId());
        System.out.println("Cart items count: " + cart.getItems().size());
        System.out.println("Saved CartItem ID: " + saved.getId());
    }



}
