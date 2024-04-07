package com.apogames.game.knuthAlgoX;

public class PlacedTileHelper {

    private final int tileNumber;
    private final int possibility;
    private int placedX;
    private int placedY;

    public PlacedTileHelper(int tileNumber, int index) {
        this.tileNumber = tileNumber;
        this.possibility = index;

        this.placedX = 0;
        this.placedY = 0;
    }

    public PlacedTileHelper(int tileNumber, int index, int placedX, int placedY) {
        this.tileNumber = tileNumber;
        this.possibility = index;
        this.placedX = placedX;
        this.placedY = placedY;
    }

    public int getTileNumber() {
        return tileNumber;
    }

    public int getPossibility() {
        return possibility;
    }

    public int getPlacedX() {
        return placedX;
    }

    public int getPlacedY() {
        return placedY;
    }

    public void setPlacedX(int placedX) {
        this.placedX = placedX;
    }

    public void setPlacedY(int placedY) {
        this.placedY = placedY;
    }
}
