package com.example.randomGroup.model;

import com.example.randomGroup.model.ENUM.Gender;
import com.example.randomGroup.model.ENUM.Level;
import com.example.randomGroup.model.ENUM.Profile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // Variables
    private Long id;
    private String name;
    private Gender gender;
    private Level frLevel;
    private Level skillLevel;
    private boolean isDWWM;
    private Profile profile;
    private int age;

    // Relation avec table StudentList
    @ManyToOne
    @JoinColumn(name = "list_id")
    private StudentList list;

    public Student() {
    }

    // Constructor
    public Student(Long id, String name, Gender gender, Level frLevel, Level skillLevel, boolean isDWWM,
            Profile profile, int age, StudentList list) {
        this.name = name;
        this.gender = gender;
        this.frLevel = frLevel;
        this.skillLevel = skillLevel;
        this.isDWWM = isDWWM;
        this.profile = profile;
        this.age = age;
        this.list = list;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Level getFrLevel() {
        return frLevel;
    }

    public void setFrLevel(Level frLevel) {
        this.frLevel = frLevel;
    }

    public Level getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(Level skillLevel) {
        this.skillLevel = skillLevel;
    }

    public boolean getIsDWWM() {
        return isDWWM;
    }

    public void setIsDWWM(boolean isDWWM) {
        this.isDWWM = isDWWM;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public StudentList getList() {
        return list;
    }

    public void setList(StudentList list) {
        this.list = list;
    }
}
