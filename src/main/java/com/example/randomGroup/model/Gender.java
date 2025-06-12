

package com.example.randomGroup.model;

//Création d'un enum Gender pour l'utiliser dans Student
public enum Gender {
    MASCULIN("Masculin"),
    FEMININ("Féminin"),
    NE_SE_PRONONCE_PAS("Ne se prononce pas");

    private final String gender;

    //Constructor
    Gender(String gender) {
        this.gender = gender;
    }

    //Getter
    public String getGender() {
        return gender;
    }
}
