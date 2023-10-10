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
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,0,0,0},
    };

    private final ArrayList<Tile> allTiles;

    public MyPuzzleADayBinary(ArrayList<Tile> allTiles) {
        this.allTiles = new ArrayList<>(allTiles);
    }

    public byte[][] getMatrix(byte[][] goal) {
        int[] valueUntil = new int[40];
        byte[][] matrix = new byte[10000][goal.length * goal[0].length * 2 + allTiles.size()];

        int curPositionInMatrix = 0;
        for (Tile tile : this.allTiles) {
            int possibility = 1;
            for (byte[][] curTile : tile.getPossibilities()) {
                for (int y = 0; y < goal.length && y <= goal.length - curTile.length; y++) {
                    for (int x = 0; x < goal[0].length && x <= goal[0].length - curTile[0].length; x++) {
                        if (canPlaceTile(x, y, curTile, goal)) {
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
        SevenArcheOLogicTiles logic = new SevenArcheOLogicTiles();
        MyPuzzleADayBinary myPuzzleADayBinary = new MyPuzzleADayBinary(logic.getAllTiles());
        byte[][] matrix = myPuzzleADayBinary.getMatrix(GOAL);

        for (byte[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }

        System.out.println(matrix.length+" "+matrix[0].length);

        AlgorithmX x = new AlgorithmX();
        ArrayList<byte[][]> run = x.run(5, 5, logic.getAllTiles().size(), logic.getMaxTile(), matrix);
        System.out.println(run.size());
    }
}
