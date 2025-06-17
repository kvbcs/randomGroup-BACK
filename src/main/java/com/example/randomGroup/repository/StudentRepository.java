package com.example.randomGroup.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.randomGroup.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByName(String name);
}
