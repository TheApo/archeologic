package com.apogames.game.question;

import com.apogames.backend.GameScreen;
import com.apogames.common.Localization;

import java.util.ArrayList;

public class SandAndGrassCheck extends Question {

    private int sand = 0;
    private int grass = 0;

    public SandAndGrassCheck(int column, int row, byte[][] solution) {
        if (column >= 0) {
            this.sand = 0;
            this.grass = 0;
            for (int y = 0; y < solution.length; y++) {
                if (solution[y][column] == 3) {
                    this.sand += 1;
                }
                if (solution[y][column] == 2) {
                    this.grass += 1;
                }
            }
            this.setColumn(column);
        } else if (row >= 0) {
            this.sand = 0;
            this.grass = 0;
            for (int x = 0; x < solution[0].length; x++) {
                if (solution[row][x] == 3) {
                    this.sand += 1;
                }
                if (solution[row][x] == 2) {
                    this.grass += 1;
                }
            }
            this.setRow(row);
        }
        this.setQuestionEnum(QuestionEnum.SAND_AND_GRASS_CHECK);
    }

    @Override
    public int getCosts() {
        return 2;
    }

    @Override
    public String getText() {
        String answer = "";
        if (this.getRow() >= 0) {
            answer = Localization.getInstance().getCommon().get("question_amount_land_row");
            answer = answer.replace("{x}", String.valueOf(getRow()+1));
        } else if (this.getColumn() >= 0) {
            answer = Localization.getInstance().getCommon().get("question_amount_land_column");
            char r = (char)(String.valueOf(this.getColumn()).charAt(0) + 17);
            answer = answer.replace("{x}", String.valueOf(r));
        }
        answer = answer.replace("{sand}", String.valueOf(this.sand));
        answer = answer.replace("{grass}", String.valueOf(this.grass));
        return answer;
    }

    @Override
    public String getAnswer() {
        return this.sand +" "+ this.grass;
    }

    @Override
    public ArrayList<Integer> filter(ArrayList<byte[][]> solutionsReal, ArrayList<byte[][]> solutions) {
        ArrayList<Integer> results = new ArrayList<>();
        for (int i = 0; i < solutionsReal.size(); i++) {
            byte[][] currentSolution = solutionsReal.get(i);
            boolean found = true;
            if (getColumn() >= 0) {
                int sand = 0;
                int grass = 0;
                for (int y = 0; y < currentSolution.length; y++) {
                    if (currentSolution[y][getColumn()] == 3) {
                        sand += 1;
                    }
                    if (currentSolution[y][getColumn()] == 2) {
                        grass += 1;
                    }
                }
                if (sand != this.sand || grass != this.grass) {
                    found = false;
                }
            } else if (getRow() >= 0) {
                int sand = 0;
                int grass = 0;
                for (int x = 0; x < currentSolution[0].length; x++) {
                    if (currentSolution[getRow()][x] == 3) {
                        this.sand += 1;
                    }
                    if (currentSolution[getRow()][x] == 2) {
                        this.grass += 1;
                    }
                }
                if (sand != this.sand || grass != this.grass) {
                    found = false;
                }
            }

            if (found) {
                results.add(i);
            }
        }
        return results;
    }

    @Override
    public void draw(GameScreen screen, int x, int y, int size) {

    }
}
