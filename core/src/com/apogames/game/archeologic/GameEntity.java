package com.apogames.game.archeologic;

import com.apogames.Constants;
import com.apogames.game.tiles.ArcheOLogicTiles;
import com.apogames.game.tiles.Tile;

import java.util.ArrayList;

public class GameEntity {

    private byte[][] solution;
    private byte[][] solutionPossibilities;

    private ArrayList<GameTile> solutionTiles;

    private ArrayList<byte[][]> possibleSolutions;
    private ArrayList<byte[][]> possibleSolutionsPossibilities;

    private ArrayList<GameTile> currentTiles;

    public GameEntity() {
        init();
    }

    private void init() {
        if (currentTiles == null) {
            ArcheOLogicTiles currentTiles = new ArcheOLogicTiles();
            this.currentTiles = new ArrayList<>();
            for (Tile tile : currentTiles.getAllTiles()) {
                if (tile.getPossibilities().get(0).length != 1 || tile.getPossibilities().get(0)[0].length != 1) {
                    this.currentTiles.add(new GameTile(tile));
                }
            }

            int startX = 9 * Constants.TILE_SIZE;
            int startY = 3 * Constants.TILE_SIZE;

            for (GameTile tile : this.currentTiles) {
                tile.changePosition(startX, startY);

                startX += (tile.getTile().getPossibilities().get(tile.getCurrentTile())[0].length) * Constants.TILE_SIZE;
                if (startX >= 15 * Constants.TILE_SIZE) {
                    startX = 9 * Constants.TILE_SIZE;
                    startY += 3 * Constants.TILE_SIZE;
                }
            }
        }
    }

    public void choseNewSolution() {
        if (this.possibleSolutions.size() > 0) {
            int random = (int) (Math.random() * this.possibleSolutions.size());
            this.solution = this.possibleSolutions.get(random);
            this.solutionPossibilities = this.possibleSolutionsPossibilities.get(random);

            this.solutionTiles = new ArrayList<>();

            ArrayList<Integer> found = new ArrayList<>();
            for (int y = 0; y < this.solution.length; y++) {
                for (int x = 0; x < this.solution.length; x++) {
                    int index = (this.solution[y][x]);
                    if (this.solution[y][x] != 0 && this.solution[y][x] < 7 && !found.contains(index)) {
                        found.add(index);
                        foundAndAddSolutionTile(x, y);
                    }
                }
            }
            System.out.println(this.solutionTiles.size());
        }
    }

    private void foundAndAddSolutionTile(int givenX, int givenY) {
        int solutionIndex = (this.solution[givenY][givenX]);
        GameTile gameTile = this.currentTiles.get(solutionIndex - 1);

        int index = 0;
        for (byte[][] possibleTile : gameTile.getTile().getPossibilities()) {
            boolean foundSolution = false;
            for (int y = givenY - possibleTile.length + 1; y <= givenY; y++) {
                for (int x = givenX - possibleTile[0].length + 1; x <= givenX; x++) {
                    if (canPlaceTile(x, y, possibleTile, index)) {
                        GameTile nextTile = new GameTile(new Tile(solutionIndex, possibleTile));
                        nextTile.changePosition((2 + x) * Constants.TILE_SIZE, (3 + y) * Constants.TILE_SIZE);
                        this.solutionTiles.add(nextTile);
                        foundSolution = true;
                        break;
                    }
                }
                if (foundSolution) {
                    break;
                }
            }
            index += 1;
        }

    }

    private boolean canPlaceTile(int givenX, int givenY, byte[][] possibleTile, int index) {
        if (givenX < 0 || givenY < 0 ||
                givenX + possibleTile[0].length >= this.solution[0].length ||
                givenY + possibleTile.length >= this.solution.length) {
            return false;
        }
        for (int y = givenY; y < givenY + possibleTile.length; y++) {
            for (int x = givenX; x < givenX + possibleTile[0].length; x++) {
                if (possibleTile[y - givenY][x - givenX] != 0 && this.solutionPossibilities[y][x] != index) {
                    return false;
                }
            }
        }

        return true;
    }

    public void setAllSolutions(ArrayList<byte[][]> possibleSolutions, ArrayList<byte[][]> possibleSolutionsPossibilities) {
        this.possibleSolutions = new ArrayList<>(possibleSolutions);
        this.possibleSolutionsPossibilities = new ArrayList<>(possibleSolutionsPossibilities);
        this.choseNewSolution();
    }

    public ArrayList<byte[][]> getPossibleSolutions() {
        return possibleSolutions;
    }

    public ArrayList<GameTile> getSolutionTiles() {
        return solutionTiles;
    }

    public void mouseMoved(int mouseX, int mouseY) {
        for (GameTile tile : this.currentTiles) {
            tile.isIn(mouseX, mouseY);
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
}
