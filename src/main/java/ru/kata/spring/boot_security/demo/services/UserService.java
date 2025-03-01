package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.entities.User;

import java.util.List;

public interface UserService {
    User getUserById(Long id);
    User findByUsername(String username);
    List<User> getAllUsers();
    void saveUser(User user);
    void deleteUser(Long id);
    void updateUser(User user);

}