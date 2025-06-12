package com.example.randomGroup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.randomGroup.model.Student;
import com.example.randomGroup.repository.StudentRepository;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return repository.save(student);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student student) {
        Student existingStudent = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        existingStudent.setName(student.getName());
        existingStudent.setGender(student.getGender());
        existingStudent.setFrLevel(student.getFrLevel());
        existingStudent.setIsDWWM(student.getIsDWWM());
        existingStudent.setSkillLevel(student.getSkillLevel());
        existingStudent.setProfile(student.getProfile());
        existingStudent.setAge(student.getAge());
       
        return repository.save(existingStudent);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (repository.findById(id).isEmpty()) {
            System.out.print("Unexisting user !");
        }
        repository.deleteById(id);
    }
}
