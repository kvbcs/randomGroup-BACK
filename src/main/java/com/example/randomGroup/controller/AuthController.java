
package com.example.randomGroup.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.randomGroup.model.User;
import com.example.randomGroup.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository repository;

    @Autowired
    public AuthController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        // Vérifie si l'email existe déjà avec méthode créé dans le repository
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            // Renvoie un http 409 conflit
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid credentials");
        }
        User newUser = repository.save(user);

        // Retourne 201 + message confirmation
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    // Méthode attend une réponse HTTP avec ResponseEntity
    public ResponseEntity<String> login(@RequestBody User user) {

        // Optional : peut contenir ou non une value, vérifiable avec isPresent()
        Optional<User> existingUser = repository.findByEmail(user.getEmail());

        // Vérification si l'utilisateur n'existe pas
        if (existingUser.isEmpty()) {
            // Retourne une réponse HTTP 404 avec ResponseEntity
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ User not found");
        }

        // Vérif que le body password corresponde à un password de la bdd
        if (!existingUser.get().getPassword().equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Incorrect password");
        }

        return ResponseEntity.ok("Login successful !");
    }
}
