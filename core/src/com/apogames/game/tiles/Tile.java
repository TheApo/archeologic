package com.apogames.game.tiles;

import com.apogames.help.Helper;

import java.util.ArrayList;

public class Tile {

    private final int tileNumber;
    private ArrayList<byte[][]> possibilities;
    private ArrayList<ArrayList<Integer>[][]> waterSurround;

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

        this.waterSurround = new ArrayList<>();
        for (byte[][] possibility : this.possibilities) {
            ArrayList<Integer>[][] list = new ArrayList[possibility.length][possibility[0].length];

            for (int y = 0; y < possibility.length; y++) {
                for (int x = 0; x < possibility[0].length; x++) {
                    list[y][x] = getWaterListForPoint(x, y, possibility);
                }
            }
            this.waterSurround.add(list);
        }
    }

    private ArrayList<Integer> getWaterListForPoint(int x, int y, byte[][] possibility) {
        ArrayList<Integer> list = new ArrayList<>();

        if (possibility[y][x] != 0) {
            boolean left = isFilled(x, y, possibility, -1, 0);
            boolean right = isFilled(x, y, possibility, +1, 0);
            boolean up = isFilled(x, y, possibility, 0, -1);
            boolean down = isFilled(x, y, possibility, 0, 1);

            if (!left && !right && !up) {
                list.add(6);
            } else if (!left && !right && !down) {
                list.add(4);
            } else if (!left && !up && !down) {
                list.add(5);
            } else if (!right && !up && !down) {
                list.add(7);
            } else if (!right && !up) {
                list.add(10);
            } else if (!right && !down) {
                list.add(11);
            } else if (!left && !up) {
                list.add(9);
            } else if (!left && !down) {
                list.add(8);
            } else if (!up && !down) {
                list.add(3);
                list.add(1);
            } else if (!left && !right) {
                list.add(2);
                list.add(0);
            } else if (!left) {
                list.add(0);
            } else if (!up) {
                list.add(1);
            } else if (!right) {
                list.add(2);
            } else if (!down) {
                list.add(3);
            }

            if (left && up && !isFilled(x, y, possibility, -1, -1)) {
                list.add(12);
            }
            if (right && up && !isFilled(x, y, possibility, 1, -1)) {
                list.add(13);
            }
            if (right && down && !isFilled(x, y, possibility, 1, 1)) {
                list.add(14);
            }
            if (left && down && !isFilled(x, y, possibility, -1, 1)) {
                list.add(15);
            }
        }

        return list;
    }

    private boolean isFilled(int x, int y, byte[][] possibility, int addX, int addY) {
        if (x + addX < 0 || x + addX >= possibility[0].length ||
                y + addY < 0 || y + addY >= possibility.length) {
            return false;
        }
        return possibility[y+addY][x+addX] >= 1;
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

    public ArrayList<ArrayList<Integer>[][]> getWaterSurround() {
        return waterSurround;
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
