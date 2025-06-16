package com.example.randomGroup.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.randomGroup.model.Group;
import com.example.randomGroup.model.Student;
import com.example.randomGroup.repository.GroupRepository;
import com.example.randomGroup.repository.StudentRepository;


@RestController
@RequestMapping("/group")
public class GroupController {

    private final GroupRepository repository;
    private final StudentRepository studentRepository;

    @Autowired
    public GroupController(GroupRepository repository, StudentRepository studentRepository) {
        this.repository = repository;
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public List<Group> getAllGroups() {
        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable Long id){
        return this.repository.findById(id).orElseThrow();
    }

    @PostMapping
    public Group create(@RequestBody Group group) {

        // 1. Récupérer tous les étudiants disponibles
        List<Student> allStudents = studentRepository.findAll();

        // 2. Filtrer les étudiants selon les critères
        List<Student> selectedStudents = filterStudents(allStudents, group);
        //TODO : rajouter méthode dans repository

        // 3. Affecter les étudiants au groupe
        for (Student student : selectedStudents) {
            student.setGroup(group); // Important pour la relation bidirectionnelle
        }

        group.setStudents(selectedStudents);

        // 4. Sauvegarder
        return repository.save(group);
    }
    
    @PutMapping("/{id}")
    public Group update(@PathVariable Long id, @RequestBody Group group) {
         Group  existingGroup = repository.findById(id).orElseThrow(() -> new RuntimeException("Group not found"));
        existingGroup.setName(group.getName());
        existingGroup.setGroupNumber(group.getGroupNumber());
        existingGroup.setStudents(group.getStudents());
       
        return repository.save(existingGroup);
    }

}
