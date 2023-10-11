package com.apogames.game.question;

public enum QuestionEnum {
    EMPTY("question_enum_empty"),
    AMOUNT_TILES("question_enum_amount_tiles"),
    SAND_AND_GRASS_CHECK("question_enum_sand_and_grass"),
    ONE_TILE_CHECK("question_enum_one_tile");

    private final String text;
    QuestionEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
