package com.ts2.centr.controllers;

import com.ts2.centr.models.Post;
import com.ts2.centr.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("title", "Главная еблица");
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title","Страница о нас");
        return "about";
    }

}
