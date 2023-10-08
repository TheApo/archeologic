package com.apogames.game.archeologic;

public class PrefillHelp {

    private final int x;
    private final int y;
    private final int tileNumber;

    public PrefillHelp(int x, int y, int tileNumber) {
        this.x = x;
        this.y = y;
        this.tileNumber = tileNumber;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTileNumber() {
        return tileNumber;
    }
}
