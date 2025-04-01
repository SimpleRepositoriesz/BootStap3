package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.entities.Role;

import java.util.Optional;
import java.util.Set;

public interface RoleService {
    Set<Role> getRoles();

    Optional<Role> findByName(String roleUser);

    void save(Role roles);
}