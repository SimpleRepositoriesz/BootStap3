package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        return "new-user";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = roleService.findByName("ROLE_USER");
            user.setRoles(Set.of(defaultRole));
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + id);
        }
        logger.debug("Editing user: {}", user);
        List<Role> allRoles = roleService.getListRoles();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        return "edit-user";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user) {
        User existingUser = userService.getUserById(user.getId());
        if (existingUser == null) {
            throw new RuntimeException("User not found with id: " + user.getId());
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}