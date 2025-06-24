package com.example.randomGroup;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.randomGroup.model.ENUM.Gender.FEMININ;
import static com.example.randomGroup.model.ENUM.Gender.MASCULIN;
import static com.example.randomGroup.model.ENUM.Level.LEVEL_1;
import static com.example.randomGroup.model.ENUM.Level.LEVEL_2;
import static com.example.randomGroup.model.ENUM.Level.LEVEL_3;
import static com.example.randomGroup.model.ENUM.Level.LEVEL_4;
import static com.example.randomGroup.model.ENUM.Profile.RESERVE;
import static com.example.randomGroup.model.ENUM.Profile.TIMIDE;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.randomGroup.model.Group;
import com.example.randomGroup.model.Student;
import com.example.randomGroup.model.DTO.GroupRequestDTO;
import com.example.randomGroup.model.ENUM.Gender;
import com.example.randomGroup.repository.GroupRepository;
import com.example.randomGroup.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class GroupControllerTest {

    @Autowired
    private GroupRepository repository;

    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        List<Student> students = studentRepo.findAll();
        for (Student s : students) {
            s.setGroup(null);
        }
        studentRepo.saveAll(students);

        // Puis vider les tables sans erreur de contrainte
        repository.deleteAll();
        studentRepo.deleteAll();    }

    @Test
    void testGetGroups() throws Exception {
        repository.save(new Group(null, "group", null));

        mockMvc.perform(get("/groups")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("group"));
    }

    @Test
    void testGetGroup() throws Exception {
        Group group = repository.save(new Group(null, "group", null));

        mockMvc.perform(get("/groups/" + group.getId())).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("group"));
    }

    @Test
    void testCreateGroup() throws Exception {
        // Arrange : on sauvegarde des étudiants en base
        studentRepo.save(new Student(null, "Alice", FEMININ, LEVEL_1, LEVEL_1, true,
                TIMIDE, 52, null, null));
        studentRepo.save(new Student(null, "Bob", MASCULIN, LEVEL_3, LEVEL_3, true,
                TIMIDE, 30, null, null));
        studentRepo.save(new Student(null, "Charlie", MASCULIN, LEVEL_4, LEVEL_2, true,
                TIMIDE, 19, null, null));

        // On crée le JSON pour GroupRequestDTO
        GroupRequestDTO dto = new GroupRequestDTO();
        dto.setName("Groupe Test");
        dto.setGroupNumber(2);
        dto.setMixGender(true); // Critère activé
        dto.setMixAges(false);
        dto.setMixDWWM(false);

        // Act & Assert
        mockMvc.perform(post("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Groupe Test"))
                .andExpect(jsonPath("$.students").isArray())
                .andExpect(jsonPath("$.students.length()").value(2))
                .andExpect(jsonPath("$.students[0].id").exists())
                .andExpect(jsonPath("$.students[1].id").exists());
    }

    @Test
    void testDeleteGroup() throws Exception {
        Group group = repository.save(new Group(null, "group", null));

        mockMvc.perform(delete("/groups/" + group.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist()).andExpect(jsonPath("$.name").doesNotExist());
    }
}
