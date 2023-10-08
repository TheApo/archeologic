package com.apogames.game.archeologic;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.DrawString;
import com.apogames.backend.SequentiallyThinkingScreenModel;
import com.apogames.common.Localization;
import com.apogames.entity.ApoButton;
import com.apogames.game.MainPanel;
import com.apogames.game.knuthAlgoX.AlgorithmX;
import com.apogames.game.knuthAlgoX.MyPuzzleADayBinary;
import com.apogames.game.menu.Difficulty;
import com.apogames.game.tiles.ArcheOLogicTiles;
import com.apogames.game.tiles.GivenTiles;
import com.apogames.game.tiles.Tile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
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

    public static final String FUNCTION_FINISH_BACK = "TIWANAKU_FINISH_QUIT";
    public static final String FUNCTION_FINISH_NEW_LEVEL = "TIWANAKU_FINISH_NEW_LEVEL";
    public static final String FUNCTION_FINISH_RESTART = "TIWANAKU_FINISH_RESTART";

    private final boolean[] keys = new boolean[256];

    private boolean isPressed = false;

    private GameEntity game;

    public ArcheOLogicPanel(final MainPanel game) {
        super(game);
    }

    public void setNeededButtonsVisible() {
        getMainPanel().getButtonByFunction(FUNCTION_TIWANAKU_BACK).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_RESTART).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_NEW_LEVEL).setVisible(true);
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

        if (this.game == null) {
            this.game = new GameEntity();

            int xSize = 5;
            int ySize = 5;

            ArcheOLogicTiles logic = new ArcheOLogicTiles();
            MyPuzzleADayBinary myPuzzleADayBinary = new MyPuzzleADayBinary(logic.getAllTiles());
            byte[][] matrix = myPuzzleADayBinary.getMatrix(MyPuzzleADayBinary.GOAL);

            AlgorithmX algoX = new AlgorithmX();
            algoX.run(xSize, ySize, logic.getAllTiles().size(), matrix);

            this.game.setAllSolutions(algoX.allSolutions, algoX.allValueSolutions);
        }

        this.getMainPanel().resetSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        this.setNeededButtonsVisible();
    }

    public void setNewLevel(Difficulty difficulty) {
        this.game.setNewLevel(difficulty);
    }

    public GameEntity getGame() {
        return game;
    }

    @Override
    public void keyPressed(int keyCode, char character) {
        super.keyPressed(keyCode, character);

        keys[keyCode] = true;
    }

    private void createNewLevel() {
        this.getMainPanel().getButtonByFunction(FUNCTION_NEW_LEVEL).setVisible(true);

        this.game.choseNewSolution();
        this.game.resetTiles();

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
        this.game.mouseMoved(mouseX, mouseY);

        if (this.isPressed) {
            this.mouseButtonReleased(mouseX, mouseY, false);
        }
    }

    public void mouseButtonReleased(int mouseX, int mouseY, boolean isRightButton) {
        this.isPressed = false;

        this.game.mouseButtonReleased(mouseX, mouseY, isRightButton);
        if (this.game.isWon()) {
            this.createNewLevel();
        }
    }

    public void mousePressed(int x, int y, boolean isRightButton) {
        if (!this.isPressed) {
            this.isPressed = true;

            this.game.mousePressed(x, y, isRightButton);
        }
    }

    public void mouseDragged(int x, int y, boolean isRightButton) {
        if (!this.isPressed) {
            this.mousePressed(x, y, isRightButton);
        }

        this.game.mouseDragged(x, y, isRightButton);
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
                this.game.resetTiles();
                break;
        }
    }

    private void setButtonsVisibility() {
        getMainPanel().getButtonByFunction(FUNCTION_TIWANAKU_BACK).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_RESTART).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_NEW_LEVEL).setVisible(false);
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

        getMainPanel().spriteBatch.begin();

        getMainPanel().spriteBatch.draw(AssetLoader.backgroundMainTextureRegion, 0, 0);
        getMainPanel().spriteBatch.draw(AssetLoader.boardTextureRegion, Constants.TILE_SIZE, Constants.TILE_SIZE * 2, 7 * Constants.TILE_SIZE, 7 * Constants.TILE_SIZE);

        getMainPanel().spriteBatch.end();

//        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
//        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);
//        for (GameTile tile : this.game.getCurrentTiles()) {
//            tile.renderShadow(getMainPanel());
//        }
//
//        getMainPanel().getRenderer().end();
//        Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);

        getMainPanel().spriteBatch.begin();

        for (GameTile tile : this.game.getCurrentTiles()) {
            tile.render(getMainPanel());
        }

        float hudStartX = Constants.GAME_WIDTH - 5 - AssetLoader.hudRightTextureRegion.getRegionWidth();
        getMainPanel().spriteBatch.draw(AssetLoader.hudRightTextureRegion, hudStartX, 5);


        getMainPanel().spriteBatch.draw(AssetLoader.titleTextureRegion, (Constants.GAME_WIDTH - AssetLoader.hudRightTextureRegion.getRegionWidth() - AssetLoader.titleTextureRegion.getRegionWidth())/2f, 5);
        getMainPanel().drawString(Constants.PROPERTY_NAME, (Constants.GAME_WIDTH - AssetLoader.hudRightTextureRegion.getRegionWidth())/2f, 44, Constants.COLOR_WHITE, AssetLoader.font30, DrawString.MIDDLE, true, false);

        getMainPanel().spriteBatch.end();

        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);

        for (GameTile tile : this.game.getCurrentTiles()) {
            tile.renderLine(getMainPanel());
        }

        getMainPanel().getRenderer().end();

        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        getMainPanel().spriteBatch.begin();
        getMainPanel().spriteBatch.setColor(1, 1, 1, 0.4f);
        for (GameTile tile : this.game.getHiddenTiles()) {
            tile.render(getMainPanel(), false);
        }

        getMainPanel().spriteBatch.setColor(1, 1, 1, 1f);
        getMainPanel().spriteBatch.end();
        Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);

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
