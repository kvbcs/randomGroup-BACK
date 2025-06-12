package com.example.randomGroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.randomGroup.model.StudentList;

public interface StudentListRepository extends JpaRepository<StudentList, Long> {

}
