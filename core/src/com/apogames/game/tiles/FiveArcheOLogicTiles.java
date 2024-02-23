package com.apogames.game.tiles;

public class FiveArcheOLogicTiles extends GivenTiles {

    private final byte[][] tileOneByte = new byte[][] {
            {2, 1},
            {1, 0},
            {1, 1},
    };

    private final byte[][] tileTwoByte = new byte[][] {
            {3},
            {1},
            {2},
            {1}
    };

    private final byte[][] tileThreeByte = new byte[][] {
            {1, 0},
            {1, 3},
            {2, 0},
    };

    private final byte[][] tileFourByte = new byte[][] {
            {2, 0},
            {1, 1},
            {0, 1},
    };

    private final byte[][] tileFiveByte = new byte[][] {
            {1, 3},
            {1, 0},
            {1, 0},
            {2, 0}
    };

    private final byte[][] tileSevenByte = new byte[][] {
            {1},
    };

    private final Tile tileOne = new Tile(1, tileOneByte);
    private final Tile tileTwo = new Tile(2, tileTwoByte);
    private final Tile tileThree = new Tile(3, tileThreeByte);
    private final Tile tileFour = new Tile(4, tileFourByte);
    private final Tile tileFive = new Tile(5, tileFiveByte);
    private final Tile tileSeven = new Tile(6, tileSevenByte);
    private final Tile tileEight = new Tile(7, tileSevenByte);
    private final Tile tileNine = new Tile(8, tileSevenByte);

    public FiveArcheOLogicTiles() {
        super.getAllTiles().add(tileOne);
        super.getAllTiles().add(tileTwo);
        super.getAllTiles().add(tileThree);
        super.getAllTiles().add(tileFour);
        super.getAllTiles().add(tileFive);
        super.getAllTiles().add(tileSeven);
        super.getAllTiles().add(tileEight);
        super.getAllTiles().add(tileNine);
    }

    @Override
    public String getName() {
        return "FiveArcheOLogic";
    }

    @Override
    public int getMaxTile() {
        return 5;
    }

    @Override
    public int[] getDifficultyTiles() {
        return new int[] {3, 2, 2, 1};
    }
}
