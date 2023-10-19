package com.apogames.game.question;

public enum QuestionEnum {
    ONE_TILE_CHECK("question_enum_one_tile"),
    HORIZONTAL_VERTICAL_BORDER_CHECK("question_enum_horizontal_border"),
    EMPTY("question_enum_empty"),
    AMOUNT_TILES("question_enum_amount_tiles"),
    SAND_AND_GRASS_CHECK("question_enum_sand_and_grass");

    private final String text;
    QuestionEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
