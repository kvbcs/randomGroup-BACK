package com.example.randomGroup.model;

//Création d'un enum utilisé dans Student
public enum Level {
    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3),
    LEVEL_4(4);

    private final int level;

    //Contructor
    Level(int level) {
        this.level = level;
    }

    //Getter
    public int getLevel() {
        return level;
    }
}
