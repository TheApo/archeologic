package com.apogames.game.archeologic;

import com.apogames.Constants;
import com.apogames.game.knuthAlgoX.AlgorithmX;
import com.apogames.game.knuthAlgoX.MyPuzzleADayBinary;
import com.apogames.game.menu.Difficulty;
import com.apogames.game.question.OneSpecificPosition;
import com.apogames.game.question.Question;
import com.apogames.game.tiles.*;

import java.util.ArrayList;

public class GameEntity {

    public static final int MAX_SHOWN_QUESTION = 18;
    private Difficulty difficulty = Difficulty.EASY;
    private byte[][] solution;
    private byte[][] solutionPossibilities;

    private byte[][] realSolution;

    private ArrayList<GameTile> solutionTiles;

    private final ArrayList<GameTile> hiddenTiles = new ArrayList<>();

    private ArrayList<byte[][]> possibleSolutions;
    private ArrayList<byte[][]> possibleSolutionsPossibilities;
    private final ArrayList<byte[][]> possibleSolutionsReal = new ArrayList<>();

    private ArrayList<GameTile> currentTiles;

    private final ArrayList<Question> questions = new ArrayList<>();
    private int curStartQuestion = 0;

    private GivenTiles givenTiles;

    private int costs = 0;

    public GameEntity() {
        init();
    }

    private void init() {
        if (currentTiles == null) {
            setGivenTiles(6);
        }
    }

    public void setGivenTiles(int tiles) {
        if (tiles == 5) {
            this.givenTiles = new FiveArcheOLogicTiles();
        } else if (tiles == 7) {
            this.givenTiles = new SevenArcheOLogicTiles();
        } else {
            this.givenTiles = new ArcheOLogicTiles();
        }
        this.currentTiles = new ArrayList<>();
        for (int i = 0; i < this.givenTiles.getAllTiles().size(); i++) {
            Tile tile = this.givenTiles.getAllTiles().get(i);
            if (tile.getTileNumber() <= this.givenTiles.getMaxTile()) {
                this.currentTiles.add(new GameTile(tile));
            }
        }
        resetTiles();
    }

    public int getCosts() {
        return costs;
    }

