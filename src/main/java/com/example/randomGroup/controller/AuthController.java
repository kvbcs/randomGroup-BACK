
package com.example.randomGroup.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.randomGroup.model.User;
import com.example.randomGroup.repository.randomGroupRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private randomGroupRepository repository;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        // Méthode findByEmail qui a été mis dans le fichier repository
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            // Si le compte existe déjà, return rien
            return null;
        }

        // Sinon, register validé
        System.out.println("✅ Register successful");
        //Sauvegarde l'utilisateur dans la bdd
        return repository.save(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {

        //Optional : peut contenir ou non une value, vérifiable avec isPresent()
        Optional<User> existingUser = repository.findByEmail(user.getEmail());

        // Vérification si l'utilisateur n'existe pas
        if (existingUser.isEmpty()) {
            // Retourne une réponse HTTP 404 avec ResponseEntity
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ User not found");
        }

        if (!existingUser.get().getPassword().equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Incorrect password");
        }

        System.out.println("✅ Login successful");
        return ResponseEntity.ok("Welcome back " + existingUser.get().getFirstName() + "!");
    }
}
