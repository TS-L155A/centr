package com.ts2.centr.controllers;

import com.ts2.centr.models.Havka;
import com.ts2.centr.repo.HavkaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    private HavkaRepository havkaRepository;

    @GetMapping("/blog")
    public String blogMain(Model model){
        Iterable<Havka> havkas = havkaRepository.findAll();
        model.addAttribute("havkas", havkas);
        return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model){
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title, @RequestParam String addits, @RequestParam String unit, @RequestParam String imagePath, @RequestParam double quantity, Model model){
        Havka havka = new Havka(title, addits, imagePath, unit, quantity);
        havkaRepository.save(havka);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value = "id") long id, Model model){
        Optional<Havka> havka = havkaRepository.findById(id);
        ArrayList<Havka> res = new ArrayList<>();
        havka.ifPresent(res::add);

        model.addAttribute("havkas", res);
        return "blog-details";
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long id, Model model){
        Havka havka = havkaRepository.findById(id).orElseThrow();
        model.addAttribute("havkas", havka); // 👈 кладём один объект
        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogPostUpdate(@PathVariable(value = "id") long id, @RequestParam String title, @RequestParam String addits, @RequestParam String unit, @RequestParam String imagePath, @RequestParam double quantity, Model model){
        Havka havka = havkaRepository.findById(id).orElseThrow();
        havka.setTitle(title);
        havka.setAddits(addits);
        havka.setUnit(unit);
        havka.setImagePath(imagePath);
        havka.setQuantity(quantity);
        havkaRepository.save(havka);

        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/remove")
    public String blogPostDelete(@PathVariable(value = "id") long id, Model model){
        Havka havka = havkaRepository.findById(id).orElseThrow();
        havkaRepository.delete(havka);

        return "redirect:/blog";
    }
}
