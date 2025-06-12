package com.example.randomGroup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.randomGroup.model.User;
import com.example.randomGroup.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        if (repository.findAll().isEmpty()) {
            System.out.print("No users found !");
            return null;
        }
        System.out.print("All users found");

        return repository.findAll();

    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        if (repository.findById(id).isEmpty()) {
            System.out.print("Unexisting user !");
            return null;
        }
        System.out.print("User found");
        return repository.findById(id).orElseThrow();

    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        User existingUser = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        System.out.print("User updated" + existingUser);

        return repository.save(existingUser);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (repository.findById(id).isEmpty()) {
            System.out.print("Unexisting user !");
        }
        repository.deleteById(id);
        System.out.print("User deleted");
    }
}