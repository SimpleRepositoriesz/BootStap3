package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserService;
import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "home";
    }

    @GetMapping("/user")
    public String showUser(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/user/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        if (!currentUser.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only edit your own profile");
        }
        User user = userService.findById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User user) {
        User existingUser = userService.findById(id);
        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        user.setId(id); // Убедимся, что ID установлен
        userService.updateUser(user); // Используем метод updateUser
        return "redirect:/user";
    }

}