package com.example.randomGroup.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.randomGroup.model.User;
import com.example.randomGroup.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repository;

    @Autowired
    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @CrossOrigin(origins="http://localhost:4200")
    @GetMapping
    public List<User> getAllUsers() {
        if (repository.findAll().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No users found !");
        }

        return repository.findAll();

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        User existingUser = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unexisting user !"));

        return existingUser;

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        User existingUser = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unexisting user !"));

                // Variable qui reprend un email existant, peut être vide d'où Optional
                Optional<User> userWithSameEmail = repository.findByEmail(user.getEmail());

        //Si l'user avec le même email est présent et que l'id est différent
        if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already used by another user!");
        }

        // Mise à jour uniquement si la donnée est présente dans le body
        if (user.getFirstName() != null) {
            existingUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            existingUser.setLastName(user.getLastName());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }

        //Sauvegarde
        User updated = repository.save(existingUser);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (repository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unexisting user !");
        }
        repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted !");
    }
}