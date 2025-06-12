package com.example.randomGroup.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.randomGroup.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Optional : peut contenir ou non une value, vérifiable avec isPresent()
    // Ajout de cette méthode pour l'utiliser dans le controller
    Optional<User> findByEmail(String email);

}
