package com.apogames.game.archeologic;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.GameScreen;
import com.apogames.game.tiles.Tile;

public class GameTile {

    private final Tile tile;

    private byte[][] bytes;

    private int currentTile = 0;

    private boolean over = false;

    private int gameX = 0;
    private int gameY = 0;
    private float x = 0;
    private float y = 0;

    private int nextX = 0;
    private int nextY = 0;

    private int difX = 0;
    private int difY = 0;
    private float startX = 0;
    private float startY = 0;

    private int genDif = 0;

    private boolean fixed = false;

    GameTile(Tile tile) {
        this.tile = tile;

        this.bytes = this.tile.getPossibilities().get(this.currentTile);
    }

    public Tile getTile() {
        return tile;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public void changePosition(float x, float y) {
        this.x = x;

        int myX = (int)((this.x + Constants.TILE_SIZE/2) / Constants.TILE_SIZE);
        this.nextX = myX * Constants.TILE_SIZE;

        this.y = y;

        int myY = (int)((this.y + Constants.TILE_SIZE/2) / Constants.TILE_SIZE);
        this.nextY = myY * Constants.TILE_SIZE;

        this.gameX = (this.nextX - 2 * Constants.TILE_SIZE) / Constants.TILE_SIZE;
        this.gameY = (this.nextY - 3 * Constants.TILE_SIZE) / Constants.TILE_SIZE;
    }

    public void setTileGamePosition(int gameX, int gameY) {
        this.changePosition((gameX + 2) * Constants.TILE_SIZE, (gameY + 3) * Constants.TILE_SIZE);
    }

    public int getGameX() {
        return gameX;
    }

    public boolean isNextXAndYSameAsGameXAndY() {
        int curGameX = (this.nextX - 2 * Constants.TILE_SIZE) / Constants.TILE_SIZE;
        int curGameY = (this.nextY - 3 * Constants.TILE_SIZE) / Constants.TILE_SIZE;
        return curGameX == gameX && curGameY == gameY;
    }

    public byte[][] getBytes() {
        return bytes;
    }

    public int getGameY() {
        return gameY;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getNextX() {
        return nextX;
    }

    public int getNextY() {
        return nextY;
    }

    public int getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(int currentTile) {
        this.currentTile = currentTile;
        if (this.currentTile >= this.tile.getPossibilities().size()) {
            this.currentTile = 0;
        } else if (this.currentTile < 0) {
            this.currentTile = this.tile.getPossibilities().size() - 1;
        }

        this.bytes = this.tile.getPossibilities().get(this.currentTile);
        this.changePosition(this.x, this.y);
    }

    public int getGenDif() {
        return genDif;
    }

    public boolean isIn(int x, int y) {
        over = false;
        if (x >= this.nextX && (x < this.nextX + bytes[0].length * Constants.TILE_SIZE) &&
           (y >= this.nextY && (y < this.nextY + bytes.length * Constants.TILE_SIZE))) {
            int mouseX = (x - nextX)/Constants.TILE_SIZE;
            int mouseY = (y - nextY)/Constants.TILE_SIZE;
            if (bytes[mouseY][mouseX] != 0) {
                over = true;
                return true;
            }
        }
        return false;
    }

    public boolean click(int x, int y) {
        int dif = this.genDif;//(int)(Math.abs(this.startX - this.x) + Math.abs(this.startY - this.y));
        if (this.over && dif < 30) {
            int mouseX = (x - nextX)/Constants.TILE_SIZE;
            int mouseY = (y - nextY)/Constants.TILE_SIZE;

            int newX = mouseY;
            int newY = bytes[0].length - 1 - mouseX;

            this.changePosition((mouseX - newX) * Constants.TILE_SIZE + this.x, (mouseY - newY) * Constants.TILE_SIZE + this.y);

            this.setCurrentTile(this.currentTile + 1);

            this.x = this.nextX;
            this.y = this.nextY;

            this.difX = 0;
            this.difY = 0;
            this.genDif = 0;

            this.over = false;
            return true;
        }
        boolean oldOver = this.over;
        this.difX = 0;
        this.difY = 0;
        this.genDif = 0;

        this.over = false;
        return oldOver;
    }

    public void setXAndYToNextXAndNextY() {
        this.x = this.nextX;
        this.y = this.nextY;
    }

    public boolean pressMouse(int x, int y) {
        if (this.over) {
            this.difX = (int) (x - this.x);
            this.difY = (int) (y - this.y);

            this.startX = this.x;
            this.startY = this.y;
        }
        return this.over;
    }

    public boolean dragTile(int x, int y) {
        if (this.over && (this.difX != 0 || this.difY != 0)) {
            this.genDif += (int) (Math.abs(x - this.x - difX) + Math.abs(y - this.y - difY));
            this.changePosition(x - this.difX, y - this.difY);
        }
        return this.over;
    }

    public void render(GameScreen screen) {
        float curX = this.nextX;
        float curY = this.nextY;
        if (this.difX != 0 || this.difY != 0) {
            curX = this.x;
            curY = this.y;
        }
        render(screen,curX, curY, true);
    }

    public void renderX(GameScreen screen) {
        int size = 20;
        screen.spriteBatch.draw(AssetLoader.xTextureRegion, nextX + Constants.TILE_SIZE/2f - size/2f, nextY + Constants.TILE_SIZE/2f - size/2f, size, size);
    }

    public void render(GameScreen screen, boolean withWater) {
        render(screen,this.nextX, this.nextY, withWater);
    }

    public void render(GameScreen screen, float curX, float curY, boolean withWater) {
        byte[][] byteArray = tile.getPossibilities().get(this.currentTile);
        int yTile = (int) (Constants.TILE_SIZE * AssetLoader.backgroundTextureRegion[0].getRegionHeight() / (float) (AssetLoader.backgroundTextureRegion[0].getRegionWidth()));
        int xTile = Constants.TILE_SIZE;
        int yChange = yTile - xTile;

        for (int y = 0; y < byteArray.length; y++) {
            for (int x = 0; x < byteArray[0].length; x++) {
                if (byteArray[y][x] == 1) {
                    screen.spriteBatch.draw(AssetLoader.backgroundTextureRegion[1], curX + x * Constants.TILE_SIZE, curY + y * Constants.TILE_SIZE - yChange, Constants.TILE_SIZE, yTile);
                }
                if (byteArray[y][x] == 2) {
                    screen.spriteBatch.draw(AssetLoader.backgroundTextureRegion[2], curX + x * Constants.TILE_SIZE, curY + y * Constants.TILE_SIZE - yChange, Constants.TILE_SIZE, yTile);
                }
                if (byteArray[y][x] == 3) {
                    screen.spriteBatch.draw(AssetLoader.backgroundTextureRegion[0], curX + x * Constants.TILE_SIZE, curY + y * Constants.TILE_SIZE - yChange, Constants.TILE_SIZE, yTile);
                }
            }
        }
        if (withWater) {
            for (int y = 0; y < byteArray.length; y++) {
                for (int x = 0; x < byteArray[0].length; x++) {
                    if (byteArray[y][x] >= 1) {
                        for (int z = 0; z < tile.getWaterSurround().get(this.currentTile)[y][x].size(); z++) {
                            screen.spriteBatch.draw(AssetLoader.waterTextureRegion[tile.getWaterSurround().get(this.currentTile)[y][x].get(z)], curX + x * Constants.TILE_SIZE, curY + y * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
                        }
                    }
                }
            }
        }
    }

    public void renderLine(GameScreen screen) {
        screen.getRenderer().setColor(Constants.COLOR_BLACK[0], Constants.COLOR_BLACK[1], Constants.COLOR_BLACK[2], 1f);
        if (over) {
            screen.getRenderer().setColor(Constants.COLOR_YELLOW[0], Constants.COLOR_YELLOW[1], Constants.COLOR_YELLOW[2], 1f);
        } else if (isFixed()) {
            screen.getRenderer().setColor(Constants.COLOR_GREY[0], Constants.COLOR_GREY[1], Constants.COLOR_GREY[2], 1f);
        } else {
            return;
        }

        float curX = this.nextX;
        float curY = this.nextY;
        if (this.difX != 0 || this.difY != 0) {
            curX = this.x;
            curY = this.y;
        }

        byte[][] byteArray = tile.getPossibilities().get(this.currentTile);
        for (int y = 0; y < byteArray.length; y++) {
            for (int x = 0; x < byteArray[0].length; x++) {
                if (byteArray[y][x] != 0) {
                    if (x - 1 < 0 || byteArray[y][x-1] == 0) {
                        screen.getRenderer().rectLine(curX + 1 + x * Constants.TILE_SIZE, curY + y * Constants.TILE_SIZE, curX + 1 + x * Constants.TILE_SIZE, curY + y * Constants.TILE_SIZE + Constants.TILE_SIZE, 3);
                    }
                    if (x + 1 >= byteArray[0].length || byteArray[y][x+1] == 0) {
                        screen.getRenderer().rectLine(curX - 1 + x * Constants.TILE_SIZE + Constants.TILE_SIZE, curY + y * Constants.TILE_SIZE, curX - 1 + x * Constants.TILE_SIZE + Constants.TILE_SIZE, curY + y * Constants.TILE_SIZE + Constants.TILE_SIZE, 3);
                    }
                    if (y - 1 < 0 || byteArray[y - 1][x] == 0) {
                        screen.getRenderer().rectLine(curX + x * Constants.TILE_SIZE, curY + 1 + y * Constants.TILE_SIZE, curX + x * Constants.TILE_SIZE + Constants.TILE_SIZE, curY + 1 + y * Constants.TILE_SIZE, 3);
                    }
                    if (y + 1 >= byteArray.length || byteArray[y + 1][x] == 0) {
                        screen.getRenderer().rectLine(curX - 1 + x * Constants.TILE_SIZE, curY + y * Constants.TILE_SIZE + Constants.TILE_SIZE, curX - 1 + x * Constants.TILE_SIZE + Constants.TILE_SIZE, curY + y * Constants.TILE_SIZE + Constants.TILE_SIZE, 3);
                    }
                }
            }
        }
    }

    public void renderShadow(GameScreen screen) {
        if (this.difX == 0 && this.difY == 0) {
            return;
        }

        screen.getRenderer().setColor(Constants.COLOR_GREY[0], Constants.COLOR_GREY[1], Constants.COLOR_GREY[2], 0.6f);

        float curX = this.nextX;
        float curY = this.nextY;

        byte[][] byteArray = tile.getPossibilities().get(this.currentTile);
        for (int y = 0; y < byteArray.length; y++) {
            for (int x = 0; x < byteArray[0].length; x++) {
                if (byteArray[y][x] != 0) {
                    screen.getRenderer().rect(curX + x * Constants.TILE_SIZE, curY + y * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
                }
            }
        }
    }
}
