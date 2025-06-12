package com.example.randomGroup.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;

@Entity
public class StudentList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // Variables
    private Long id;
    private String name;

    // Relation one to many à la table student
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students = new ArrayList<>();

    // Constructor
    public StudentList() {
    }

    public StudentList(Long id, String name, List<Student> students) {
        this.name = name;
        this.students = students;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
