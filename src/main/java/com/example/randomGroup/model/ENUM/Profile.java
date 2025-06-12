package com.example.randomGroup.model.ENUM;

//Crétion d'un enum utilisé dans Student
public enum Profile {
    TIMIDE("Timide"),
    RESERVE("Réservé"),
    A_L_AISE("A l'aise");

    private final String profile;

    //Constructor
    private Profile(String profile) {
        this.profile = profile;
    }

    //Getter
    public String getProfile() {
        return profile;
    }
}
