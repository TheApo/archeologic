package com.apogames.game.question;

import com.apogames.Constants;
import com.apogames.backend.GameScreen;
import com.apogames.common.Localization;
import com.apogames.help.Helper;

import java.util.ArrayList;

public class OneTileSideCheck extends Question {

    private int tile = -1;

    private byte[][] solution;

    private boolean notOnBorder = true;
    private byte[][] currentTile;

    public OneTileSideCheck(int column, int row, byte[][] solution, int tile, byte[][] currentTile) {
        this.tile = tile;

        this.currentTile = Helper.clone(currentTile);
        this.solution = solution;

        if (column >= 0) {
            this.setColumn(column);
        }
        if (row >= 0) {
            this.setRow(row);
        }

        for (int y = 0; y < this.solution.length; y++) {
            for (int x = 0; x < this.solution[0].length; x++) {
                if (this.getRow() >= 0 && (y == 0 || y == this.solution.length - 1)) {
                    if (this.solution[y][x] == tile) {
                        notOnBorder = false;
                    }
                }
                if (this.getColumn() >= 0 && (x == 0 || x == this.solution[0].length - 1)) {
                    if (this.solution[y][x] == tile) {
                        notOnBorder = false;
                    }
                }
            }
        }
        this.setQuestionEnum(QuestionEnum.HORIZONTAL_VERTICAL_BORDER_CHECK);
    }

    @Override
    public void draw(GameScreen screen, int addX, int addY, int size) {
        int startY = addY - this.currentTile.length*size/2;
        screen.getRenderer().setColor(Constants.COLOR_BLACK[0], Constants.COLOR_BLACK[1], Constants.COLOR_BLACK[2], 1f);
        for (int y = 0; y < currentTile.length; y++) {
            for (int x = 0; x < currentTile[0].length; x++) {
                if (this.currentTile[y][x] != 0) {
                    screen.getRenderer().rect(addX + 1 + x * size, startY + 1 + y * size, size - 2, size - 2);
                }
            }
        }

        size -= 2;
        startY = addY - this.solution.length*size/2;
        for (int y = 0; y < this.solution.length; y++) {
            for (int x = 0; x < this.solution[0].length; x++) {
                screen.getRenderer().setColor(Constants.COLOR_BLACK[0], Constants.COLOR_BLACK[1], Constants.COLOR_BLACK[2], 1f);
                if (this.getRow() >= 0 && (y == 0 || y == this.solution.length - 1)) {
                    screen.getRenderer().setColor(Constants.COLOR_RED[0], Constants.COLOR_RED[1], Constants.COLOR_RED[2], 1f);
                }
                if (this.getColumn() >= 0 && (x == 0 || x == this.solution[0].length - 1)) {
                    screen.getRenderer().setColor(Constants.COLOR_RED[0], Constants.COLOR_RED[1], Constants.COLOR_RED[2], 1f);
                }
                screen.getRenderer().rect(addX + 450 + 1 + x * size, startY + 1 + y * size, size - 2, size - 2);
            }
        }
    }

    @Override
    public int getCosts() {
        return 2;
    }

    public boolean withAskCosts() {
        return false;
    }

    @Override
    public String getText() {
        String answer = "";
        if (this.getRow() >= 0) {
            if (this.getColumn() >= 0) {
                answer = Localization.getInstance().getCommon().get("question_complete_border");
            } else {
                answer = Localization.getInstance().getCommon().get("question_horizontal_border");
            }
        } else if (this.getColumn() >= 0) {
            answer = Localization.getInstance().getCommon().get("question_vertical_border");
        }

        String answerNot = "";
        if (this.notOnBorder) {
            answerNot = Localization.getInstance().getCommon().get("question_not")+" ";
        }
        answer = answer.replace("{x}", answerNot);
        return answer;
    }

    @Override
    public String getAnswer() {
        return "";
    }

    @Override
    public ArrayList<Integer> filter(ArrayList<byte[][]> solutionsReal, ArrayList<byte[][]> solutions) {
        ArrayList<Integer> results = new ArrayList<>();
        for (int i = 0; i < solutionsReal.size(); i++) {
            byte[][] solution = solutions.get(i);
            boolean found = true;
            int countOnBorder = 0;
            for (int y = 0; y < solution.length; y++) {
                for (int x = 0; x < solution[0].length; x++) {
                    if (notOnBorder) {
                        if (this.getRow() >= 0 && (y == 0 || y == solution.length - 1)) {
                            if (solution[y][x] == tile) {
                                found = false;
                                break;
                            }
                        }
                        if (this.getColumn() >= 0 && (x == 0 || x == solution[0].length - 1)) {
                            if (solution[y][x] == tile) {
                                found = false;
                                break;
                            }
                        }
                    } else {
                        if (this.getRow() >= 0 && (y == 0 || y == solution.length - 1)) {
                            if (solution[y][x] == tile) {
                                countOnBorder += 1;
                            }
                        }
                        if (this.getColumn() >= 0 && (x == 0 || x == solution[0].length - 1)) {
                            if (solution[y][x] == tile) {
                                countOnBorder += 1;
                            }
                        }
                    }
                }
                if (!found) {
                    break;
                }
            }

            if (!notOnBorder && countOnBorder == 0) {
                found = false;
            }

            if (found) {
                results.add(i);
            }
        }
        return results;
    }
}
