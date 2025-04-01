package ru.kata.spring.boot_security.demo.services;


import ru.kata.spring.boot_security.demo.entities.User;

import java.util.List;
import java.util.Optional;


public interface UserService {

    List<User> findAll();

    User findById(Long Id);

    Optional<User> findByEmail(String email);

    void saveUser(User user);

    void updateUser(User user);

    void deleteById(Long id);
}