package com.apogames.game.archeologic;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.DrawString;
import com.apogames.backend.SequentiallyThinkingScreenModel;
import com.apogames.entity.ApoButton;
import com.apogames.game.MainPanel;
import com.apogames.game.menu.Difficulty;
import com.apogames.game.question.*;
import com.apogames.help.Helper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ArcheOLogicPanel extends SequentiallyThinkingScreenModel {

    private final int addX = 10;
    private final int addY = 100;

    private int addXScale = 0;

    public static final String FUNCTION_TIWANAKU_BACK = "ARCHEOLOGIC_QUIT";
    public static final String FUNCTION_NEW_LEVEL = "ARCHEOLOGIC_NEW_LEVEL";
    public static final String FUNCTION_RESTART = "ARCHEOLOGIC_RESTART";

    public static final String FUNCTION_FINISH_BACK = "ARCHEOLOGIC_FINISH_QUIT";
    public static final String FUNCTION_FINISH_NEW_LEVEL = "ARCHEOLOGIC_FINISH_NEW_LEVEL";
    public static final String FUNCTION_FINISH_RESTART = "ARCHEOLOGIC_FINISH_RESTART";
    public static final String FUNCTION_QUESTION_TEST = "ARCHEOLOGIC_QUESTION_TEST";

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
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_TEST).setVisible(true);
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
            case ArcheOLogicPanel.FUNCTION_QUESTION_TEST:
                Helper.print(this.game.getSolution());
                for (int i = 0; i < 5; i++) {
                    Question question = new Empty(i, -1, this.game.getRealSolution());
                    System.out.println(question.getText());

                    question = new AmountTiles(i, -1, this.game.getSolution());
                    System.out.println(question.getText());

                    question = new SandAndGrassCheck(i, -1, this.game.getRealSolution());
                    System.out.println(question.getText());

                    for (GameTile tile : this.game.getCurrentTiles()) {
                        question = new OneTileCheck(i, -1, this.game.getSolution(), this.game.getRealSolution(), tile.getTile().getTileNumber(), tile.getTile().getPossibilities().get(0));
                        System.out.println(question.getText());
                    }
                }
                for (int i = 0; i < 5; i++) {
                    Question question = new Empty(-1, i, this.game.getRealSolution());
                    System.out.println(question.getText());

                    question = new AmountTiles(-1, i, this.game.getSolution());
                    System.out.println(question.getText());

                    question = new SandAndGrassCheck(-1, i, this.game.getRealSolution());
                    System.out.println(question.getText());
                }
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
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_TEST).setVisible(true);
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

        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);

        for (GameTile tile : this.game.getCurrentTiles()) {
            tile.renderShadow(getMainPanel());
        }

        getMainPanel().getRenderer().end();

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
        getMainPanel().spriteBatch.setColor(1, 1, 1, 0.7f);
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

    public void drawOverlay() {
    }

    @Override
    public void dispose() {
    }
}
