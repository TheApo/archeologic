package com.apogames.game.question;

import com.apogames.Constants;
import com.apogames.backend.GameScreen;
import com.apogames.common.Localization;
import com.apogames.help.Helper;

import java.util.ArrayList;
import java.util.Locale;

public class TileNextTile extends Question {

    private boolean next = false;

    private int tile = 0;

    private byte[][] currentTile;
    private int otherTile = 0;

    private byte[][] currentOtherTile;

    public TileNextTile(byte[][] solution, int tile, byte[][] currentTile, int otherTile, byte[][] currentOtherTile) {
        super();
        for (int y = 0; y < solution.length; y++) {
            for (int x = 0; x < solution[0].length; x++) {
                if (solution[y][x] == otherTile) {
                    if (isNextTile(x, y, -1, 0, tile, solution) ||
                        isNextTile(x, y, +1, 0, tile, solution) ||
                        isNextTile(x, y, 0, -1, tile, solution) ||
                        isNextTile(x, y, 0, +1, tile, solution)) {
                        this.next = true;
                        break;
                    }
                }
            }
            if (this.next) {
                break;
            }
        }
        this.tile = tile;
        this.currentTile = Helper.clone(currentTile);
        this.otherTile = otherTile;
        this.currentOtherTile = Helper.clone(currentOtherTile);
        this.setQuestionEnum(QuestionEnum.TILE_NEXT_TILE);
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
        answer = Localization.getInstance().getCommon().get("question_tile_next_tile") ;
        if (!this.next) {
            answer = answer.replace("{x}", " " + Localization.getInstance().getCommon().get("question_not"));
        } else {
            answer = answer.replace("{x}", "");
        }
        return answer;
    }

    @Override
    public String getAnswer() {
        return String.valueOf(this.next);
    }

    public boolean withAskCosts() {
        return false;
    }

    @Override
    public ArrayList<Integer> filter(ArrayList<byte[][]> solutionsReal, ArrayList<byte[][]> possibleSolutionsPossibilities, ArrayList<byte[][]> solutions) {
        ArrayList<Integer> results = new ArrayList<>();
        for (int i = 0; i < solutionsReal.size(); i++) {
            byte[][] currentSolution = solutions.get(i);
            boolean found = true;
            boolean isNextTo = false;
            for (int y = 0; y < currentSolution.length; y++) {
                for (int x = 0; x < currentSolution[0].length; x++) {
                    if (currentSolution[y][x] == this.otherTile) {
                        if (isNextTile(x, y, -1, 0, this.tile, currentSolution) ||
                                isNextTile(x, y, +1, 0, this.tile, currentSolution) ||
                                isNextTile(x, y, 0, -1, this.tile, currentSolution) ||
                                isNextTile(x, y, 0, +1, this.tile, currentSolution)) {
                            isNextTo = true;
                        }
                    }
                    if (isNextTo) {
                        break;
                    }
                }
                if (isNextTo) {
                    break;
                }
            }
            if (isNextTo != this.next) {
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
        addX += 100;
        addY -= 5;

        int extraAddY = 0;
        if (this.currentTile.length == 1) {
            extraAddY += size;
        } else if (this.currentTile.length == 2) {
            extraAddY += size/2;
        }
        int startY = addY - this.currentTile.length*size/2;
        screen.getRenderer().setColor(Constants.COLOR_BLACK[0], Constants.COLOR_BLACK[1], Constants.COLOR_BLACK[2], 1f);
        for (int y = 0; y < currentTile.length; y++) {
            for (int x = 0; x < currentTile[0].length; x++) {
                if (this.currentTile[y][x] != 0) {
                    screen.getRenderer().rect(addX + 1 + x * size, startY + 1 + y * size + extraAddY, size - 2, size - 2);
                }
            }
        }

        addX += 130;
        if (Localization.getInstance().getLocale() != Locale.ENGLISH && Localization.getInstance().getLocale() != Locale.UK && Localization.getInstance().getLocale() != Locale.US) {
            addX += 50;
        }
        if (!this.next) {
            addX += 50;
        }
        extraAddY = 0;
        if (this.currentOtherTile.length == 1) {
            extraAddY += size;
        } else if (this.currentOtherTile.length == 2) {
            extraAddY += size/2;
        }
        for (int y = 0; y < currentOtherTile.length; y++) {
            for (int x = 0; x < currentOtherTile[0].length; x++) {
                if (this.currentOtherTile[y][x] != 0) {
                    screen.getRenderer().rect(addX + 1 + x * size, startY + 1 + y * size + extraAddY, size - 2, size - 2);
                }
            }
        }
    }

    public void renderSprite(GameScreen screen, int changeX, int changeY) {
        super.renderSprite(screen, changeX, changeY);

        super.renderIsle(screen, changeX - 40, changeY + 10, 15, this.currentTile);

        super.renderIsle(screen, changeX + 10, changeY + 10, 15, this.currentOtherTile);
    }
}
