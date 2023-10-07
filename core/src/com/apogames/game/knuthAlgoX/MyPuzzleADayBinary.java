package com.apogames.game.knuthAlgoX;

import com.apogames.game.tiles.ArcheOLogicTiles;
import com.apogames.game.tiles.Tile;

import java.util.ArrayList;
import java.util.Arrays;

public class MyPuzzleADayBinary {

    public static final byte[][] GOAL = new byte[][] {
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,0,0,0},
    };

    private byte[][] tileOneByte = new byte[][] {
            {0, 1},
            {0, 1},
            {0, 1},
            {1, 1},
    };

    private byte[][] tileTwoByte = new byte[][] {
            {1, 1},
            {1, 1},
            {1, 0},
    };

    private byte[][] tileThreeByte = new byte[][] {
            {1, 1},
            {1, 1},
            {1, 1},
    };

    private byte[][] tileFourByte = new byte[][] {
            {1, 1},
            {1, 0},
            {1, 1},
    };

    private byte[][] tileFiveByte = new byte[][] {
            {0, 1},
            {1, 1},
            {0, 1},
            {0, 1},
    };

    private byte[][] tileSixByte = new byte[][] {
            {1, 1, 1},
            {0, 0, 1},
            {0, 0, 1},
    };

    private byte[][] tileSevenByte = new byte[][] {
            {1, 1, 0},
            {0, 1, 0},
            {0, 1, 1},
    };

    private byte[][] tileEightByte = new byte[][] {
            {1, 0},
            {1, 0},
            {1, 1},
            {0, 1},
    };

    private Tile tileOne = new Tile(1, tileOneByte);
    private Tile tileTwo = new Tile(2, tileTwoByte);
    private Tile tileThree = new Tile(3, tileThreeByte);
    private Tile tileFour = new Tile(4, tileFourByte);
    private Tile tileFive = new Tile(5, tileFiveByte);
    private Tile tileSix = new Tile(6, tileSixByte);
    private Tile tileSeven = new Tile(7, tileSevenByte);
    private Tile tileEight = new Tile(8, tileEightByte);

    private ArrayList<Tile> allTiles;

    public MyPuzzleADayBinary() {
        if (this.allTiles == null || this.allTiles.size() == 0) {
            this.allTiles = new ArrayList<>();
            this.allTiles.add(tileOne);
            this.allTiles.add(tileTwo);
            this.allTiles.add(tileThree);
            this.allTiles.add(tileFour);
            this.allTiles.add(tileFive);
            this.allTiles.add(tileSix);
            this.allTiles.add(tileSeven);
            this.allTiles.add(tileEight);
        }
    }

    public MyPuzzleADayBinary(byte[][][] tiles) {
        if (this.allTiles == null || this.allTiles.size() == 0) {
            this.allTiles = new ArrayList<>();
            for (int i = 0; i < tiles.length; i++) {
                this.allTiles.add(new Tile(i+1, tiles[i]));
            }
        }
    }

    public MyPuzzleADayBinary(ArrayList<Tile> allTiles) {
        if (this.allTiles == null || this.allTiles.size() == 0) {
            this.allTiles = new ArrayList<>(allTiles);
        }
    }

    public ArrayList<Tile> getAllTiles() {
        return allTiles;
    }

    public byte[][] getMatrix(byte[][] goal) {
        byte[][] matrix = new byte[10000][goal.length * goal[0].length * 2 + allTiles.size()];

        int positionAllTiles = 0;
        int curPositionInMatrix = 0;
        for (Tile tile : this.allTiles) {
            int possibility = 1;
            for (byte[][] curTile : tile.getPossibilities()) {
                for (int y = 0; y < goal.length && y <= goal.length - curTile.length; y++) {
                    for (int x = 0; x < goal[0].length && x <= goal[0].length - curTile[0].length; x++) {
                        if (canPlaceTile(x, y, curTile, goal)) {
                            matrix[curPositionInMatrix] = createRowForTile(tile.getTileNumber(), possibility, curTile, x, y, matrix[curPositionInMatrix].length, goal);
                            curPositionInMatrix += 1;
                            positionAllTiles += 1;
                        }
                    }
                }
                possibility += 1;
            }
        }
        for (int y = 0; y < goal.length; y++) {
            for (int x = 0; x < goal[0].length; x++) {
                matrix[curPositionInMatrix] = createRowForTileRule(x, y, matrix[curPositionInMatrix].length, goal, positionAllTiles, 2);
                curPositionInMatrix += 1;
            }
        }
        for (int y = 0; y < goal.length; y++) {
            for (int x = 0; x < goal[0].length; x++) {
                matrix[curPositionInMatrix] = createRowForTileRule(x, y, matrix[curPositionInMatrix].length, goal, positionAllTiles, 3);
                curPositionInMatrix += 1;
            }
        }

        byte[][] returnMatrix = new byte[curPositionInMatrix + 1][goal.length * goal[0].length + allTiles.size()];
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

    private byte[] createRowForTileRule(int checkX, int checkY, int size, byte[][] goal, int maxPosition, int goalRule) {
        byte[] curMatrix = new byte[size];

        if (goal[checkY][checkX] == goalRule) {
            int position = (checkY) * goal[0].length + (checkX) % goal[0].length + (goal[checkY][checkX] + 1) * goal.length * goal[0].length;
            //curMatrix[position] = 1;
            int rulePosition = (checkY) * goal[0].length + (checkX) % goal[0].length + (goal[checkY][checkX] - 1) * goal.length * goal[0].length;
            //curMatrix[rulePosition] = 1;
        }

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
        byte[][] matrix = myPuzzleADayBinary.getMatrix(GOAL);

//        for (byte[] row : matrix) {
//            System.out.println(Arrays.toString(row));
//        }
//
//        System.out.println(matrix.length+" "+matrix[0].length);

        AlgorithmX x = new AlgorithmX();
        ArrayList<byte[][]> run = x.run(5, 5, 9, matrix);
        System.out.println(run.size());
    }
}
