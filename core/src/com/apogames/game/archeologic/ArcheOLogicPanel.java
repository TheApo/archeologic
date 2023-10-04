package com.apogames.game.archeologic;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.DrawString;
import com.apogames.backend.SequentiallyThinkingScreenModel;
import com.apogames.common.Localization;
import com.apogames.entity.ApoButton;
import com.apogames.game.MainPanel;
import com.apogames.game.knuthAlgoX.AlgorithmX;
import com.apogames.game.tiles.ArcheOLogicTiles;
import com.apogames.game.tiles.GivenTiles;
import com.apogames.game.tiles.Tile;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class ArcheOLogicPanel extends SequentiallyThinkingScreenModel {

    private final int addX = 10;
    private final int addY = 100;

    private int addXScale = 0;

    public static final String FUNCTION_TIWANAKU_BACK = "TIWANAKU_QUIT";
    public static final String FUNCTION_NEW_LEVEL = "TIWANAKU_NEW_LEVEL";
    public static final String FUNCTION_RESTART = "TIWANAKU_RESTART";
    public static final String FUNCTION_FIX = "TIWANAKU_FIX";
    public static final String FUNCTION_HELP = "TIWANAKU_HELP";

    public static final String FUNCTION_FINISH_BACK = "TIWANAKU_FINISH_QUIT";
    public static final String FUNCTION_FINISH_NEW_LEVEL = "TIWANAKU_FINISH_NEW_LEVEL";
    public static final String FUNCTION_FINISH_RESTART = "TIWANAKU_FINISH_RESTART";

    private final boolean[] keys = new boolean[256];

    private boolean isPressed = false;

    private ArrayList<GameTile> currentTiles;

    public ArcheOLogicPanel(final MainPanel game) {
        super(game);
    }

    public void setNeededButtonsVisible() {
        getMainPanel().getButtonByFunction(FUNCTION_TIWANAKU_BACK).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_RESTART).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_FIX).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_NEW_LEVEL).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_HELP).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_BACK).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_RESTART).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_NEW_LEVEL).setVisible(false);
    }

    public void setValues() {
    }

    @Override
    public void init() {
        if (getGameProperties() == null) {
            setGameProperties(new ArcheOLogicPreferences(this));
            loadProperties();
        }

        if (currentTiles == null) {
            ArcheOLogicTiles currentTiles = new ArcheOLogicTiles();
            this.currentTiles = new ArrayList<>();
            for (Tile tile : currentTiles.getAllTiles()) {
                if (tile.getPossibilities().get(0).length != 1 || tile.getPossibilities().get(0)[0].length != 1) {
                    this.currentTiles.add(new GameTile(tile));
                }
            }

            int startX = 9 * Constants.TILE_SIZE;
            int startY = 3 * Constants.TILE_SIZE;

            for (GameTile tile : this.currentTiles) {
                tile.setX(startX);
                tile.setY(startY);

                startX += (tile.getTile().getPossibilities().get(tile.getCurrentTile())[0].length) * Constants.TILE_SIZE;
                if (startX >= 15 * Constants.TILE_SIZE) {
                    startX = 9 * Constants.TILE_SIZE;
                    startY += 3 * Constants.TILE_SIZE;
                }
            }
        }

        this.getMainPanel().resetSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        this.setNeededButtonsVisible();
    }

    @Override
    public void keyPressed(int keyCode, char character) {
        super.keyPressed(keyCode, character);

        keys[keyCode] = true;
    }

    private void createNewLevel() {
        this.getMainPanel().getButtonByFunction(FUNCTION_NEW_LEVEL).setVisible(true);

        setNeededButtonsVisible();
    }

    @Override
    public void keyButtonReleased(int keyCode, char character) {
        super.keyButtonReleased(keyCode, character);

        if (keyCode == Input.Keys.N) {
            this.createNewLevel();
        }

        keys[keyCode] = false;
    }

    public void mouseMoved(int mouseX, int mouseY) {
    }

    public void mouseButtonReleased(int mouseX, int mouseY, boolean isRightButton) {
        this.isPressed = false;

    }

    public void mousePressed(int x, int y, boolean isRightButton) {
        if (isRightButton && !this.isPressed) {
            this.isPressed = true;
        }
    }

    public void mouseDragged(int x, int y, boolean isRightButton) {
        if (isRightButton) {
            if (!this.isPressed) {
                this.mousePressed(x, y, isRightButton);
            }
        }
    }

    @Override
    public void mouseButtonFunction(String function) {
        super.mouseButtonFunction(function);
        switch (function) {
            case ArcheOLogicPanel.FUNCTION_TIWANAKU_BACK:
            case ArcheOLogicPanel.FUNCTION_FINISH_BACK:
                quit();
                break;
            case ArcheOLogicPanel.FUNCTION_NEW_LEVEL:
            case ArcheOLogicPanel.FUNCTION_FINISH_NEW_LEVEL:
                createNewLevel();
                break;
            case ArcheOLogicPanel.FUNCTION_RESTART:
            case ArcheOLogicPanel.FUNCTION_FINISH_RESTART:
                break;
            case ArcheOLogicPanel.FUNCTION_FIX:
                break;
        }
    }

    private void setButtonsVisibility() {
        getMainPanel().getButtonByFunction(FUNCTION_TIWANAKU_BACK).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_RESTART).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_FIX).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_NEW_LEVEL).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_HELP).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_BACK).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_RESTART).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_NEW_LEVEL).setVisible(true);
    }

    public void mouseWheelChanged(int changed) {
    }

    @Override
    protected void quit() {
        getMainPanel().changeToMenu();
    }

    @Override
    public void doThink(float delta) {

    }

    @Override
    public void render() {
        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);

        getMainPanel().getRenderer().setColor(Constants.COLOR_BLACK[0], Constants.COLOR_BLACK[1], Constants.COLOR_BLACK[2], 1f);

        for (GameTile tile : this.currentTiles) {
            tile.renderLine(getMainPanel());
        }

        getMainPanel().getRenderer().end();

        getMainPanel().spriteBatch.begin();

        getMainPanel().spriteBatch.draw(AssetLoader.boardTextureRegion, Constants.TILE_SIZE, Constants.TILE_SIZE * 2, 7 * Constants.TILE_SIZE, 7 * Constants.TILE_SIZE);

        for (GameTile tile : this.currentTiles) {
            tile.render(getMainPanel());
        }

        float hudStartX = Constants.GAME_WIDTH - 5 - AssetLoader.hudRightTextureRegion.getRegionWidth();
        getMainPanel().spriteBatch.draw(AssetLoader.hudRightTextureRegion, hudStartX, 5);


        getMainPanel().spriteBatch.draw(AssetLoader.titleTextureRegion, (Constants.GAME_WIDTH - AssetLoader.hudRightTextureRegion.getRegionWidth() - AssetLoader.titleTextureRegion.getRegionWidth())/2f, 5);
        getMainPanel().drawString(Constants.PROPERTY_NAME, (Constants.GAME_WIDTH - AssetLoader.hudRightTextureRegion.getRegionWidth())/2f, 44, Constants.COLOR_WHITE, AssetLoader.font30, DrawString.MIDDLE, true, false);

        int tileSizeWidth = AssetLoader.backgroundTextureRegion[4].getRegionWidth();
        int tileSizeHeight = AssetLoader.backgroundTextureRegion[4].getRegionHeight();
        float scale = 1.0f;

        getMainPanel().spriteBatch.end();

        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);

        getMainPanel().getRenderer().setColor(Constants.COLOR_BLACK[0], Constants.COLOR_BLACK[1], Constants.COLOR_BLACK[2], 1f);

        for (GameTile tile : this.currentTiles) {
            tile.renderLine(getMainPanel());
        }

        getMainPanel().getRenderer().end();

        for (ApoButton button : this.getMainPanel().getButtons()) {
            button.render(this.getMainPanel());
        }
    }

//	        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
//			Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);
//
//			getMainPanel().getRenderer().begin(ShapeType.Line);
//			getMainPanel().getRenderer().setColor(Constants.COLOR_WHITE[0], Constants.COLOR_WHITE[1], Constants.COLOR_WHITE[2], 1f);
//			getMainPanel().getRenderer().roundedRectLine((WIDTH - width)/2f, startY, width, height, 5);
//			getMainPanel().getRenderer().end();


    public void drawOverlay() {
    }

    @Override
    public void dispose() {
    }
}
