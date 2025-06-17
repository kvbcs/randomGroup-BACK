package com.example.randomGroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.randomGroup.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

}
