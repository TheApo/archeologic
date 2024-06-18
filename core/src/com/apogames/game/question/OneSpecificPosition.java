package com.apogames.game.question;

import com.apogames.backend.GameScreen;
import com.apogames.common.Localization;

import java.util.ArrayList;

public class OneSpecificPosition extends Question {

    private int answer = 0;

    public OneSpecificPosition(int column, int row, byte[][] solution) {
        this.answer = solution[row][column];
        this.setColumn(column);
        this.setRow(row);
    }

    @Override
    public int getCosts() {
        return 0;
    }

    @Override
    public String getText() {
        String answer = "";
        answer = Localization.getInstance().getCommon().get("question_one_specific");
        answer = answer.replace("{x}", String.valueOf(getRow()+1));
        char r = (char)(String.valueOf(this.getColumn()).charAt(0) + 17);
        answer = answer.replace("{y}", String.valueOf(r));
        answer = answer.replace("{answer}", getAnswer());

        return answer;
    }

    @Override
    public String getAnswer() {
        if (this.answer == 2) {
            return Localization.getInstance().getCommon().get("question_grass");
        } else if (this.answer == 3) {
            return Localization.getInstance().getCommon().get("question_sand");
        } else if (this.answer == 1) {
            return Localization.getInstance().getCommon().get("question_forest");
        } else {
            return Localization.getInstance().getCommon().get("question_water");
        }
    }

    @Override
    public ArrayList<Integer> filter(ArrayList<byte[][]> solutionsReal, ArrayList<byte[][]> possibleSolutionsPossibilities, ArrayList<byte[][]> solutions) {
        ArrayList<Integer> results = new ArrayList<>();
        for (int i = 0; i < solutionsReal.size(); i++) {
            byte[][] currentSolution = solutionsReal.get(i);
            boolean found = true;
            if (getColumn() >= 0) {
                int answer = 0;
                for (int y = 0; y < currentSolution.length; y++) {
                    if (currentSolution[y][getColumn()] == 0) {
                        answer += 1;
                    }
                }
                if (answer != this.answer) {
                    found = false;
                }
            } else if (getRow() >= 0) {
                int answer = 0;
                for (int x = 0; x < currentSolution[0].length; x++) {
                    if (currentSolution[getRow()][x] == 0) {
                        answer += 1;
                    }
                }
                if (answer != this.answer) {
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
