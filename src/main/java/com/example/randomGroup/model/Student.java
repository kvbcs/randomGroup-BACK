package com.example.randomGroup.model;

import com.example.randomGroup.model.ENUM.Gender;
import com.example.randomGroup.model.ENUM.Level;
import com.example.randomGroup.model.ENUM.Profile;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Variables
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "fr_level")
    private Level frLevel;
    @Column(name = "skill_level")
    @Enumerated(EnumType.ORDINAL)
    private Level skillLevel;
    private Boolean isDWWM;
    @Enumerated(EnumType.STRING)
    private Profile profile;
    private Integer age;

    // Relation avec table StudentList
    @ManyToOne
    @JoinColumn(name = "list")
    @JsonIgnoreProperties({ "students" })
    // @JsonBackReference // affiche qu'une fois la liste dans Student sinon boucle
    // infinie
    private StudentList list;

    @ManyToOne
    @JoinColumn(name = "group_id")
    // @JsonBackReference // affiche qu'une fois le group dans Student sinon boucle infinie
    private Group group;

    public Student() {
    }

    // Constructor
    public Student(Long id, String name, Gender gender, Level frLevel, Level skillLevel, Boolean isDWWM,
            Profile profile, Integer age, StudentList list, Group group) {
        this.name = name;
        this.gender = gender;
        this.frLevel = frLevel;
        this.skillLevel = skillLevel;
        this.isDWWM = isDWWM;
        this.profile = profile;
        this.age = age;
        this.list = list;
        this.group = group;
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

    public Boolean getIsDWWM() {
        return isDWWM;
    }

    public void setIsDWWM(Boolean isDWWM) {
        this.isDWWM = isDWWM;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public StudentList getList() {
        return list;
    }

    public void setList(StudentList list) {
        this.list = list;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
