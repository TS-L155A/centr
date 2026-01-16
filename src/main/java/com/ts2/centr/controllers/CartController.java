package com.ts2.centr.controllers;

import com.ts2.centr.models.Cart;
import com.ts2.centr.models.CartItem;
import com.ts2.centr.security.User;
import com.ts2.centr.service.CartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/blog")
public class CartController {
    private final CartService cartService;

    public CartController (CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String cartMain(Model model, @AuthenticationPrincipal User user){
        //пик активной корзины
        Cart cart = cartService.getOrCreateActiveCart(user);
        List<CartItem> cartItems = cart.getItems();

        //накинуть насвай в модель
        model.addAttribute(cart);
        model.addAttribute(cartItems);

        //вазвращаем шаблон
        return "cart/main";
    }
}
