package com.ts2.centr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("title", "Главная еблица");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title","Страница о нас");
        return "about";
    }

}
