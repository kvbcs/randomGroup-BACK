package com.example.randomGroup;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.MockMvc;

import com.example.randomGroup.model.StudentList;
import com.example.randomGroup.model.User;
import com.example.randomGroup.model.DTO.StudentListDTO;
import com.example.randomGroup.repository.StudentListRepository;
import com.example.randomGroup.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentListControllerTest {
    @Autowired
    private StudentListRepository repository;

    @Autowired
    private UserRepository userRepo;
    // mockmvc simule les requetes http
    @Autowired
    private MockMvc mockMvc;

    // transforme les objets en json
    @Autowired
    private ObjectMapper objectMapper;

    // avant chaque test, on vide la base pour rester propre
    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testGetLists() throws Exception {
        User user = userRepo.save(new User(null, "fghjk", "fghj", "ghjk", "ghjk", null));
        repository.save(new StudentList(null, "liste", null, user));

        mockMvc.perform(get("/lists")).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("liste"))
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void testGetList() throws Exception {
        User user = userRepo.save(new User(null, "fghjk", "fghj", "ghjk", "ghjk", null));
        StudentList list = repository.save(new StudentList(null, "liste", null, user));

        mockMvc.perform(get("/lists/" + list.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("liste"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testCreateList() throws Exception {
        User user = userRepo.save(new User(null, "fghjk", "fghj", "ghjk", "ghjk", null));
        StudentListDTO list = new StudentListDTO(null, "liste", user.getId());

        mockMvc.perform(
                post("/lists").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(list)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("liste"));
    }

    @Test
    void testUpdateList() throws Exception {
        User user = userRepo.save(new User(null, "fghjk", "fghj", "ghjk", "ghjk", null));

        StudentList list = repository.save(new StudentList(null, "fghj", null, user));

        list.setName("liste");

        mockMvc.perform(
                put("/lists").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(list)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("liste"));
    }
}