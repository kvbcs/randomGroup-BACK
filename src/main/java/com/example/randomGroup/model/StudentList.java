package com.example.randomGroup.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // stop la sérialisation infinie et renvoie un id à la place
@Entity
public class StudentList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Variables
    private String name;

    // Relation one to many à la table student
    // orphanRemoval = supprime les étudiants si la liste est supprimée
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    // @JsonManagedReference // Sérialise le parent pour stopper la bouble infinie
    // d'affichage
    private List<Student> students = new ArrayList<>();

    
    @ManyToOne(optional = false) // impose que ce champ soit obligatoire (non-null)
    @JoinColumn(name = "user_id", nullable = false) // force NOT NULL au niveau SQL
    @JsonIgnoreProperties({"lists", "password", "firstName", "lastName"}) // empêche l'inclusion des propriétés dans la réponse
    private User user;

    // Constructor
    public StudentList() {
    }

    public StudentList(Long id, String name, List<Student> students, User user) {
        this.name = name;
        this.students = students;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
