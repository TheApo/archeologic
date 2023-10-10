package com.apogames.game.tiles;

import java.util.ArrayList;
import java.util.Collections;

public abstract class GivenTiles {

    private final ArrayList<Tile> allTiles = new ArrayList<>();

    public ArrayList<Tile> getAllTiles() {
        return allTiles;
    }

    public abstract String getName();

    public abstract int getMaxTile();

    public abstract int[] getDifficultyTiles();

    public void shuffleTiles() {
        Collections.shuffle(allTiles);
        for (Tile tile : allTiles) {
            Collections.shuffle(tile.getPossibilities());
        }
    }
}
