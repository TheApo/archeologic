package com.apogames.game.tiles;

import com.apogames.help.Helper;

import java.util.ArrayList;

public class Tile {

    private final int tileNumber;
    private ArrayList<byte[][]> possibilities;

    private int currentPossibilities;

    private boolean withMirror;
    public Tile(int tileNumber, byte[][] tileArray) {
        this(tileNumber, tileArray, false);
    }

    public Tile(int tileNumber, byte[][] tileArray, boolean withMirror) {
        this.tileNumber = tileNumber;

        this.possibilities = new ArrayList<>();

        this.currentPossibilities = 0;

        this.withMirror = withMirror;

        this.possibilities.add(tileArray);
        if (withMirror) {
            this.possibilities.add(mirror(tileArray));
        }

        byte[][] rightRotation = addRightRotation(tileArray);
        byte[][] nextRightRotation = addRightRotation(rightRotation);
        addRightRotation(nextRightRotation);

        removeDoubleEntries();
    }

    public final int getTileNumber() {
        return tileNumber;
    }

    public ArrayList<byte[][]> getPossibilities() {
        return possibilities;
    }

    public int getCurrentPossibilities() {
        return currentPossibilities;
    }

    public void setCurrentPossibilities(int currentPossibilities) {
        this.currentPossibilities = currentPossibilities;
        if (this.currentPossibilities >= this.possibilities.size()) {
            this.currentPossibilities = 0;
        }
        if (this.currentPossibilities < 0) {
            this.currentPossibilities = this.possibilities.size() - 1;
        }
    }

    private byte[][] addRightRotation(byte[][] tileArray) {
        byte[][] rightRotation = rightRotation(tileArray);
        this.possibilities.add(rightRotation);
        if (this.withMirror) {
            this.possibilities.add(mirror(rightRotation));
        }

        return rightRotation;
    }

    private byte[][] mirror(byte[][] tileArray) {
        byte[][] mirror = new byte[tileArray.length][tileArray[0].length];
        for (int i = 0; i < tileArray.length; i++) {
            System.arraycopy(tileArray[i], 0, mirror[tileArray.length - 1 - i], 0, tileArray[0].length);
        }
        return mirror;
    }

    private byte[][] rightRotation(byte[][] tileArray) {
        byte[][] rightRotation = new byte[tileArray[0].length][tileArray.length];
        for (int i = 0; i < tileArray.length; i++) {
            for (int j = 0; j < tileArray[0].length; j++) {
                rightRotation[tileArray[0].length - 1 - j][i] = tileArray[i][j];
            }
        }
        return rightRotation;
    }

    private void removeDoubleEntries() {
        ArrayList<byte[][]> newResult = new ArrayList<>();
        for (byte[][] result : this.possibilities) {
            boolean same = false;
            for (byte[][] curResult : newResult) {
                if (Helper.equal(curResult, result)) {
                    same = true;
                    break;
                }
            }
            if (!same) {
                newResult.add(result);
            }
        }
        this.possibilities = newResult;
    }
}
