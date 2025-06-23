package com.example.randomGroup.controller;

import java.util.List;
import java.util.Map;
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

import com.example.randomGroup.model.ENUM.Gender;
import com.example.randomGroup.model.ENUM.Level;
import com.example.randomGroup.model.ENUM.Profile;
import com.example.randomGroup.model.Student;
import com.example.randomGroup.model.StudentList;
import com.example.randomGroup.repository.StudentListRepository;
import com.example.randomGroup.repository.StudentRepository;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository repository;
    private final StudentListRepository studentListRepository;

    @Autowired
    public StudentController(StudentRepository repository, StudentListRepository studentListRepository) {
        this.repository = repository;
        this.studentListRepository = studentListRepository;
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
    public Student update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Student existingStudent = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (body.containsKey("list")) {
            Long listId = Long.valueOf(body.get("list").toString());
            StudentList studentList = studentListRepository.findById(listId)
                    .orElseThrow(() -> new RuntimeException("List not found"));
            existingStudent.setList(studentList);
        }

        if (body.containsKey("name")) {
            existingStudent.setName(body.get("name").toString());
        }
        if (body.containsKey("gender")) {
            existingStudent.setGender(Gender.valueOf(body.get("gender").toString().toUpperCase()));
        }
        if (body.containsKey("frLevel")) {
            existingStudent.setFrLevel(Level.valueOf(body.get("frLevel").toString().toUpperCase()));
        }
        if (body.containsKey("isDWWM")) {
            existingStudent.setIsDWWM(Boolean.valueOf(body.get("isDWWM").toString()));
        }
        if (body.containsKey("skillLevel")) {
            existingStudent.setSkillLevel(Level.valueOf(body.get("skillLevel").toString().toUpperCase()));
        }
        if (body.containsKey("profile")) {
            existingStudent.setProfile(Profile.valueOf(body.get("profile").toString().toUpperCase()));
        }
        if (body.containsKey("age")) {
            existingStudent.setAge(Integer.valueOf(body.get("age").toString()));
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