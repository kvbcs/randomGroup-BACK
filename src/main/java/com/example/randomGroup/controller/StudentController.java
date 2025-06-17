package com.example.randomGroup.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.randomGroup.model.Student;
import com.example.randomGroup.repository.StudentRepository;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository repository;

    @Autowired
    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        List<Student> students = repository.findAll();

        if (students.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No students found !");
        }

        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        Student existingStudent = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unexisting student !"));
        return existingStudent;
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        Optional<Student> existingStudent = repository.findByName(student.getName());

        if (existingStudent.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student already exists !");
        }
        return repository.save(student);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student student) {
        Student existingStudent = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (student.getName() != null) {

            existingStudent.setName(student.getName());
        }
        if (student.getGender() != null) {

            existingStudent.setGender(student.getGender());
        }
        if (student.getFrLevel() != null) {

            existingStudent.setFrLevel(student.getFrLevel());
        }

        if (student.getIsDWWM() != null) {

            existingStudent.setIsDWWM(student.getIsDWWM());
        }

        if (student.getSkillLevel() != null) {

            existingStudent.setSkillLevel(student.getSkillLevel());
        }

        if (student.getProfile() != null) {

            existingStudent.setProfile(student.getProfile());
        }

        if (student.getAge() != null) {

            existingStudent.setAge(student.getAge());
        }

        return repository.save(existingStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (repository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unexisiting student !");
        }
        repository.deleteById(id);

        return ResponseEntity.ok("User deleted !");
    }
}