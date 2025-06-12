package com.example.randomGroup.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.randomGroup.model.User;
import com.example.randomGroup.repository.randomGroupRepository;

@RestController
@RequestMapping("/users")
public class UserController {
    private final randomGroupRepository repository;

    public UserController(randomGroupRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        System.out.print("All users found");

        return repository.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        System.out.print("User found");

        return repository.findById(id).orElseThrow();
    }

    @PostMapping
    public User register(@RequestBody User user) {
        System.out.print("Register successful");

        return repository.save(user);
    }

    @PostMapping
    public void login(@RequestBody User user) {
        User existingUser = repository.exists(user).orElseThrow(() -> new RuntimeException("Unexisting user"));
        System.out.print("Register successful");

    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        User existingUser = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        System.out.print("User updated");

        return repository.save(existingUser);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        repository.deleteById(id);
        System.out.print("User deleted");
    }
}