    public void resetTiles() {
        int startX = Constants.TILE_SIZE;
        int startY = 9 * Constants.TILE_SIZE;

        for (GameTile tile : this.currentTiles) {
            tile.changePosition(startX, startY);

            startX += (tile.getTile().getPossibilities().get(tile.getCurrentTile())[0].length) * Constants.TILE_SIZE;
            if (startX >= 11 * Constants.TILE_SIZE) {
                startX = 8 * Constants.TILE_SIZE;
                startY -= 3 * Constants.TILE_SIZE;
            }
        }
        for (int i = this.questions.size() - 1; i >= this.givenTiles.getDifficultyTiles()[this.difficulty.getGivenTiles()]; i--) {
            this.questions.remove(this.questions.get(i));
        }
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void choseNewSolution() {
        this.costs = 0;
        this.questions.clear();
        this.curStartQuestion = 0;

        int xSize = 5;
        int ySize = 5;

        byte[][] goal = new byte[ySize][xSize];
        int size = this.givenTiles.getDifficultyTiles()[this.difficulty.getGivenTiles()];
        prefillGoal(goal, size);

        MyPuzzleADayBinary myPuzzleADayBinary = new MyPuzzleADayBinary(givenTiles.getAllTiles());
        byte[][] matrix = myPuzzleADayBinary.getMatrix(goal);

        AlgorithmX algoX = new AlgorithmX();
        algoX.run(xSize, ySize, givenTiles.getAllTiles().size(), givenTiles.getMaxTile(), matrix);

        if (algoX.allSolutions.size() < 10) {
            choseNewSolution();
        } else {
            this.setAllSolutions(algoX.allSolutions, algoX.allValueSolutions);

            int random = (int) (Math.random() * this.possibleSolutions.size());
            this.solution = this.possibleSolutions.get(random);
            this.solutionPossibilities = this.possibleSolutionsPossibilities.get(random);
            this.realSolution = new byte[this.solution.length][this.solution[0].length];

            this.solutionTiles = new ArrayList<>();

            ArrayList<Integer> found = new ArrayList<>();
            for (int y = 0; y < this.solution.length; y++) {
                for (int x = 0; x < this.solution.length; x++) {
                    int index = (this.solution[y][x]);
                    if (this.solution[y][x] != 0 && this.solution[y][x] < 8 && !found.contains(index)) {
                        found.add(index);
                        GameTile nextTile = new GameTile(new Tile(index, this.getTileFromTileNumber(index).getTile().getPossibilities().get(this.solutionPossibilities[y][x] - 1)));
                        this.solutionTiles.add(nextTile);
                        foundAndAddSolutionTile(x, y, nextTile);
                    }
                }
            }
        }
    }

    public ArrayList<GameTile> getHiddenTiles() {
        return hiddenTiles;
    }

    private GameTile getTileFromTileNumber(int tileNumber) {
        for (GameTile tile : this.currentTiles) {
            if (tile.getTile().getTileNumber() == tileNumber) {
                return tile;
            }
        }
        return this.currentTiles.get(0);
    }

    private void foundAndAddSolutionTile(int givenX, int givenY, GameTile nextTile) {
        int solutionIndex = (this.solution[givenY][givenX]);
        GameTile gameTile = this.currentTiles.get(solutionIndex - 1);

        byte[][] possibleTile = gameTile.getTile().getPossibilities().get(this.solutionPossibilities[givenY][givenX] - 1);
        boolean foundSolution = false;
        for (int y = givenY - possibleTile.length + 1; y <= givenY; y++) {
            for (int x = givenX - possibleTile[0].length + 1; x <= givenX; x++) {
                if (canPlaceTile(x, y, possibleTile, solutionIndex, this.solution)) {
                    nextTile.changePosition((2 + x) * Constants.TILE_SIZE, (3 + y) * Constants.TILE_SIZE);
                    placeInSolution(x, y, possibleTile, this.realSolution);
                    foundSolution = true;
                    break;
                }
            }
            if (foundSolution) {
                break;
            }
        }

    }

    private void placeInSolution(int givenX, int givenY, byte[][] possibleTile, byte[][] realSolution) {
        for (int y = givenY; y < givenY + possibleTile.length; y++) {
            for (int x = givenX; x < givenX + possibleTile[0].length; x++) {
                if (possibleTile[y - givenY][x - givenX] != 0) {
                    realSolution[y][x] = possibleTile[y - givenY][x - givenX];
                }
            }
        }
    }

    private boolean canPlaceTile(int givenX, int givenY, byte[][] possibleTile, int tileNumber, byte[][] solution) {
        if (givenX < 0 || givenY < 0 ||
                givenX + possibleTile[0].length > solution[0].length ||
                givenY + possibleTile.length > solution.length) {
            return false;
        }
        for (int y = givenY; y < givenY + possibleTile.length; y++) {
            for (int x = givenX; x < givenX + possibleTile[0].length; x++) {
                if (possibleTile[y - givenY][x - givenX] != 0 && solution[y][x] != tileNumber) {
                    return false;
                }
            }
        }

        return true;
    }

    public void setNewLevel(Difficulty difficulty) {
        this.difficulty = difficulty;

        this.choseNewSolution();
    }

    public void prefillGoal(byte[][] goal, int size) {
        int count = size;
        this.getHiddenTiles().clear();
        ArrayList<Byte> values = new ArrayList<>();
        for (GameTile tile : this.currentTiles) {
            byte[][] bytes = tile.getTile().getPossibilities().get(0);
            for (int y = 0; y < bytes.length; y++) {
                for (int x = 0; x < bytes[0].length; x++) {
                    if (bytes[y][x] > 1) {
                        values.add(bytes[y][x]);
                    }
                }
            }
        }

        while (count > 0) {
            int x = (int)(Math.random() * goal[0].length);
            int y = (int)(Math.random() * goal.length);
            if (goal[y][x] == 0) {
                goal[y][x] = values.remove((int)(Math.random() * values.size()));
                GameTile gameTile = new GameTile(new Tile(1, new byte[][]{{goal[y][x]}}));
                gameTile.changePosition((2 + x) * Constants.TILE_SIZE, (3+y) * Constants.TILE_SIZE);
                this.getHiddenTiles().add(gameTile);
                this.getQuestions().add(new OneSpecificPosition(x, y, goal));
                count -= 1;
            }
        }
    }


    public boolean isWon() {
        for (GameTile tile : this.currentTiles) {
            byte[][] bytes = tile.getTile().getPossibilities().get(tile.getCurrentTile());
            if (tile.getGameX() < 0 || tile.getGameX() + bytes[0].length > this.solution[0].length ||
                    tile.getGameY() < 0 || tile.getGameY() + bytes.length > this.solution.length) {
                return false;
            }
            boolean found = true;
            for (int y = 0; y < bytes.length; y++) {
                for (int x = 0; x < bytes[0].length; x++) {
                    if (bytes[y][x] != 0 && (this.solution[tile.getGameY()+y][tile.getGameX()+x] != tile.getTile().getTileNumber() || this.realSolution[tile.getGameY()+y][tile.getGameX()+x] != bytes[y][x])) {
                        found = false;
                        break;
                    }
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    public void setAllSolutions(ArrayList<byte[][]> possibleSolutions, ArrayList<byte[][]> possibleSolutionsPossibilities) {
        this.possibleSolutions = new ArrayList<>(possibleSolutions);
        this.possibleSolutionsPossibilities = new ArrayList<>(possibleSolutionsPossibilities);

        this.possibleSolutionsReal.clear();
        for (int i = 0; i < this.possibleSolutions.size(); i++) {
            byte[][] bytes = this.possibleSolutions.get(i);
            byte[][] realSolution = new byte[bytes.length][bytes[0].length];
            ArrayList<Byte> found = new ArrayList<>();
            for (int y = 0; y < bytes.length; y++) {
                for (int x = 0; x < bytes[0].length; x++) {
                    if (bytes[y][x] > 0 && bytes[y][x] <= this.givenTiles.getMaxTile() && !found.contains(bytes[y][x])) {
                        found.add(bytes[y][x]);
                        placeInReal(x, y, realSolution, bytes, this.possibleSolutionsPossibilities.get(i)[y][x]);
                    }
                }
            }
            this.possibleSolutionsReal.add(realSolution);
        }
    }

    private void placeInReal(int givenX, int givenY, byte[][] realSolution, byte[][] givenArray, byte possibility) {
        byte[][] bytes = this.currentTiles.get(givenArray[givenY][givenX] - 1).getTile().getPossibilities().get(possibility-1);

        boolean foundSolution = false;
        for (int y = givenY - bytes.length + 1; y <= givenY; y++) {
            for (int x = givenX - bytes[0].length + 1; x <= givenX; x++) {
                if (canPlaceTileReal(x, y, bytes, givenArray, realSolution, givenArray[givenY][givenX])) {
                    placeInSolution(x, y, bytes, realSolution);
                    foundSolution = true;
                    break;
                }
            }
            if (foundSolution) {
                break;
            }
        }
    }

    private boolean canPlaceTileReal(int givenX, int givenY, byte[][] possibleTile, byte[][] givenArray, byte[][] realSolution, int tileNumber) {
        if (givenX < 0 || givenY < 0 ||
                givenX + possibleTile[0].length > realSolution[0].length ||
                givenY + possibleTile.length > realSolution.length) {
            return false;
        }
        for (int y = givenY; y < givenY + possibleTile.length; y++) {
            for (int x = givenX; x < givenX + possibleTile[0].length; x++) {
                if (possibleTile[y - givenY][x - givenX] != 0 && (realSolution[y][x] != 0 || givenArray[y][x] != tileNumber)) {
                    return false;
                }
            }
        }

        return true;
    }

    public byte[][] getRealSolution() {
        return realSolution;
    }

    public void mouseMoved(int mouseX, int mouseY) {
        for (GameTile tile : this.currentTiles) {
            tile.isIn(mouseX, mouseY);
            tile.setXAndYToNextXAndNextY();
        }
    }

    public void mouseButtonReleased(int mouseX, int mouseY, boolean isRightButton) {
        for (GameTile tile : this.currentTiles) {
            tile.isIn(mouseX, mouseY);
            if (tile.click(mouseX, mouseY)) {
                break;
            }
        }

    }

    public void mousePressed(int x, int y, boolean isRightButton) {
        for (GameTile tile : this.currentTiles) {
            if (tile.pressMouse(x, y)) {
                break;
            }
        }
    }

    public void mouseDragged(int x, int y, boolean isRightButton) {
        for (GameTile tile : this.currentTiles) {
            if (tile.dragTile(x, y)) {
                break;
            }
        }
    }

    public byte[][] getSolution() {
        return solution;
    }

    public ArrayList<GameTile> getCurrentTiles() {
        return currentTiles;
    }

    public void setCosts(int costs) {
        this.costs = costs;
    }

    public int getCurStartQuestion() {
        return curStartQuestion;
    }

    public void addCurStartQuestion(int add) {
        if (this.curStartQuestion + add < 0) {
            this.curStartQuestion = 0;
        } else if (this.curStartQuestion + add + MAX_SHOWN_QUESTION <= this.questions.size()) {
            this.curStartQuestion += add;
        }
    }
}
