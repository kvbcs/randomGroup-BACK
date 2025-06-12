package com.example.randomGroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.randomGroup.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
