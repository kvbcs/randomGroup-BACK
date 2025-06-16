package com.example.randomGroup.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

@Entity
@Table(name = "group_table")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Variables
    @Column(name = "group_number")
    private int groupNumber;
    private String name;
    // @Column(name = "mix_dwwm")
    private boolean mixDWWM;
    // @Column(name = "mix_ages")
    private boolean mixAges;
    // @Column(name = "mix_profiles")
    private boolean mixProfiles;
    // @Column(name = "mix_tech")
    private boolean mixTech;
    // @Column(name = "mix_french")
    private boolean mixFrench;
    // @Column(name = "mix_gender")
    private boolean mixGender;

    // mappedBy : référence la variable group de Student
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference
    private List<Student> students = new ArrayList<>();

    public Group() {

    }

    public Group(Long id, int groupNumber, String name, boolean mixDWWM, boolean mixAges,
            boolean mixProfiles,
            boolean mixTech,
            boolean mixFrench,
            boolean mixGender, List<Student> students) {
        this.id = id;
        this.groupNumber = groupNumber;
        this.name = name;
        this.mixDWWM = mixDWWM;
        this.mixAges = mixAges;
        this.mixProfiles = mixProfiles;
        this.mixTech = mixTech;
        this.mixFrench = mixFrench;
        this.students = students;
    }

    // Getters & Setters
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

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public boolean getMixDWWM() {
        return mixDWWM;
    }

    public void setMixDWWM(boolean mixDWWM) {
        this.mixDWWM = mixDWWM;
    }

    public boolean getMixProfiles() {
        return mixProfiles;
    }

    public void setMixProfiles(boolean mixProfiles) {
        this.mixProfiles = mixProfiles;
    }

    public boolean getMixTech() {
        return mixTech;
    }

    public void setMixTech(boolean mixTech) {
        this.mixTech = mixTech;
    }

    public boolean getMixFrench() {
        return mixFrench;
    }

    public void setMixFrench(boolean mixFrench) {
        this.mixFrench = mixFrench;
    }

    public boolean getMixAges() {
        return mixAges;
    }

    public void setMixAges(boolean mixAges) {
        this.mixAges = mixAges;
    }

    public boolean getMixGender() {
        return mixGender;
    }

    public void setMixGender(boolean mixGender) {
        this.mixGender = mixGender;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
