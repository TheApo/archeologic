package com.apogames.game.question;

import com.apogames.backend.GameScreen;
import com.apogames.common.Localization;

import java.util.ArrayList;
import java.util.HashSet;

public class AmountTiles extends Question {

    private int answer = 0;

    public AmountTiles(int column, int row, byte[][] solution) {
        if (column >= 0) {
            this.answer = 0;
            HashSet<Byte> differentTiles = new HashSet<>();
            for (int y = 0; y < solution.length; y++) {
                if (solution[y][column] < 9 && solution[y][column] > 0) {
                    differentTiles.add(solution[y][column]);
                }
            }
            this.answer = differentTiles.size();
            this.setColumn(column);
        } else if (row >= 0) {
            this.answer = 0;
            HashSet<Byte> differentTiles = new HashSet<>();
            for (int x = 0; x < solution[0].length; x++) {
                if (solution[row][x] < 9 && solution[row][x] > 0) {
                    differentTiles.add(solution[row][x]);
                }
            }
            this.answer = differentTiles.size();
            this.setRow(row);
        }
        this.setQuestionEnum(QuestionEnum.AMOUNT_TILES);
    }

    @Override
    public int getCosts() {
        return 1;
    }

    @Override
    public String getText() {
        String answer = "";
        if (this.getRow() >= 0) {
            answer = Localization.getInstance().getCommon().get("question_different_tiles_row");
            answer = answer.replace("{x}", String.valueOf(getRow()+1));
        } else if (this.getColumn() >= 0) {
            answer = Localization.getInstance().getCommon().get("question_different_tiles_column");
            char r = (char)(String.valueOf(this.getColumn()).charAt(0) + 17);
            answer = answer.replace("{x}", String.valueOf(r));
        }
        answer = answer.replace("{y}", getAnswer());
        return answer;
    }

    @Override
    public String getAnswer() {
        return String.valueOf(this.answer);
    }

    @Override
    public ArrayList<Integer> filter(ArrayList<byte[][]> solutionsReal, ArrayList<byte[][]> solutions) {
        ArrayList<Integer> results = new ArrayList<>();
        for (int i = 0; i < solutionsReal.size(); i++) {
            byte[][] currentSolution = solutionsReal.get(i);
            boolean found = true;
            if (getColumn() >= 0) {
                HashSet<Byte> differentTiles = new HashSet<>();
                for (int y = 0; y < currentSolution.length; y++) {
                    if (currentSolution[y][getColumn()] < 9 && currentSolution[y][getColumn()] > 0) {
                        differentTiles.add(currentSolution[y][getColumn()]);
                    }
                }
                if (differentTiles.size() != this.answer) {
                    found = false;
                }
            } else if (getRow() >= 0) {
                HashSet<Byte> differentTiles = new HashSet<>();
                for (int x = 0; x < currentSolution[0].length; x++) {
                    if (currentSolution[getRow()][x] < 9 && currentSolution[getRow()][x] > 0) {
                        differentTiles.add(currentSolution[getRow()][x]);
                    }
                }
                if (differentTiles.size() != this.answer) {
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
    public void draw(GameScreen screen, int addX, int addY, int size) {
    }
}
