package com.example.randomGroup.controller;

import java.util.List;

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
import com.example.randomGroup.model.StudentList;
import com.example.randomGroup.repository.StudentListRepository;

@RestController
@RequestMapping("/lists")
public class StudentListController {

    private final StudentListRepository repository;

    @Autowired
    public StudentListController(StudentListRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<StudentList> getAllLists() {
        if (repository.findAll().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No lists found !");
        }

        return repository.findAll();
    }

    @GetMapping("/{id}")
    public StudentList getList(@PathVariable Long id) {
        StudentList existingList = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unexisting list !"));
        return existingList;
    }

    @PostMapping
    public StudentList create(@RequestBody StudentList list) {
        return repository.save(list);
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> addStudents(@PathVariable Long id, @RequestBody Student newStudent) {
        StudentList existingList = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unexisting list !"));

        // Associe le student à la liste
        newStudent.setList(existingList);

        // Ajoute le student à la liste
        existingList.getStudents().add(newStudent);

        repository.save(existingList);

        return ResponseEntity.status(HttpStatus.OK).body("Student added to list " + existingList.getName());
    }

    @PutMapping("/{id}")
    public StudentList update(@PathVariable Long id, @RequestBody StudentList studentList) {
        StudentList existingList = repository.findById(id).orElseThrow(() -> new RuntimeException("List not found"));
        existingList.setName(studentList.getName());

        return repository.save(existingList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (repository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unexisting list !");
        }
        repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("List deleted !");
    }
}
