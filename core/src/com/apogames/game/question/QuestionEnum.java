package com.apogames.game.question;

import java.util.ArrayList;

public enum QuestionEnum {
    ONE_TILE_CHECK("question_enum_one_tile", 0, 0),
    HORIZONTAL_VERTICAL_BORDER_CHECK("question_enum_horizontal_border", 1, 0),
    EMPTY_NEXT_TILE("question_enum_empty_next_tile", 1, 1),
    TILE_NEXT_TILE("question_enum_tile_next_tile", 1, 2),
    EMPTY("question_enum_empty", 0, 1),
    AMOUNT_TILES("question_enum_amount_tiles", 0, 2),
    SAND_AND_GRASS_CHECK("question_enum_sand_and_grass", 0, 3);

    private final String text;

    private final int questionType;
    private final int index;
    QuestionEnum(String text, int questionType, int index) {
        this.text = text;
        this.questionType = questionType;
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public int getQuestionType() {
        return questionType;
    }

    public int getIndex() {
        return index;
    }

    public static QuestionEnum[] getQuestionEnumForQuestionType(int questionType) {
        ArrayList<QuestionEnum> list = new ArrayList<>();
        for (QuestionEnum questionEnum : QuestionEnum.values()) {
            if (questionEnum.getQuestionType() == questionType) {
                list.add(questionEnum);
            }
        }
        QuestionEnum[] arr = new QuestionEnum[list.size()];
        int index = 0;
        for (QuestionEnum questionEnum : list) {
            arr[index] = questionEnum;
            index += 1;
        }
        return arr;
    }
}
