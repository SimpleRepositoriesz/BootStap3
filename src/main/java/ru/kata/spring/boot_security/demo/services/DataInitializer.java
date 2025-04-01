package ru.kata.spring.boot_security.demo.services;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;

import java.util.Set;


@Component
public class DataInitializer implements ApplicationRunner {

    private final UserService userService;
    private final RoleService roleService;

    public DataInitializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }



    @Override
    public void run(ApplicationArguments args) {
        if (roleService.getRoles().isEmpty()) {
            Role adminRole = new Role();
            adminRole.setRoles("ROLE_ADMIN");
            Role userRole = new Role();
            userRole.setRoles("ROLE_USER");
            roleService.save(adminRole);
            roleService.save(userRole);
        }

        if (userService.findAll().isEmpty()) {
            User admin = new User();
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setAge(20);
            admin.setEmail("admin@admin.com");
            admin.setPassword("admin");
            admin.setRoles(Set.of(
                    roleService.findByName("ROLE_ADMIN")
                            .orElseThrow(() -> new RuntimeException("Роль ROLE_ADMIN не найдена")),
                    roleService.findByName("ROLE_USER")
                            .orElseThrow(() -> new RuntimeException("Роль ROLE_USER не найдена"))
            ));
            userService.saveUser(admin);

            User user = new User();
            user.setFirstName("user");
            user.setLastName("user");
            user.setAge(19);
            user.setEmail("user@user.com");
            user.setPassword("user");
            user.setRoles(Set.of(
                    roleService.findByName("ROLE_USER")
                            .orElseThrow(() -> new RuntimeException(("Роль ROLE_USER не найдена")))
            ));
            userService.saveUser(user);
        }
    }
}