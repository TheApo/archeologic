package com.apogames.game.archeologic;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.DrawString;
import com.apogames.backend.SequentiallyThinkingScreenModel;
import com.apogames.common.Localization;
import com.apogames.entity.ApoButton;
import com.apogames.entity.ApoButtonImageDropdown;
import com.apogames.game.MainPanel;
import com.apogames.game.menu.Difficulty;
import com.apogames.game.question.*;
import com.apogames.help.Helper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ArcheOLogicPanel extends SequentiallyThinkingScreenModel {

    public static final String FUNCTION_ARCHEOLOGIC_BACK = "ARCHEOLOGIC_QUIT";
    public static final String FUNCTION_NEW_LEVEL = "ARCHEOLOGIC_NEW_LEVEL";
    public static final String FUNCTION_RESTART = "ARCHEOLOGIC_RESTART";

    public static final String FUNCTION_FINISH_BACK = "ARCHEOLOGIC_FINISH_QUIT";
    public static final String FUNCTION_FINISH_NEW_LEVEL = "ARCHEOLOGIC_FINISH_NEW_LEVEL";
    public static final String FUNCTION_FINISH_RESTART = "ARCHEOLOGIC_FINISH_RESTART";
    public static final String FUNCTION_QUESTION_TEST = "ARCHEOLOGIC_QUESTION_TEST";
    public static final String FUNCTION_QUESTION_CHECK = "ARCHEOLOGIC_QUESTION_CHECK";
    public static final String FUNCTION_QUESTION_QUESTION = "ARCHEOLOGIC_QUESTION_QUESTION";
    public static final String FUNCTION_QUESTION_ROW = "ARCHEOLOGIC_QUESTION_ROW_";
    public static final String FUNCTION_QUESTION_QUESTION_ASK = "ARCHEOLOGIC_QUESTION_QUESTION_";
    public static final String FUNCTION_QUESTION_QUESTION_REAL = "ARCHEOLOGIC_QUESTION_QUESTION_REAL";
    public static final String FUNCTION_QUESTION_QUESTION_DROPDOWN = "ARCHEOLOGIC_QUESTION_QUESTION_DROPDOWN";
    public static final String FUNCTION_QUESTION_QUESTION_CLOSE = "ARCHEOLOGIC_QUESTION_QUESTION_CLOSE";

    public static final String[] askOrder = new String[] {
            "A", "1", "B", "2", "C", "3", "D", "4", "E", "5"
    };

    private int curAskOrder = 0;
    private int curWantedQuestion = 0;

    private String curAddQuestionString = "";
    private final boolean[] keys = new boolean[256];

    private boolean isPressed = false;

    private GameEntity game;

    private boolean showQuestion = false;

    public ArcheOLogicPanel(final MainPanel game) {
        super(game);
    }

    public void setNeededButtonsVisible() {
        getMainPanel().getButtonByFunction(FUNCTION_ARCHEOLOGIC_BACK).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_RESTART).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_NEW_LEVEL).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_TEST).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_CHECK).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_BACK).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_RESTART).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_NEW_LEVEL).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_REAL).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_CLOSE).setVisible(false);
        for (String s : askOrder) {
            getMainPanel().getButtonByFunction(FUNCTION_QUESTION_ROW + s).setVisible(false);
        }
        for (int i = 0; i < QuestionEnum.values().length; i++) {
            getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.values()[i].name()).setVisible(false);
        }
    }

    @Override
    public void init() {
        if (getGameProperties() == null) {
            setGameProperties(new ArcheOLogicPreferences(this));
            loadProperties();
        }

        if (this.game == null) {
            this.game = new GameEntity();

            ApoButtonImageDropdown buttonByFunction = (ApoButtonImageDropdown)(this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN));
            buttonByFunction.setCurTiles(this.game.getCurrentTiles());

            this.curAddQuestionString = Localization.getInstance().getCommon().get("question_add_column")+" A?";
        }

        this.getMainPanel().resetSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        this.setNeededButtonsVisible();
    }

    public void setNewLevel(Difficulty difficulty) {
        this.game.setNewLevel(difficulty);

        ApoButtonImageDropdown buttonByFunction = (ApoButtonImageDropdown)(this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN));
        buttonByFunction.setCurTiles(this.game.getCurrentTiles());
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

        ApoButtonImageDropdown buttonByFunction = (ApoButtonImageDropdown)(this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN));
        buttonByFunction.setCurTiles(this.game.getCurrentTiles());

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

        if (this.showQuestion) {
            if (mouseX < Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() - 10) {
                this.setShowQuestion(false);
            }
        } else {
            this.game.mouseButtonReleased(mouseX, mouseY, isRightButton);
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

    private void setShowQuestion(boolean showQuestion) {
        this.showQuestion = showQuestion;

        getMainPanel().getButtonByFunction(FUNCTION_ARCHEOLOGIC_BACK).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_RESTART).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_NEW_LEVEL).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_TEST).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_CHECK).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_BACK).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_RESTART).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_NEW_LEVEL).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_REAL).setVisible(showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_CLOSE).setVisible(showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN).setVisible(showQuestion);
        for (String s : askOrder) {
            getMainPanel().getButtonByFunction(FUNCTION_QUESTION_ROW + s).setVisible(showQuestion);
        }
        for (int i = 0; i < QuestionEnum.values().length; i++) {
            getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.values()[i].name()).setVisible(showQuestion);
        }
    }

    @Override
    public void mouseButtonFunction(String function) {
        if (this.showQuestion) {
            if (function.equals(ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_REAL)) {
                this.addQuestion();
                return;
            }
            if (function.equals(ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_CLOSE)) {
                this.setShowQuestion(false);
                return;
            }
            for (String s : askOrder) {
                String buttonFunction = ArcheOLogicPanel.FUNCTION_QUESTION_ROW + s;
                if (function.equals(buttonFunction)) {
                    for (int i = 0; i < askOrder.length; i++) {
                        String deselectAll = askOrder[i];
                        String buttonDeselect = ArcheOLogicPanel.FUNCTION_QUESTION_ROW + deselectAll;
                        getMainPanel().getButtonByFunction(buttonDeselect).setSelect(false);
                        if (buttonDeselect.equals(buttonFunction)) {
                            this.curWantedQuestion = i;
                        }
                    }
                    setCurAddQuestionString(s);
                    getMainPanel().getButtonByFunction(buttonFunction).setSelect(true);
                    return;
                }
            }
            for (int i = 0; i < QuestionEnum.values().length; i++) {
                String buttonFunction = FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.values()[i].name();
                if (function.equals(buttonFunction)) {
                    for (int j = 0; j < QuestionEnum.values().length; j++) {
                        String buttonDeselect = FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.values()[j].name();
                        getMainPanel().getButtonByFunction(buttonDeselect).setSelect(false);
                    }
                    getMainPanel().getButtonByFunction(buttonFunction).setSelect(true);
                    return;
                }
            }
        } else {
            super.mouseButtonFunction(function);
            switch (function) {
                case ArcheOLogicPanel.FUNCTION_ARCHEOLOGIC_BACK:
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
                case ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION:
                    this.setShowQuestion(true);
                    break;
                case ArcheOLogicPanel.FUNCTION_QUESTION_CHECK:
                    if (this.game.isWon()) {
                        this.createNewLevel();
                    } else {
                        this.game.setCosts(this.game.getCosts() + 5);
                    }
                    break;
                case ArcheOLogicPanel.FUNCTION_QUESTION_TEST:
                    Helper.print(this.game.getSolution());
                    for (int i = 0; i < 5; i++) {
                        Question question = new Empty(i, -1, this.game.getRealSolution());
                        this.game.getQuestions().add(question);

                        question = new AmountTiles(i, -1, this.game.getSolution());
                        this.game.getQuestions().add(question);

                        question = new SandAndGrassCheck(i, -1, this.game.getRealSolution());
                        this.game.getQuestions().add(question);

                        for (GameTile tile : this.game.getCurrentTiles()) {
                            question = new OneTileCheck(i, -1, this.game.getSolution(), this.game.getRealSolution(), tile.getTile().getTileNumber(), tile.getTile().getPossibilities().get(0));
                            this.game.getQuestions().add(question);
                        }
                    }
                    for (int i = 0; i < 5; i++) {
                        Question question = new Empty(-1, i, this.game.getRealSolution());
                        this.game.getQuestions().add(question);

                        question = new AmountTiles(-1, i, this.game.getSolution());
                        this.game.getQuestions().add(question);

                        question = new SandAndGrassCheck(-1, i, this.game.getRealSolution());
                        this.game.getQuestions().add(question);
                    }
                    break;
            }
        }
    }

    private void addQuestion() {
        for (int i = 0; i < QuestionEnum.values().length; i++) {
            String buttonFunction = FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.values()[i].name();
            if (getMainPanel().getButtonByFunction(buttonFunction).isSelect()) {
                int column = -1;
                int row = -1;

                for (String s : askOrder) {
                    String function = ArcheOLogicPanel.FUNCTION_QUESTION_ROW + s;
                    if (getMainPanel().getButtonByFunction(function).isSelect()) {
                        if (s.charAt(0) < 60) {
                            row = Integer.parseInt(s) - 1;
                        } else {
                            char c = (char)(s.charAt(0) - 17);
                            column = Integer.parseInt(String.valueOf(c));
                        }
                        break;
                    }
                }

                if (QuestionEnum.values()[i] == QuestionEnum.EMPTY) {
                    this.game.getQuestions().add(new Empty(column, row, this.game.getRealSolution()));
                } else if (QuestionEnum.values()[i] == QuestionEnum.AMOUNT_TILES) {
                    this.game.getQuestions().add(new AmountTiles(column, row, this.game.getSolution()));
                } else if (QuestionEnum.values()[i] == QuestionEnum.SAND_AND_GRASS_CHECK) {
                    this.game.getQuestions().add(new SandAndGrassCheck(column, row, this.game.getRealSolution()));
                } else if (QuestionEnum.values()[i] == QuestionEnum.ONE_TILE_CHECK) {
                    ApoButtonImageDropdown buttonByFunction = (ApoButtonImageDropdown)getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN);
                    this.game.getQuestions().add(new OneTileCheck(column, row, this.game.getSolution(), this.game.getRealSolution(), buttonByFunction.getTileNumber(), buttonByFunction.getTileArray()));
                }
                this.curAskOrder += this.curWantedQuestion + 1;
                if (this.curAskOrder >= askOrder.length) {
                    this.curAskOrder = 0;
                }
                String nextFunction = ArcheOLogicPanel.FUNCTION_QUESTION_ROW + askOrder[this.curAskOrder];
                for (String s : askOrder) {
                    String function = ArcheOLogicPanel.FUNCTION_QUESTION_ROW + s;
                    getMainPanel().getButtonByFunction(function).setSelect(false);
                    if (function.equals(nextFunction)) {
                        getMainPanel().getButtonByFunction(function).setSelect(true);
                    }
                }
                break;
            }
        }

        this.setShowQuestion(false);
    }



    private void setCurAddQuestionString(String s) {
        char c = s.charAt(0);
        if (c < 60) {
            this.curAddQuestionString = Localization.getInstance().getCommon().get("question_add_row")+" "+s+"?";
        } else {
            this.curAddQuestionString = Localization.getInstance().getCommon().get("question_add_column")+" "+s+"?";
        }
    }

    public void mouseWheelChanged(int changed) {
        this.game.addCurStartQuestion(changed);
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

        getMainPanel().drawString(Constants.PROPERTY_NAME, (Constants.GAME_WIDTH - 300), 100, Constants.COLOR_BLACK, AssetLoader.font30, DrawString.MIDDLE, true, false);

        getMainPanel().spriteBatch.draw(AssetLoader.coinTextureRegion, (Constants.GAME_WIDTH - 150), 80, 60, 60);
        getMainPanel().drawString(String.valueOf(this.game.getCosts()), (Constants.GAME_WIDTH - 120), 110, Constants.COLOR_BLACK, AssetLoader.font30, DrawString.MIDDLE, true, false);

        for (int i = 0; i < askOrder.length; i++) {
            float[] color = Constants.COLOR_BLACK;
            if (i == this.curAskOrder) {
                color = Constants.COLOR_RED_DARK;
            }
            getMainPanel().drawString(askOrder[i], Constants.GAME_WIDTH - 550 + i * 45, 170, color, AssetLoader.font30, DrawString.MIDDLE, true, false);
        }

        for (int i = this.game.getCurStartQuestion(); i < this.game.getCurStartQuestion() + GameEntity.MAX_SHOWN_QUESTION && i < this.game.getQuestions().size(); i++) {
            getMainPanel().drawString(this.game.getQuestions().get(i).getText(), Constants.GAME_WIDTH - 575, 200 + (i - this.game.getCurStartQuestion()) * 25, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, true, false);
        }

        for (GameTile tile : this.game.getCurrentTiles()) {
            tile.render(getMainPanel());
        }

        getMainPanel().spriteBatch.end();

        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);

        getMainPanel().getRenderer().setColor(Constants.COLOR_BLACK[0], Constants.COLOR_BLACK[1], Constants.COLOR_BLACK[2], 1f);
        for (int i = this.game.getCurStartQuestion(); i < this.game.getCurStartQuestion() + GameEntity.MAX_SHOWN_QUESTION && i < this.game.getQuestions().size(); i++) {
            this.game.getQuestions().get(i).draw(getMainPanel(), Constants.GAME_WIDTH - 600, 200 + (i-this.game.getCurStartQuestion()) * 25, 6);
        }

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

        if (this.showQuestion) {
            getMainPanel().spriteBatch.begin();

            getMainPanel().spriteBatch.draw(AssetLoader.backgroundQuestionTextureRegion, Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() - 10, 50);

            for (int i = 0; i < QuestionEnum.values().length; i++) {
                BitmapFont font15 = AssetLoader.font15;
                String text = Localization.getInstance().getCommon().get(QuestionEnum.values()[i].getText())+" ";
                getMainPanel().drawString(text, Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() - 10 + 100, 285 + i * (50 + 10), Constants.COLOR_BLACK, font15, DrawString.BEGIN, true, false);
                Constants.glyphLayout.setText(font15, text);
                getMainPanel().drawString(this.curAddQuestionString, Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() - 10 + 100 + Constants.glyphLayout.width, 285 + i * (50 + 10), Constants.COLOR_BLACK, font15, DrawString.BEGIN, true, false);
            }

            getMainPanel().spriteBatch.end();
        }

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
