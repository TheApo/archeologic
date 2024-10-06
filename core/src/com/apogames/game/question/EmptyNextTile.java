package com.apogames.game.question;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.DrawString;
import com.apogames.backend.GameScreen;
import com.apogames.common.Localization;
import com.apogames.help.Helper;

import java.util.ArrayList;

public class EmptyNextTile extends Question {

    private int answer = 0;

    private int tile = 0;

    private byte[][] currentTile;

    public EmptyNextTile(byte[][] solution, byte[][] solutionReal, int tile, byte[][] currentTile) {
        super();
        for (int y = 0; y < solution.length; y++) {
            for (int x = 0; x < solution[0].length; x++) {
                if (solutionReal[y][x] == 0) {
                    if (isNextTile(x, y, -1, 0, tile, solution) ||
                        isNextTile(x, y, +1, 0, tile, solution) ||
                        isNextTile(x, y, 0, -1, tile, solution) ||
                        isNextTile(x, y, 0, +1, tile, solution)) {
                        this.answer += 1;
                    }
                }
            }
        }
        this.tile = tile;
        this.currentTile = Helper.clone(currentTile);
        this.setQuestionEnum(QuestionEnum.EMPTY_NEXT_TILE);
    }

    private boolean isNextTile(int x, int y, int addX, int addY, int tile, byte[][] solution) {
        if (x + addX < 0 || x + addX >= solution[0].length || y + addY < 0 || y + addY >= solution.length) {
            return false;
        }
        return solution[y + addY][x + addX] == tile;
    }

    @Override
    public int getCosts() {
        return 2;
    }

    @Override
    public String getText() {
        String answer = "";
        answer = this.answer == 1 ? Localization.getInstance().getCommon().get("question_empty_next_tile_singular") : Localization.getInstance().getCommon().get("question_empty_next_tile") ;
        answer = answer.replace("{x}", String.valueOf(this.answer));
        return answer;
    }

    @Override
    public String getAnswer() {
        return String.valueOf(this.answer);
    }

    public boolean withAskCosts() {
        return false;
    }

    @Override
    public ArrayList<Integer> filter(ArrayList<byte[][]> solutionsReal, ArrayList<byte[][]> possibleSolutionsPossibilities, ArrayList<byte[][]> solutions) {
        ArrayList<Integer> results = new ArrayList<>();
        for (int i = 0; i < solutionsReal.size(); i++) {
            byte[][] currentSolution = solutions.get(i);
            byte[][] currentSolutionReal = solutionsReal.get(i);
            boolean found = true;
            int value = 0;
            for (int y = 0; y < currentSolution.length; y++) {
                for (int x = 0; x < currentSolution[0].length; x++) {
                    if (currentSolutionReal[y][x] == 0) {
                        if (isNextTile(x, y, -1, 0, this.tile, currentSolution) ||
                                isNextTile(x, y, +1, 0, this.tile, currentSolution) ||
                                isNextTile(x, y, 0, -1, this.tile, currentSolution) ||
                                isNextTile(x, y, 0, +1, this.tile, currentSolution)) {
                            value += 1;
                        }
                    }
                    if (value > this.answer) {
                        found = false;
                        break;
                    }
                }
                if (value > this.answer) {
                    found = false;
                    break;
                }
            }
            if (value != this.answer) {
                found = false;
            }
            if (found) {
                results.add(i);
            }
        }
        return results;
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
    }

    public void renderFilled(GameScreen screen, int changeX, int changeY) {
        super.renderFilled(screen, changeX, changeY);

        super.renderIsleFilled(screen, changeX, changeY, 20, this.currentTile);
    }

    public void renderSprite(GameScreen screen, int changeX, int changeY) {
        if (this.getX() >= 0) {
            super.renderSprite(screen, changeX, changeY);

            super.renderIsle(screen, changeX, changeY, 20, this.currentTile);
        }
    }
}
