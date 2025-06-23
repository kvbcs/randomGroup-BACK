package com.example.randomGroup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.example.randomGroup.model.ENUM.Gender.*;
import static com.example.randomGroup.model.ENUM.Level.*;
import static com.example.randomGroup.model.ENUM.Profile.*;

import com.example.randomGroup.model.Student;
import com.example.randomGroup.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {
    @Autowired
    private StudentRepository repository;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testGetStudents() throws Exception {
        Student student = repository
                .save(new Student(null, "joe", MASCULIN, LEVEL_1, LEVEL_1, true, TIMIDE, 18, null, null));

        mockMvc.perform(get("/students")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("joe"));
    }

    @Test
    void testGetStudent() throws Exception {
        Student student = repository
                .save(new Student(null, "joe", MASCULIN, LEVEL_1, LEVEL_1, true, TIMIDE, 18, null, null));

        mockMvc.perform(get("/students/" + student.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("joe"));
    }

    @Test
    void testCreateStudent() throws Exception {
        Student student = new Student(null, "joe", MASCULIN, LEVEL_1, LEVEL_1, true, TIMIDE, 18, null, null);

        mockMvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student))).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("joe"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        Student student = repository
                .save(new Student(null, "joe", MASCULIN, LEVEL_1, LEVEL_1, true, TIMIDE, 18, null, null));

        student.setName("jack");
        mockMvc.perform(put("/students/" + student.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student))).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("jack"));
    }

}
