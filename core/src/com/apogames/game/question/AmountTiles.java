package com.apogames.game.question;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.GameScreen;
import com.apogames.common.Localization;

import java.util.ArrayList;
import java.util.HashSet;

public class AmountTiles extends Question {

    private int answer = 0;

    public AmountTiles(int column, int row, byte[][] solution) {
        super();
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
    public ArrayList<Integer> filter(ArrayList<byte[][]> solutionsReal, ArrayList<byte[][]> possibleSolutionsPossibilities, ArrayList<byte[][]> possibleSolutions) {
        ArrayList<Integer> results = new ArrayList<>();
        for (int i = 0; i < possibleSolutions.size(); i++) {
            byte[][] currentSolution = possibleSolutions.get(i);
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

    public void renderFilled(GameScreen screen, int changeX, int changeY) {
        if (this.getX() < 0) {
            return;
        }

        super.renderFilled(screen, changeX, changeY);

        int size = 80;
        float startX = getX() + changeX + getWidth()/2f - size/2f;
        float startY = getY() + changeY + 5;

        float calc = size / (float) AssetLoader.boardTextureLittleRegion.getRegionWidth();

        float[] color = Constants.COLOR_BLUE_BRIGHT;
        screen.getRenderer().setColor(color[0], color[1], color[2], 1f);

        if (this.getColumn() >= 0) {
            screen.getRenderer().rect(startX + 18 * calc + this.getColumn() * 17 * calc, startY + 18 * calc, 17 * calc, 5 * 17 * calc);
        }
        if (this.getRow() >= 0) {
            screen.getRenderer().rect(startX + 18 * calc, startY + 18 * calc + this.getRow() * 17 * calc, 5 * 17 * calc, 17 * calc);
        }
    }

    public void renderSprite(GameScreen screen, int changeX, int changeY) {
        if (this.getX() < 0) {
            return;
        }
        super.renderSprite(screen, changeX, changeY);
        int size = 80;
        float startX = getX() + changeX + getWidth()/2f - size/2f;
        float startY = getY() + changeY + 5;
        screen.spriteBatch.draw(AssetLoader.boardTextureLittleRegion, startX, startY, size, size);
    }
}
