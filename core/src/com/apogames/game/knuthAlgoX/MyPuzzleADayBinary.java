package com.apogames.game.knuthAlgoX;

import com.apogames.game.tiles.ArcheOLogicTiles;
import com.apogames.game.tiles.FiveArcheOLogicTiles;
import com.apogames.game.tiles.SevenArcheOLogicTiles;
import com.apogames.game.tiles.Tile;

import java.util.ArrayList;
import java.util.Arrays;

public class MyPuzzleADayBinary {

    public static final byte[][] GOAL = new byte[][] {
            {0,0,0,0,0},
            {0,0,0,3,0},
            {0,0,0,0,0},
            {2,0,0,0,0},
            {0,0,0,0,0},
    };

    public static final byte[][] SOLUTION = new byte[][] {
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,1,1,0},
            {0,1,1,0,0},
            {0,0,0,0,0},
    };

    public static final byte[][] REALSOLUTION = new byte[][] {
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,1,1,0},
            {0,2,1,0,0},
            {0,0,0,0,0},
    };

    private byte[][] randomSolution = null;

    private byte[][] randomRealSolution = null;

    private final ArrayList<Tile> allTiles;

    private final ArrayList<PlacedTileHelper> usedTiles = new ArrayList<>();

    public MyPuzzleADayBinary(ArrayList<Tile> allTiles) {
        this.allTiles = new ArrayList<>(allTiles);
    }

    public ArrayList<Tile> getAllTiles() {
        return allTiles;
    }

    public ArrayList<PlacedTileHelper> getUsedTiles() {
        return usedTiles;
    }

    public byte[][] getRandomSolution() {
        return randomSolution;
    }

    public byte[][] getMatrix(byte[][] goal) {
        if (this.randomSolution == null) {
            this.randomSolution = new byte[goal.length][goal[0].length];
            this.randomRealSolution = new byte[goal.length][goal[0].length];
        }
        return getMatrix(goal, this.randomSolution, this.randomRealSolution);
    }

    public void setRandomSolution(int sizeX, int sizeY, int tileSize) {
        this.randomSolution = new byte[sizeY][sizeX];
        this.randomRealSolution = new byte[sizeY][sizeX];
        int count = 0;
        this.usedTiles.clear();
        while (count < tileSize) {
            byte[][] randomTile = getRandomTile(usedTiles);
            placeTileInRandomSolution(randomTile);
            count += 1;
        }
    }

    private byte[][] copy(byte[][] array) {
        byte[][] copy = new byte[array.length][array[0].length];

        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[0].length; x++) {
                copy[y][x] = array[y][x];
            }
        }

        return copy;
    }

    private void placeTileInRandomSolution(byte[][] randomTile) {
        byte[][] nextRandomSolution = copy(this.randomSolution);
        byte[][] nextRealRandomSolution = copy(this.randomRealSolution);

        int startX = (int)(Math.random() * (this.randomSolution[0].length - randomTile[0].length + 1));
        int startY = (int)(Math.random() * (this.randomSolution.length - randomTile.length + 1));

        for (int y = startY; y < startY + randomTile.length; y++) {
            for (int x = startX; x < startX + randomTile[0].length; x++) {
                if (randomTile[y - startY][x - startX] != 0 && nextRandomSolution[y][x] != 0) {
                    placeTileInRandomSolution(randomTile);
                    return;
                } else if (randomTile[y - startY][x - startX] != 0) {
                    nextRandomSolution[y][x] = (byte)this.usedTiles.get(this.usedTiles.size() - 1).getTileNumber();
                    nextRealRandomSolution[y][x] = randomTile[y - startY][x - startX];
                }
            }
        }

        this.usedTiles.get(this.usedTiles.size() - 1).setPlacedX(startX);
        this.usedTiles.get(this.usedTiles.size() - 1).setPlacedY(startY);
        this.randomSolution = nextRandomSolution;
        this.randomRealSolution = nextRealRandomSolution;
    }

    private byte[][] getRandomTile(ArrayList<PlacedTileHelper> usedTiles) {
        int random;
        byte[][] result;
        int index;
        boolean stayIn;
        do {
            stayIn = false;
            random = (int) (Math.random() * this.allTiles.size());
            Tile tile = this.allTiles.get(random);
            index = (int) (tile.getPossibilities().size() * Math.random());
            result = tile.getPossibilities().get(index);
            for (PlacedTileHelper helper : usedTiles) {
                if (helper.getTileNumber() == this.allTiles.get(random).getTileNumber()) {
                    stayIn = true;
                    break;
                }
            }
        } while (stayIn || result.length * result[0].length < 4);
        usedTiles.add(new PlacedTileHelper(this.allTiles.get(random).getTileNumber(), index));
        return result;
    }

    public byte[][] getMatrix(byte[][] goal, byte[][] realSolution, byte[][] randomRealSolution) {
        byte[][] matrix = new byte[10000][goal.length * goal[0].length * 2 + allTiles.size()];

        int curPositionInMatrix = 0;
        for (Tile tile : this.allTiles) {
            int possibility = 1;
            boolean needToFind = isTileInRealSolution(realSolution, tile.getTileNumber());
            for (byte[][] curTile : tile.getPossibilities()) {
                for (int y = 0; y < goal.length && y <= goal.length - curTile.length; y++) {
                    for (int x = 0; x < goal[0].length && x <= goal[0].length - curTile[0].length; x++) {
                        if (canPlaceTile(x, y, curTile, goal) && isCorrectTileAndInPosition(x, y, curTile, realSolution, randomRealSolution, tile.getTileNumber(), needToFind)) {
                            matrix[curPositionInMatrix] = createRowForTile(tile.getTileNumber(), possibility, curTile, x, y, matrix[curPositionInMatrix].length, goal);
                            curPositionInMatrix += 1;
                        }
                    }
                }
                possibility += 1;
            }
        }

        byte[][] returnMatrix = new byte[curPositionInMatrix + 1][goal.length * goal[0].length * 2 + allTiles.size()];
        System.arraycopy(matrix, 0, returnMatrix, 0, curPositionInMatrix + 1);

        for (int j = 0; j < returnMatrix[0].length; j++) {
            boolean found = false;
            for (int i = 0; i < curPositionInMatrix; i++) {
                if (matrix[i][j] != 0) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                matrix[curPositionInMatrix][j] = 1;
            }
        }

        return returnMatrix;
    }

    private boolean isTileInRealSolution(byte[][] realSolution, int tileNumber) {
        for (int y = 0; y < realSolution.length; y++) {
            for (int x = 0; x < realSolution[0].length; x++) {
                if (realSolution[y][x] == tileNumber) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCorrectTileAndInPosition(int checkX, int checkY, byte[][] curTile, byte[][] realSolution, byte[][] randomRealSolution, int tileNumber, boolean needToFind) {
        if (!needToFind) {
            return true;
        }

        for (int y = 0; y < curTile.length; y++) {
            for (int x = 0; x < curTile[0].length; x++) {
                if (realSolution[checkY+y][checkX+x] == tileNumber && curTile[y][x] != randomRealSolution[checkY+y][checkX+x]) {
                    return false;
                }
                if (realSolution[checkY+y][checkX+x] != tileNumber && curTile[y][x] != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    private byte[] createRowForTile(int tileNumber, int realTileNumber, byte[][] curTile, int checkX, int checkY, int size, byte[][] goal) {
        byte[] curMatrix = new byte[size];

        for (int y = 0; y < curTile.length; y++) {
            for (int x = 0; x < curTile[0].length; x++) {
                if (goal[checkY+y][checkX+x] >= 0 && curTile[y][x] >= 1) {
                    int position = (checkY+y) * goal[0].length + (checkX+x) % goal[0].length;
                    curMatrix[position] = 1;
                    if (curTile[y][x] == 2 && goal[checkY+y][checkX+x] == 2) {
                        position = (checkY+y) * goal[0].length + (checkX+x) % goal[0].length + goal.length * goal[0].length;
                        curMatrix[position] = 1;
                    }
                    if (curTile[y][x] == 3 && goal[checkY+y][checkX+x] == 3) {
                        position = (checkY+y) * goal[0].length + (checkX+x) % goal[0].length + goal.length * goal[0].length;
                        curMatrix[position] = 1;
                    }
                }
            }
        }
        curMatrix[goal.length * goal[0].length * 2 + tileNumber - 1] = (byte)realTileNumber;

        return curMatrix;
    }

    private boolean canPlaceTile(int checkX, int checkY, byte[][] curTile, byte[][] goal) {
        for (int y = 0; y < curTile.length; y++) {
            for (int x = 0; x < curTile[0].length; x++) {
                if (goal[checkY+y][checkX+x] < 0 && curTile[y][x] >= 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        ArcheOLogicTiles logic = new ArcheOLogicTiles();
        MyPuzzleADayBinary myPuzzleADayBinary = new MyPuzzleADayBinary(logic.getAllTiles());
        //myPuzzleADayBinary.setRandomSolution(5, 5, 1);
        byte[][] matrix = myPuzzleADayBinary.getMatrix(GOAL, SOLUTION, REALSOLUTION);

        for (byte[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }

        System.out.println(matrix.length+" "+matrix[0].length);

        AlgorithmX x = new AlgorithmX();
        ArrayList<byte[][]> run = x.run(5, 5, logic.getAllTiles().size(), logic.getMaxTile(), matrix);
        System.out.println(run.size());

        if (!run.isEmpty()) {
            byte[][] array = run.get(0);
            for (byte[] row : array) {
                System.out.println(Arrays.toString(row));
            }
            System.out.println();
        } else {
            System.out.println();
        }
    }
}
