package com.apogames.game.question;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.GameScreen;
import com.apogames.common.Localization;
import com.apogames.help.Helper;

import java.util.ArrayList;

public class OneTileCheck extends Question {

    private int sand = 0;
    private int grass = 0;
    private int forest = 0;

    private int tile = -1;

    private int cost = 0;

    private byte[][] currentTile;

    public OneTileCheck(int column, int row, byte[][] solution, byte[][] realSolution, int tile, byte[][] currentTile) {
        super();
        this.tile = tile;
        this.currentTile = Helper.clone(currentTile);

        this.sand = 0;
        this.grass = 0;
        this.forest = 0;
        if (column >= 0) {
            for (int y = 0; y < solution.length; y++) {
                if (realSolution[y][column] == 3 && solution[y][column] == tile) {
                    this.sand += 1;
                }
                if (realSolution[y][column] == 2 && solution[y][column] == tile) {
                    this.grass += 1;
                }
                if (realSolution[y][column] == 1 && solution[y][column] == tile) {
                    this.forest += 1;
                }
            }
            this.setColumn(column);
        } else if (row >= 0) {
            this.sand = 0;
            this.grass = 0;
            for (int x = 0; x < solution[0].length; x++) {
                if (realSolution[row][x] == 3 && solution[row][x] == tile) {
                    this.sand += 1;
                }
                if (realSolution[row][x] == 2 && solution[row][x] == tile) {
                    this.grass += 1;
                }
                if (realSolution[row][x] == 1 && solution[row][x] == tile) {
                    this.forest += 1;
                }
            }
            this.setRow(row);
        }

        int count = 0;
        for (int y = 0; y < currentTile.length; y++) {
            for (int x = 0; x < currentTile[0].length; x++) {
                if (currentTile[y][x] != 0) {
                    count += 1;
                }
            }
        }
        if (count <= 3) {
            this.cost = 2;
        } else {
            this.cost = 3;
        }
        this.setQuestionEnum(QuestionEnum.ONE_TILE_CHECK);
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

    @Override
    public int getCosts() {
        return this.cost;
    }

    @Override
    public String getText() {
        String answer = "";
        if (this.getRow() >= 0) {
            answer = Localization.getInstance().getCommon().get("question_one_tile_row");
            answer = answer.replace("{x}", String.valueOf(getRow()+1));
        } else if (this.getColumn() >= 0) {
            answer = Localization.getInstance().getCommon().get("question_one_tile_column");
            char r = (char)(String.valueOf(this.getColumn()).charAt(0) + 17);
            answer = answer.replace("{x}", String.valueOf(r));
        }
        answer = answer.replace("{sand}", String.valueOf(this.sand));
        answer = answer.replace("{grass}", String.valueOf(this.grass));
        answer = answer.replace("{forest}", String.valueOf(this.forest));
        answer = answer.replace("{tile}", getStringFromTile());
        return answer;
    }

    private String getStringFromTile() {
        StringBuilder s = new StringBuilder();
        for (int y = 0; y < currentTile.length; y++) {
            s.append("{");
            for (int x = 0; x < currentTile[0].length; x++) {
                if (currentTile[y][x] > 0) {
                    s.append("1");
                } else {
                    s.append("0");
                }
                if (x + 1 < currentTile[0].length) {
                    s.append(", ");
                } else {
                    s.append("} ");
                }
            }
        }
        return s.toString();
    }

    @Override
    public String getAnswer() {
        return this.sand +" "+ this.grass;
    }

    @Override
    public ArrayList<Integer> filter(ArrayList<byte[][]> solutionsReal, ArrayList<byte[][]> possibleSolutionsPossibilities, ArrayList<byte[][]> solutions) {
        ArrayList<Integer> results = new ArrayList<>();
        for (int i = 0; i < solutionsReal.size(); i++) {
            byte[][] currentSolution = solutionsReal.get(i);
            byte[][] solution = solutions.get(i);
            boolean found = true;
            int sand = 0;
            int grass = 0;
            int forest = 0;
            if (getColumn() >= 0) {
                for (int y = 0; y < solution.length; y++) {
                    if (currentSolution[y][getColumn()] == Constants.SAND && solution[y][getColumn()] == tile) {
                        sand += 1;
                    }
                    if (currentSolution[y][getColumn()] == Constants.GRAS && solution[y][getColumn()] == tile) {
                        grass += 1;
                    }
                    if (currentSolution[y][getColumn()] == Constants.FOREST && solution[y][getColumn()] == tile) {
                        forest += 1;
                    }
                }
                if (sand != this.sand || grass != this.grass || forest != this.forest) {
                    found = false;
                }
            } else if (getRow() >= 0) {
                for (int x = 0; x < currentSolution[0].length; x++) {
                    if (currentSolution[getRow()][x] == Constants.SAND && solution[getRow()][x] == tile) {
                        sand += 1;
                    }
                    if (currentSolution[getRow()][x] == Constants.GRAS && solution[getRow()][x] == tile) {
                        grass += 1;
                    }
                    if (currentSolution[getRow()][x] == Constants.FOREST && solution[getRow()][x] == tile) {
                        forest += 1;
                    }
                }
                if (sand != this.sand || grass != this.grass || forest != this.forest) {
                    found = false;
                }
            }

            if (found) {
                results.add(i);
            }
        }
        return results;
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

        float[] color = Constants.COLOR_GREEN_BRIGHT;
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

        super.renderIsle(screen, changeX - 70, changeY + 10, 15, this.currentTile);
    }
}
