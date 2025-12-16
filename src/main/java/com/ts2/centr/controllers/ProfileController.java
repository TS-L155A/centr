package com.ts2.centr.controllers;

import com.ts2.centr.repo.UserRepository;
import com.ts2.centr.security.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ProfileController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d).{5,}$";


    public ProfileController(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model){

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/profile/password")
    public String changePasswordPage() {
        return "change-password";
    }

    @PostMapping("/profile/password")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, Authentication authentication, Model model){
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            model.addAttribute("error", "Старый пароль НЕ ТОТ, ты пиздабол");
            return "change-password";
        }

        if (!newPassword.matches(PASSWORD_REGEX)) {
            model.addAttribute("error", "Пароль должен быть от 5 символов, с 1 заглавной буквой и 1 цифрой");
            return "change-password";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "redirect:/profile";
    }
}
