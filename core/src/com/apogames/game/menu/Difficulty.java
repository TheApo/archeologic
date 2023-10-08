package com.apogames.game.menu;

public enum Difficulty {

    EASY("Normal", 5),

    HARD("Hard", 3);

    private final String text;

    private final int givenTiles;

    Difficulty(String text, int givenTiles) {
        this.text = text;
        this.givenTiles = givenTiles;
    }

    public int getGivenTiles() {
        return givenTiles;
    }

    public String getText() {
        return text;
    }

    public Difficulty addDifficulty(int add) {
        Difficulty difficulty = this;
        for (int i = 0; i < values().length; i++) {
            if (values()[i].equals(this)) {
                int next = i + add;
                if (next < 0) {
                    return values()[values().length - 1];
                } else if (next >= values().length) {
                    return values()[0];
                }
                return values()[next];
            }
        }
        return difficulty;
    }
}
