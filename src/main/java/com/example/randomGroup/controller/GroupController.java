package com.example.randomGroup.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.randomGroup.model.DTO.GroupRequestDTO;
import com.example.randomGroup.model.Group;
import com.example.randomGroup.model.Student;
import com.example.randomGroup.model.ENUM.Gender;
import com.example.randomGroup.repository.GroupRepository;
import com.example.randomGroup.repository.StudentRepository;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupRepository repository;
    private final StudentRepository studentRepository;

    @Autowired
    public GroupController(GroupRepository repository, StudentRepository studentRepository) {
        this.repository = repository;
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public List<Group> getAllGroups() {
        if (repository.findAll().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No groups found !");
        }
        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable Long id) {
        Group existingGroup = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unexisting group !"));
        return existingGroup;
    }

    @PostMapping
    public ResponseEntity<Group> create(@RequestBody GroupRequestDTO dto) {

        // 1. Récupérer tous les étudiants disponibles
        List<Student> allStudents = studentRepository.findAll();

        // 2. Filtrer les étudiants selon les critères
        List<Student> selectedStudents = filterStudents(allStudents, dto);

        // 3. Crée l'entité Group avec uniquement les infos à stocker
        Group group = new Group();
        group.setName(dto.getName());

        for (Student s : selectedStudents) {
            s.setGroup(group);
        }

        group.setStudents(selectedStudents);
        // 4. Sauvegarder
        Group saved = repository.save(group);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Fonction pour filtrer les étudiants
    public List<Student> filterStudents(List<Student> allStudents,
            GroupRequestDTO dto) {

        int groupSize = dto.getGroupNumber();

        // Initialisation des listes par critère
        List<Student> males = new ArrayList<>();
        List<Student> females = new ArrayList<>();
        List<Student> nonBinary = new ArrayList<>();
        // DWWM
        List<Student> dwwms = new ArrayList<>();
        // Ages
        List<Student> old = new ArrayList<>();
        List<Student> young = new ArrayList<>();

        // Étape 1 : segmentation manuelle des étudiants
        for (Student student : allStudents) {

            // DWWM
            if (dto.getMixDWWM() && Boolean.TRUE.equals(student.getIsDWWM())) {
                dwwms.add(student);
            }

            // Genre
            if (dto.getMixGender()) {
                if (student.getGender() == Gender.MASCULIN) {
                    males.add(student);
                } else if (student.getGender() == Gender.FEMININ) {
                    females.add(student);
                } else if (student.getGender() == Gender.NE_SE_PRONONCE_PAS) {
                    nonBinary.add(student);
                }
            }

            // Âge
            if (dto.getMixAges()) {
                if (student.getAge() <= 25) {
                    young.add(student);
                } else {
                    old.add(student);
                }
            }
        }

        // Étape 2 : on choisit les étudiants en équilibrant les critères
        List<Student> selected = new ArrayList<>();

        // Exemple : équilibre homme/femme si activé
        if (dto.getMixGender()) {
            int half = groupSize / 2;
            selected.addAll(pickRandomFromList(males, half));
            selected.addAll(pickRandomFromList(females, groupSize - half));
        }

        // Exemple : équilibre âge si activé et pas encore rempli
        else if (dto.getMixAges()) {
            int half = groupSize / 2;
            selected.addAll(pickRandomFromList(young, half));
            selected.addAll(pickRandomFromList(old, groupSize - half));
        }

        // Si critère DWWM activé uniquement
        else if (dto.getMixDWWM()) {
            selected.addAll(pickRandomFromList(dwwms, groupSize));
        }

        // Sinon, on prend des étudiants normaux (sans filtrage)
        else {
            selected.addAll(pickRandomFromList(allStudents, groupSize));
        }

        // Dernier shuffle pour rendre le groupe aléatoire
        shuffleList(selected);
        return selected;

    }

    // Sélectionne un nombre donné d'étudiants d'une liste aléatoirement
    private List<Student> pickRandomFromList(List<Student> list, int count) {
        List<Student> copy = new ArrayList<>(list);
        shuffleList(copy);

        List<Student> result = new ArrayList<>();
        int size = Math.min(count, copy.size());

        for (int i = 0; i < size; i++) {
            result.add(copy.get(i));
        }
        return result;
    }

    // Mélange une liste
    private void shuffleList(List<Student> list) {
        Random random = new Random();
        for (int i = list.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Student temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
        }
    }

    @PutMapping("/{id}")
    public Group update(@PathVariable Long id, @RequestBody Group newGroup) {
        Group existingGroup = repository.findById(id).orElseThrow(() -> new RuntimeException("Group not found"));

        // Update du nom si fourni
        if (newGroup.getName() != null) {
            existingGroup.setName(newGroup.getName());
        }

        // Mise à jour des étudiants (champ par champ, sans perdre les données)
        if (newGroup.getStudents() != null) {
            List<Student> updatedStudents = new ArrayList<>();

            for (Student incomingStudent : newGroup.getStudents()) {
                Student originalStudent = studentRepository.findById(incomingStudent.getId())
                        .orElseThrow(() -> new RuntimeException("Student not found"));

                if (incomingStudent.getName() != null)
                    originalStudent.setName(incomingStudent.getName());

                if (incomingStudent.getAge() != null)
                    originalStudent.setAge(incomingStudent.getAge());

                if (incomingStudent.getIsDWWM() != null)
                    originalStudent.setIsDWWM(incomingStudent.getIsDWWM());

                if (incomingStudent.getFrLevel() != null)
                    originalStudent.setFrLevel(incomingStudent.getFrLevel());

                if (incomingStudent.getGender() != null)
                    originalStudent.setGender(incomingStudent.getGender());

                if (incomingStudent.getProfile() != null)
                    originalStudent.setProfile(incomingStudent.getProfile());

                if (incomingStudent.getSkillLevel() != null)
                    originalStudent.setSkillLevel(incomingStudent.getSkillLevel());

                // Important : rattache bien l'étudiant au bon groupe
                originalStudent.setGroup(existingGroup);

                updatedStudents.add(originalStudent);
            }

            // 🧼 On remplace la liste complète proprement
            existingGroup.getStudents().clear();
            existingGroup.getStudents().addAll(updatedStudents);
        }

        return repository.save(existingGroup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Group existingGroup = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unexisting group !"));
        // On détache les étudiants du groupe
        List<Student> students = existingGroup.getStudents();
        for (Student student : students) {
            student.setGroup(null);
            studentRepository.save(student);
        }
        repository.delete(existingGroup);
        return ResponseEntity.ok("Group deleted !");
    }
}
