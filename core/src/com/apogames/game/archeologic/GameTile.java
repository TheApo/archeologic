package com.apogames.game.archeologic;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.GameScreen;
import com.apogames.game.tiles.Tile;

public class GameTile {

    private final Tile tile;

    private int currentTile = 0;

    private float x = 0;
    private float y = 0;

    private int nextX = 0;
    private int nextY = 0;

    GameTile(Tile tile) {
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;

        int myX = (int)(this.x / Constants.TILE_SIZE);
        this.nextX = myX * Constants.TILE_SIZE;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;

        int myY = (int)(this.y / Constants.TILE_SIZE);
        this.nextY = myY * Constants.TILE_SIZE;
    }

    public int getNextX() {
        return nextX;
    }

    public void setNextX(int nextX) {
        this.nextX = nextX;
    }

    public int getNextY() {
        return nextY;
    }

    public void setNextY(int nextY) {
        this.nextY = nextY;
    }

    public int getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(int currentTile) {
        this.currentTile = currentTile;
    }

    public void render(GameScreen screen) {
        byte[][] byteArray = tile.getPossibilities().get(this.currentTile);
        int yTile = (int)(Constants.TILE_SIZE * AssetLoader.backgroundTextureRegion[0].getRegionHeight() / (float)(AssetLoader.backgroundTextureRegion[0].getRegionWidth()));
        int xTile = Constants.TILE_SIZE;
        int yChange = yTile - xTile;
        for (int y = 0; y < byteArray.length; y++) {
            for (int x = 0; x < byteArray[0].length; x++) {
                if (byteArray[y][x] == 1) {
                    screen.spriteBatch.draw(AssetLoader.backgroundTextureRegion[1], this.nextX + x * Constants.TILE_SIZE, this.nextY + y * Constants.TILE_SIZE - yChange, Constants.TILE_SIZE, yTile);
                }
                if (byteArray[y][x] == 2) {
                    screen.spriteBatch.draw(AssetLoader.backgroundTextureRegion[2], this.nextX + x * Constants.TILE_SIZE, this.nextY + y * Constants.TILE_SIZE - yChange, Constants.TILE_SIZE, yTile);
                }
                if (byteArray[y][x] == 3) {
                    screen.spriteBatch.draw(AssetLoader.backgroundTextureRegion[0], this.nextX + x * Constants.TILE_SIZE, this.nextY + y * Constants.TILE_SIZE - yChange, Constants.TILE_SIZE, yTile);
                }
            }
        }
    }

    public void renderLine(GameScreen screen) {
        byte[][] byteArray = tile.getPossibilities().get(this.currentTile);
        for (int y = 0; y < byteArray.length; y++) {
            for (int x = 0; x < byteArray[0].length; x++) {
                if (byteArray[y][x] != 0) {
                    if (x - 1 < 0 || byteArray[y][x-1] == 0) {
                        screen.getRenderer().rectLine(this.nextX + x * Constants.TILE_SIZE, this.nextY + y * Constants.TILE_SIZE, this.nextX + x * Constants.TILE_SIZE, this.nextY + y * Constants.TILE_SIZE + Constants.TILE_SIZE, 3);
                    }
                    if (x + 1 >= byteArray[0].length || byteArray[y][x+1] == 0) {
                        screen.getRenderer().rectLine(this.nextX + x * Constants.TILE_SIZE + Constants.TILE_SIZE, this.nextY + y * Constants.TILE_SIZE, this.nextX + x * Constants.TILE_SIZE + Constants.TILE_SIZE, this.nextY + y * Constants.TILE_SIZE + Constants.TILE_SIZE, 3);
                    }
                    if ((y - 1 < 0 || byteArray[y - 1][x] == 0) && (byteArray[y][x] != 2)) {
                        screen.getRenderer().rectLine(this.nextX + x * Constants.TILE_SIZE, this.nextY + y * Constants.TILE_SIZE, this.nextX + x * Constants.TILE_SIZE + Constants.TILE_SIZE, this.nextY + y * Constants.TILE_SIZE, 3);
                    }
                    if (y + 1 >= byteArray.length || byteArray[y + 1][x] == 0) {
                        screen.getRenderer().rectLine(this.nextX + x * Constants.TILE_SIZE, this.nextY + y * Constants.TILE_SIZE + Constants.TILE_SIZE, this.nextX + x * Constants.TILE_SIZE + Constants.TILE_SIZE, this.nextY + y * Constants.TILE_SIZE + Constants.TILE_SIZE, 3);
                    }
                }
            }
        }
    }
}
