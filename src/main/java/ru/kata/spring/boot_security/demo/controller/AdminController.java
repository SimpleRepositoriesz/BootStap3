package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin";
    }

    @GetMapping("/info")
    public String getAdminInfo(Principal principal, Model model) {
        User admin = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("admin", admin);
        return "admin-info";
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getRoles());
        return "create-user";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.getRoles());
            return "create-user";
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping
    public String saveNewUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/update")
    public String updateUserForm(@RequestParam Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getRoles());
        return "update-user";
    }

    @PutMapping("/update")
    public String updateUser(@ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getRoles());
            return "update-user";
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping()
    public String deleteUser(@RequestParam(value = "id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }
}