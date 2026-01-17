package com.ts2.centr.controllers;

import com.ts2.centr.models.Cart;
import com.ts2.centr.repo.UserRepository;
import com.ts2.centr.security.User;
import com.ts2.centr.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/blog")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @GetMapping("/cart")
    public String cartMain(Model model, Authentication authentication) {

        if (authentication == null) {
            throw new IllegalStateException("Не авторизован");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Cart cart = cartService.getOrCreateActiveCart(user);

        model.addAttribute("cart", cart);
        model.addAttribute("items", cart.getItems());

        return "cart/main";
    }

    @PostMapping("/cart/add/{havkaId}")
    public String addToCart(@PathVariable Long havkaId, Authentication authentication) {

        if (authentication == null) {
            throw new IllegalStateException("Не авторизован");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        cartService.addItem(havkaId, user);

        return "redirect:/blog/cart";
    }

    // В будущем можно добавить POST /cart/increase/{id}, /cart/decrease/{id}, /cart/remove/{id}
    @PostMapping("/cart/increase/{itemId}")
    public String increaseItemCart(@PathVariable Long itemId, Authentication authentication){
        if (authentication == null) {
            throw new IllegalStateException("Не авторизован");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalStateException("ТАКОГО НЕ ЗнАЕМ"));

        cartService.increaseItem(itemId, user);

        return "redirect:/blog/cart";
    }

    @PostMapping("/cart/decrease/{itemId}")
    public String decreaseItemCart(@PathVariable Long itemId, Authentication authentication){
        if (authentication == null) {
            throw new IllegalStateException("Не авторизован");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalStateException("ТАКОГО НЕ ЗнАЕМ"));

        cartService.decreaseItem(itemId, user);

        return "redirect:/blog/cart";
    }

    @PostMapping("/cart/remove/{itemId}")
    public String deleteItemCart(@PathVariable Long itemId, Authentication authentication){
        if (authentication == null) {
            throw new IllegalStateException("Не авторизован");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalStateException("ТАКОГО НЕ ЗнАЕМ"));

        cartService.deleteItem(itemId, user);

        return "redirect:/blog/cart";
    }
}