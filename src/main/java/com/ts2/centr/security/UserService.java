package com.ts2.centr.security;

import com.ts2.centr.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_-]{3,30}$";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String rawPassword, Role role) {

        if (!username.matches(USERNAME_REGEX)) {
            throw new IllegalArgumentException("Имя пользователя может содержать только латинские буквы, цифры, _ и -");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Такой пользователь уже существует");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);

        return userRepository.save(user);
    }
}
