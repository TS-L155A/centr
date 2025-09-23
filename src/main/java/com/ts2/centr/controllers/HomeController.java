package com.ts2.centr.controllers;


import com.ts2.centr.repo.HavkaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private HavkaRepository havkaRepository;

    @GetMapping("/")
    public String greeting_redirect(Model model) {
        model.addAttribute("title", "Главная еблица");
        return "redirect:/blog";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title","Страница о нас");
        return "about";
    }

}
