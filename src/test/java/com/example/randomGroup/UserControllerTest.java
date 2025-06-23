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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.randomGroup.model.User;
import com.example.randomGroup.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private UserRepository repository;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testGetUsers() throws Exception {
        repository.save(new User(null, "joe", "joe", "email", "password", null));

        mockMvc.perform(get("/users")).andExpect(status().isOk()).andExpect(jsonPath("$[0].firstName").value("joe"));
    }

    @Test
    void testGetUser() throws Exception {
        User user = repository.save(new User(null, "jean", "jack", "email", "pass", null));

        mockMvc.perform(get("/users/" + user.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("jack"));
    }

    @Test
    void testUpdateUser() throws Exception {
        User user = repository.save(new User(null, "uhfuehfu", "fghjk", "fghjk", "dfghjk", null));

        user.setFirstName("yo");

        mockMvc.perform(put("/users/" + user.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("yo"));

    }

    @Test
    void testDeleteUser() throws Exception {
        User user = repository.save(new User(null, "fghjk", "fghjk", "fghjk", "ghjkl", null));

        mockMvc.perform(delete("/users/" + user.getId())).andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.firstName").doesNotExist())
                .andExpect(status().isOk());
    }

}
