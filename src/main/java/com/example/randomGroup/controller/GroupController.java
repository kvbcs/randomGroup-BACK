package com.example.randomGroup.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.randomGroup.model.Group;
import com.example.randomGroup.model.Student;
import com.example.randomGroup.model.ENUM.Gender;
import com.example.randomGroup.repository.GroupRepository;
import com.example.randomGroup.repository.StudentRepository;

@RestController
@RequestMapping("/group")
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
        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable Long id) {
        return this.repository.findById(id).orElseThrow();
    }

    @PostMapping
    public Group create(@RequestBody Group group) {

        // 1. Récupérer tous les étudiants disponibles
        List<Student> allStudents = studentRepository.findAll();

        // 2. Filtrer les étudiants selon les critères
        List<Student> selectedStudents = filterStudents(allStudents, group);
        // TODO : rajouter méthode dans repository

        // 3. Affecter les étudiants au groupe
        for (Student student : selectedStudents) {
            student.setGroup(group); // Important pour la relation bidirectionnelle
        }

        group.setStudents(selectedStudents);

        // 4. Sauvegarder
        return repository.save(group);
    }

    // Fonction pour filtrer les étudiants
    public List<Student> filterStudents(List<Student> allStudents, Group groupCriteria) {

        int groupSize = groupCriteria.getGroupNumber();

        // Initialisation des listes par critère
        List<Student> males = new ArrayList<>();
        List<Student> females = new ArrayList<>();
        List<Student> nonBinary = new ArrayList<>();
        // DWWM
        List<Student> dwwms = new ArrayList<>();
        // Ages
        List<Student> old = new ArrayList<>();
        List<Student> young = new ArrayList<>();
        // Profiles
        List<Student> timidList = new ArrayList<>();
        List<Student> reserveList = new ArrayList<>();
        List<Student> aLaiseList = new ArrayList<>();

        List<Student> existingStudents = studentRepository.findAll();

        // Étape 1 : segmentation manuelle des étudiants
        for (Student student : allStudents) {

            // DWWM
            if (groupCriteria.getMixDWWM() && Boolean.TRUE.equals(student.getIsDWWM())) {
                dwwms.add(student);
            }

            // Genre
            if (groupCriteria.getMixGender()) {
                if (student.getGender() == Gender.MASCULIN) {
                    males.add(student);
                } else if (student.getGender() == Gender.FEMININ) {
                    females.add(student);
                } else if (student.getGender() == Gender.NE_SE_PRONONCE_PAS){
                    nonBinary.add(student);
                }
            }

            // Âge
            if (groupCriteria.getMixAges()) {
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
        if (groupCriteria.getMixGender()) {
            int half = groupSize / 2;
            selected.addAll(pickRandomFromList(males, half));
            selected.addAll(pickRandomFromList(females, groupSize - half));
        }

        // Exemple : équilibre âge si activé et pas encore rempli
        else if (groupCriteria.getMixAges()) {
            int half = groupSize / 2;
            selected.addAll(pickRandomFromList(young, half));
            selected.addAll(pickRandomFromList(old, groupSize - half));
        }

        // Si critère DWWM activé uniquement
        else if (groupCriteria.getMixDWWM()) {
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
    public Group update(@PathVariable Long id, @RequestBody Group group) {
        Group existingGroup = repository.findById(id).orElseThrow(() -> new RuntimeException("Group not found"));
        existingGroup.setName(group.getName());
        existingGroup.setGroupNumber(group.getGroupNumber());
        existingGroup.setStudents(group.getStudents());

        return repository.save(existingGroup);
    }

}
