package com.example.randomGroup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            System.out.print("No lists found !");
            return null;
        }
        System.out.print("All lists found");

        return repository.findAll();
    }

    @GetMapping("/{id}")
    public StudentList getList(@PathVariable Long id) {
        if (repository.findById(id).isEmpty()) {
            System.out.print("Unexisting list !");
            return null;
        }
        System.out.print("List found");
        return repository.findById(id).orElseThrow();

    }

    @PostMapping
    public StudentList create(@RequestBody StudentList list) {
        return repository.save(list);
    }

    @PutMapping("/{id}")
    public StudentList update(@PathVariable Long id, @RequestBody StudentList studentList) {
        StudentList existingList = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        existingList.setName(studentList.getName());
        existingList.setStudents(studentList.getStudents());

        System.out.print("List updated" + existingList);

        return repository.save(existingList);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (repository.findById(id).isEmpty()) {
            System.out.print("Unexisting list !");
        }
        repository.deleteById(id);
        System.out.print("List deleted");
    }
}
