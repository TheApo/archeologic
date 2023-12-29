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
    public static final String FUNCTION_OK = "ARCHEOLOGIC_OK";
    public static final String FUNCTION_CANCEL = "ARCHEOLOGIC_CANCEL";
    public static final String FUNCTION_FINISH_NEW_LEVEL = "ARCHEOLOGIC_FINISH_NEW_LEVEL";
    public static final String FUNCTION_FINISH_RESTART = "ARCHEOLOGIC_FINISH_RESTART";
    public static final String FUNCTION_QUESTION_TEST = "ARCHEOLOGIC_QUESTION_TEST";
    public static final String FUNCTION_QUESTION_CHECK = "ARCHEOLOGIC_QUESTION_CHECK";
    public static final String FUNCTION_QUESTION_QUESTION = "ARCHEOLOGIC_QUESTION_QUESTION";
    public static final String FUNCTION_QUESTION_ROW = "ARCHEOLOGIC_QUESTION_ROW_";
    public static final String FUNCTION_QUESTION_QUESTION_ASK = "ARCHEOLOGIC_QUESTION_QUESTION_";
    public static final String FUNCTION_QUESTION_QUESTION_REAL = "ARCHEOLOGIC_QUESTION_QUESTION_REAL";
    public static final String FUNCTION_QUESTION_QUESTION_DROPDOWN = "ARCHEOLOGIC_QUESTION_QUESTION_DROPDOWN";
    public static final String FUNCTION_QUESTION_QUESTION_DROPDOWN_HORIZONTAL = "ARCHEOLOGIC_QUESTION_QUESTION_DROPDOWN_HORIZONTAL";
    public static final String FUNCTION_QUESTION_QUESTION_DROPDOWN_STRING_SIDE = "ARCHEOLOGIC_QUESTION_QUESTION_DROPDOWN_STRING_SIDE";
    public static final String FUNCTION_QUESTION_QUESTION_CLOSE = "ARCHEOLOGIC_QUESTION_QUESTION_CLOSE";
    public static final String FUNCTION_HINT_UP = "ARCHEOLOGIC_HINT_UP";
    public static final String FUNCTION_HINT_DOWN = "ARCHEOLOGIC_HINT_DOWN";

    public static final String[] askOrder = new String[] {
            "A", "1", "B", "2", "C", "3", "D", "4", "E", "5"
    };

    private final Question[] nextQuestions = new Question[QuestionEnum.values().length];

    private int curAskOrder = 0;
    private int curWantedQuestion = 0;

    private String curAddQuestionString = "";
    private final boolean[] keys = new boolean[256];

    private boolean isPressed = false;

    private GameEntity game;

    private boolean showQuestion = false;

    private boolean won = false;

    private boolean checkQuestion = false;
    private String[] demandText = null;
    private DemandQuestion demandQuestion = DemandQuestion.SOLVING;

    private int nextCosts = 0;

    public ArcheOLogicPanel(final MainPanel game) {
        super(game);
    }

    public void setNeededButtonsVisible() {
        this.setAllButtonVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_ARCHEOLOGIC_BACK).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_RESTART).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_NEW_LEVEL).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_TEST).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_CHECK).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION).setVisible(true);
    }

    private void setAllButtonVisible(boolean visible) {
        getMainPanel().getButtonByFunction(FUNCTION_ARCHEOLOGIC_BACK).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_RESTART).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_NEW_LEVEL).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_TEST).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_CHECK).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_BACK).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_OK).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_CANCEL).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_RESTART).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_NEW_LEVEL).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_REAL).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_HORIZONTAL).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_STRING_SIDE).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_CLOSE).setVisible(visible);
        for (String s : askOrder) {
            getMainPanel().getButtonByFunction(FUNCTION_QUESTION_ROW + s).setVisible(visible);
        }
        for (int i = 0; i < QuestionEnum.values().length; i++) {
            getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.values()[i].name()).setVisible(visible);
        }

        getMainPanel().getButtonByFunction(FUNCTION_HINT_UP).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_HINT_DOWN).setVisible(false);
        if (this.game != null && this.game.getQuestions().size() > GameEntity.MAX_SHOWN_QUESTION) {
            getMainPanel().getButtonByFunction(FUNCTION_HINT_UP).setVisible(true);
            getMainPanel().getButtonByFunction(FUNCTION_HINT_DOWN).setVisible(true);
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

            buttonByFunction = (ApoButtonImageDropdown)(this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_HORIZONTAL));
            buttonByFunction.setCurTiles(this.game.getCurrentTiles());

        }
        this.curAddQuestionString = Localization.getInstance().getCommon().get("question_add_column")+" A?";

        this.won = false;
        this.checkQuestion = false;

        this.getMainPanel().resetSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        this.setNeededButtonsVisible();
    }

    private void setAllNextQuestions() {
        for (int i = 0; i < QuestionEnum.values().length; i++) {
            this.nextQuestions[i] = getNextQuestion(i);
        }
    }

    private void setNextCosts() {
        for (int i = 0; i < QuestionEnum.values().length; i++) {
            String buttonFunction = FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.values()[i].name();
            if (getMainPanel().getButtonByFunction(buttonFunction).isSelect()) {
                setNextCosts(this.nextQuestions[i]);
                return;
            }
        }
    }

    private void setNextCosts(Question question) {
        int nextAsk = question.withAskCosts() ? getNextCostsAsk() : 0;
        this.nextCosts = nextAsk + question.getCosts();
    }

    private int getNextCostsAsk() {
        return this.curAskOrder != this.curWantedQuestion ? 2 : 1;
    }

    public void setNewLevel(Difficulty difficulty) {
        this.game.setNewLevel(difficulty);

        this.won = false;
        this.checkQuestion = false;

        ApoButtonImageDropdown buttonByFunction = (ApoButtonImageDropdown)(this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN));
        buttonByFunction.setCurTiles(this.game.getCurrentTiles());

        buttonByFunction = (ApoButtonImageDropdown)(this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_HORIZONTAL));
        buttonByFunction.setCurTiles(this.game.getCurrentTiles());

        setAllNextQuestions();
        setNextCosts();
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

        this.won = false;
        this.checkQuestion = false;

        this.game.choseNewSolution();
        this.resetTiles();

        ApoButtonImageDropdown buttonByFunction = (ApoButtonImageDropdown)(this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN));
        buttonByFunction.setCurTiles(this.game.getCurrentTiles());

        buttonByFunction = (ApoButtonImageDropdown)(this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_HORIZONTAL));
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

//        if (this.isPressed) {
//            this.mouseButtonReleased(mouseX, mouseY, false);
//        }
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

        if (this.isPressed) {
            this.game.mouseDragged(x, y, isRightButton);
        }
    }

    private void setShowQuestion(boolean showQuestion) {
        this.showQuestion = showQuestion;

        this.setAllButtonVisible(false);

        if (this.showQuestion) {
            getMainPanel().getButtonByFunction(FUNCTION_HINT_UP).setVisible(false);
            getMainPanel().getButtonByFunction(FUNCTION_HINT_DOWN).setVisible(false);
        }

        getMainPanel().getButtonByFunction(FUNCTION_ARCHEOLOGIC_BACK).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_RESTART).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_NEW_LEVEL).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_TEST).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_CHECK).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_REAL).setVisible(showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_CLOSE).setVisible(showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN).setVisible(showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_HORIZONTAL).setVisible(showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_STRING_SIDE).setVisible(showQuestion);
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
            if (function.equals(ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_DROPDOWN)) {
                setAllNextQuestions();
                mouseButtonFunction(FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.ONE_TILE_CHECK.name());
                return;
            }
            if (function.equals(ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_DROPDOWN_HORIZONTAL) || function.equals(ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_DROPDOWN_STRING_SIDE)) {
                setAllNextQuestions();
                mouseButtonFunction(FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.HORIZONTAL_VERTICAL_BORDER_CHECK.name());
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
                    setAllNextQuestions();
                    setNextCosts();
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
                    setNextCosts(this.nextQuestions[i]);
                    return;
                }
            }
        } else if (this.checkQuestion) {
            super.mouseButtonFunction(function);
            switch (function) {
                case ArcheOLogicPanel.FUNCTION_OK:
                    if (this.demandQuestion == DemandQuestion.SOLVING) {
                        if (this.game.isWon()) {
                            setCheckQuestion(false, null, DemandQuestion.NOTHING);
                            setWon(true);
                        } else {
                            Question nextQuestion = new ErrorQuestion();
                            nextQuestion.setCompleteCosts(nextQuestion.getCosts());
                            this.game.setCosts(this.getGame().getCosts() + nextQuestion.getCosts());
                            this.game.getQuestions().add(nextQuestion);
                            setCheckQuestion(true, Localization.getInstance().getCommon().get("demand_error").split(";"), DemandQuestion.ERROR);
                        }
                        break;
                    } else {
                        setCheckQuestion(false, null, DemandQuestion.NOTHING);
                    }
                    break;
                case ArcheOLogicPanel.FUNCTION_CANCEL:
                    setCheckQuestion(false, null, DemandQuestion.SOLVING);
                    break;
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
                    resetTiles();
                    break;
                case ArcheOLogicPanel.FUNCTION_HINT_UP:
                    this.game.addCurStartQuestion(-1);
                    break;
                case ArcheOLogicPanel.FUNCTION_HINT_DOWN:
                    this.game.addCurStartQuestion(1);
                    break;
                case ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION:
                    this.setShowQuestion(true);
                    break;
                case ArcheOLogicPanel.FUNCTION_QUESTION_CHECK:
                    setCheckQuestion(true, Localization.getInstance().getCommon().get("demand_solve").split(";"), DemandQuestion.SOLVING);
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
                    //this.setButtonsVisible(false);
                    this.setShowQuestion(false);
                    this.game.addCurStartQuestion(1000);
                    break;
            }
        }
    }

    private void resetTiles() {
        this.won = false;
        this.checkQuestion = false;
        this.game.resetTiles();
        this.game.setCosts(0);
        this.resetCurAsk();
        this.setNeededButtonsVisible();
    }

    private void resetCurAsk() {
        this.curAskOrder = 0;
        this.curWantedQuestion = 0;
        setNextAskButtonsSelected();
    }

    private void setCheckQuestion(boolean checkQuestion, String[] text, DemandQuestion demandQuestion) {
        this.checkQuestion = checkQuestion;
        this.demandText = text;
        this.demandQuestion = demandQuestion;

        getMainPanel().getButtonByFunction(FUNCTION_OK).setVisible(checkQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_CANCEL).setVisible(false);
        if (this.demandQuestion == DemandQuestion.SOLVING) {
            getMainPanel().getButtonByFunction(FUNCTION_CANCEL).setVisible(checkQuestion);
        }
    }

    private void setWon(boolean won) {
        this.won = won;

        if (won) {
            this.setAllButtonVisible(false);

            getMainPanel().getButtonByFunction(FUNCTION_FINISH_BACK).setVisible(true);
            getMainPanel().getButtonByFunction(FUNCTION_FINISH_RESTART).setVisible(true);
            getMainPanel().getButtonByFunction(FUNCTION_FINISH_NEW_LEVEL).setVisible(true);
        }
    }

    private Question getNextQuestion() {
        return getNextQuestion(-1);
    }
    private Question getNextQuestion(int selectedQuestion) {
        for (int i = 0; i < QuestionEnum.values().length; i++) {
            String buttonFunction = FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.values()[i].name();
            if ((getMainPanel().getButtonByFunction(buttonFunction).isSelect() && selectedQuestion == -1) || (selectedQuestion == i)) {
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

                Question nextQuestion = null;
                if (QuestionEnum.values()[i] == QuestionEnum.EMPTY) {
                    nextQuestion = new Empty(column, row, this.game.getRealSolution());
                } else if (QuestionEnum.values()[i] == QuestionEnum.AMOUNT_TILES) {
                    nextQuestion = new AmountTiles(column, row, this.game.getSolution());
                } else if (QuestionEnum.values()[i] == QuestionEnum.SAND_AND_GRASS_CHECK) {
                    nextQuestion = new SandAndGrassCheck(column, row, this.game.getRealSolution());
                } else if (QuestionEnum.values()[i] == QuestionEnum.ONE_TILE_CHECK) {
                    ApoButtonImageDropdown buttonByFunction = (ApoButtonImageDropdown)getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN);
                    nextQuestion = new OneTileCheck(column, row, this.game.getSolution(), this.game.getRealSolution(), buttonByFunction.getTileNumber(), buttonByFunction.getTileArray());
                } else if (QuestionEnum.values()[i] == QuestionEnum.HORIZONTAL_VERTICAL_BORDER_CHECK) {
                    ApoButtonImageDropdown buttonByFunction = (ApoButtonImageDropdown)getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_HORIZONTAL);

                    ApoButtonImageDropdown buttonByFunctionString = (ApoButtonImageDropdown)getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_STRING_SIDE);

                    if (buttonByFunctionString.getTileNumber() == 0) {
                        nextQuestion = new OneTileSideCheck(-1, 1, this.game.getSolution(), buttonByFunction.getTileNumber(), buttonByFunction.getTileArray());
                    } else if (buttonByFunctionString.getTileNumber() == 1) {
                        nextQuestion = new OneTileSideCheck(1, -1, this.game.getSolution(), buttonByFunction.getTileNumber(), buttonByFunction.getTileArray());
                    } else {
                        nextQuestion = new OneTileSideCheck(1, 1, this.game.getSolution(), buttonByFunction.getTileNumber(), buttonByFunction.getTileArray());
                    }
                }

                return nextQuestion;
            }
        }
        return null;
    }

    private void addQuestion() {
        Question nextQuestion = getNextQuestion();

        int completeCost = 0;

        if (nextQuestion != null) {
            completeCost = nextQuestion.getCosts();
            this.getGame().setCosts(this.getGame().getCosts() + nextQuestion.getCosts());
            this.game.getQuestions().add(nextQuestion);

            if (nextQuestion.withAskCosts()) {
                completeCost += getNextCostsAsk();
                this.getGame().setCosts(this.getGame().getCosts() + getNextCostsAsk());
            }
        }

        this.curAskOrder = this.curWantedQuestion + 1;
        if (this.curAskOrder >= askOrder.length) {
            this.curAskOrder = 0;
        }
        this.curWantedQuestion = this.curAskOrder;
        setNextAskButtonsSelected();

        if (nextQuestion != null) {
            nextQuestion.setCompleteCosts(completeCost);
        }

        this.game.addCurStartQuestion(1);

        this.setShowQuestion(false);
    }

    private void setNextAskButtonsSelected() {
        String nextFunction = ArcheOLogicPanel.FUNCTION_QUESTION_ROW + askOrder[this.curAskOrder];
        for (String s : askOrder) {
            String function = ArcheOLogicPanel.FUNCTION_QUESTION_ROW + s;
            getMainPanel().getButtonByFunction(function).setSelect(false);
            if (function.equals(nextFunction)) {
                getMainPanel().getButtonByFunction(function).setSelect(true);
                setCurAddQuestionString(s);
            }
        }
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
        getMainPanel().drawString(String.valueOf(this.game.getCosts()), (Constants.GAME_WIDTH - 120), 113, Constants.COLOR_BLACK, AssetLoader.font30, DrawString.MIDDLE, true, false);

        getMainPanel().drawString(Localization.getInstance().getCommon().get("question_order"), Constants.GAME_WIDTH - 600, 170, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.BEGIN, true, false);
        getMainPanel().drawString(Localization.getInstance().getCommon().get("question_hint"), Constants.GAME_WIDTH - 600, 220, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.BEGIN, true, false);

        for (int i = 0; i < askOrder.length; i++) {
            float[] color = Constants.COLOR_BLACK;
            if (i == this.curAskOrder) {
                color = Constants.COLOR_RED_DARK;
            }
            getMainPanel().drawString(askOrder[i], Constants.GAME_WIDTH - 450 + i * 45, 170, color, AssetLoader.font30, DrawString.MIDDLE, true, false);
        }

        for (int i = this.game.getCurStartQuestion(); i < this.game.getCurStartQuestion() + GameEntity.MAX_SHOWN_QUESTION && i < this.game.getQuestions().size(); i++) {
            String text = this.game.getQuestions().get(i).getText();

            renderTextAndTileQuestion(text, Constants.GAME_WIDTH - 575, 250 + (i - this.game.getCurStartQuestion()) * 25);

            if (this.game.getQuestions().get(i).getCompleteCosts() > 0) {
                getMainPanel().spriteBatch.draw(AssetLoader.coinTextureRegion, (Constants.GAME_WIDTH - 109), 238 + (i - this.game.getCurStartQuestion()) * 25, 20, 20);
                getMainPanel().drawString(String.valueOf(this.game.getQuestions().get(i).getCompleteCosts()), Constants.GAME_WIDTH - 100, 250 + (i - this.game.getCurStartQuestion()) * 25, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, true, false);
            }
        }

        for (GameTile tile : this.game.getCurrentTiles()) {
            tile.render(getMainPanel());
        }

        getMainPanel().spriteBatch.end();

        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);

        getMainPanel().getRenderer().setColor(Constants.COLOR_BLACK[0], Constants.COLOR_BLACK[1], Constants.COLOR_BLACK[2], 1f);
        for (int i = this.game.getCurStartQuestion(); i < this.game.getCurStartQuestion() + GameEntity.MAX_SHOWN_QUESTION && i < this.game.getQuestions().size(); i++) {
            this.game.getQuestions().get(i).draw(getMainPanel(), Constants.GAME_WIDTH - 600, 250 + (i-this.game.getCurStartQuestion()) * 25, 6);
        }

        for (GameTile tile : this.game.getCurrentTiles()) {
            tile.renderLine(getMainPanel());
        }

        getMainPanel().getRenderer().end();

        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        getMainPanel().spriteBatch.begin();
        this.game.renderGivenTile(this.getMainPanel());
        getMainPanel().spriteBatch.end();
        Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);

        boolean showCoinNextCostAsk = false;

        if (this.showQuestion) {
            getMainPanel().spriteBatch.begin();

            getMainPanel().spriteBatch.draw(AssetLoader.backgroundQuestionTextureRegion, Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() - 10, 10, AssetLoader.backgroundQuestionTextureRegion.getRegionWidth(), Constants.GAME_HEIGHT - 20);

            getMainPanel().drawString(Localization.getInstance().getCommon().get("question_title"), Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth()/2f - 10, 85, Constants.COLOR_BLACK, AssetLoader.font30, DrawString.MIDDLE, true, false);

            getMainPanel().drawString(Localization.getInstance().getCommon().get("question_second"), Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth()/2f - 10, 240, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, true, false);
            getMainPanel().drawString(Localization.getInstance().getCommon().get("question_third"), Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth()/2f - 10, Constants.GAME_HEIGHT - 105, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, true, false);

            for (int i = 0; i < QuestionEnum.values().length; i++) {
                getMainPanel().spriteBatch.draw(AssetLoader.coinTextureRegion, Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() - 10 + 85, 275 + i * (50 + 10) - 17, 35, 35);
                getMainPanel().drawString(String.valueOf(this.nextQuestions[i].getCosts()), Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() - 10 + 101, 279 + i * (50 + 10), Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, true, false);

                BitmapFont font15 = AssetLoader.font15;
                String text = Localization.getInstance().getCommon().get(QuestionEnum.values()[i].getText())+" ";

                float startX = renderTextAndTileQuestion(text, Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() - 10 + 130, 277 + i * (50 + 10));

                //getMainPanel().drawString(text, Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() - 10 + 130, 277 + i * (50 + 10), Constants.COLOR_BLACK, font15, DrawString.BEGIN, true, false);
                if (!QuestionEnum.values()[i].equals(QuestionEnum.HORIZONTAL_VERTICAL_BORDER_CHECK)) {
                    getMainPanel().drawString(this.curAddQuestionString, startX, 277 + i * (50 + 10), Constants.COLOR_BLACK, font15, DrawString.BEGIN, true, false);
                }
                ApoButton button = getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.values()[i].name());
                if (button.isSelect() && !QuestionEnum.values()[i].equals(QuestionEnum.HORIZONTAL_VERTICAL_BORDER_CHECK)) {
                    showCoinNextCostAsk = true;
                }
            }

            if (showCoinNextCostAsk) {
                getMainPanel().drawString(Localization.getInstance().getCommon().get("question_first").split(";")[0], Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth()/2f - 10, 140, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, true, false);

                getMainPanel().spriteBatch.draw(AssetLoader.coinTextureRegion, Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() + 80, 140 - 20, 35, 35);
                getMainPanel().drawString(String.valueOf(this.getNextCostsAsk()), Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() + 80 + 17, 141, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, true, false);
            }


            ApoButton button = getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_REAL);
            getMainPanel().drawString(Localization.getInstance().getCommon().get("question_costs"), button.getXMiddle(), button.getY() - 70,  Constants.COLOR_BLACK, AssetLoader.font25, DrawString.END, true, false);

            getMainPanel().spriteBatch.draw(AssetLoader.coinTextureRegion, button.getXMiddle() + 8, button.getY() - 70 - 22, 45, 45);
            getMainPanel().drawString(String.valueOf(this.nextCosts), button.getXMiddle() + 30, button.getY() - 67,  Constants.COLOR_BLACK, AssetLoader.font30, DrawString.MIDDLE, true, false);

            getMainPanel().spriteBatch.end();
        }

        if (this.checkQuestion) {
            getMainPanel().spriteBatch.begin();

            int size = 600;
            int height = 300;
            getMainPanel().spriteBatch.draw(AssetLoader.backgroundQuestionTextureRegion, Constants.GAME_WIDTH/2f - size/2f, Constants.GAME_HEIGHT/2f - height/2f, size, height);

            getMainPanel().drawString(Localization.getInstance().getCommon().get("demand_question"), Constants.GAME_WIDTH/2f, Constants.GAME_HEIGHT/2f - height/2f + 40, Constants.COLOR_BLACK, AssetLoader.font30, DrawString.MIDDLE, true, false);

            int addY = 0;
            for (String text: this.demandText) {
                getMainPanel().drawString(text, Constants.GAME_WIDTH/2f, Constants.GAME_HEIGHT/2f - height/2f + 90 + addY, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, true, false);
                addY += 30;
            }

            getMainPanel().spriteBatch.end();
        } else if (won) {
            getMainPanel().spriteBatch.begin();

            int size = 600;
            int height = 300;
            getMainPanel().spriteBatch.draw(AssetLoader.backgroundQuestionTextureRegion, Constants.GAME_WIDTH/2f - size/2f, Constants.GAME_HEIGHT/2f - height/2f, size, height);

            String s = Localization.getInstance().getCommon().get("won");
            getMainPanel().drawString(s, Constants.GAME_WIDTH/2f, Constants.GAME_HEIGHT/2f - height/2f + 40, Constants.COLOR_BLACK, AssetLoader.font30, DrawString.MIDDLE, true, false);

            s = Localization.getInstance().getCommon().get("won_text");
            s = s.replace("{x}", String.valueOf(this.game.getQuestions().size()));
            s = s.replace("{y}", String.valueOf(this.game.getCosts()));
            getMainPanel().drawString(s, Constants.GAME_WIDTH/2f, Constants.GAME_HEIGHT/2f - height/2f + 100, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, true, false);

            getMainPanel().spriteBatch.end();
        }

        for (ApoButton button : this.getMainPanel().getButtons()) {
            if (!showCoinNextCostAsk && this.showQuestion && button.getFunction().startsWith(FUNCTION_QUESTION_ROW)) {
                continue;
            }
            button.render(this.getMainPanel());
        }

        for (ApoButton button : this.getMainPanel().getButtons()) {
            if (!showCoinNextCostAsk && this.showQuestion && button.getFunction().startsWith(FUNCTION_QUESTION_ROW)) {
                continue;
            }
            if (button.isVisible() && button.isSelect()) {
                button.render(this.getMainPanel());
            }
        }

        if (this.showQuestion) {
            getMainPanel().spriteBatch.begin();

            for (int i = 0; i < askOrder.length; i++) {
                if (i == this.curAskOrder && showCoinNextCostAsk) {
                    float[] color = Constants.COLOR_RED;
                    getMainPanel().drawString(askOrder[i], Constants.GAME_WIDTH - 10 - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() + 30 + i * 64 + 32, 160 + 35, color, AssetLoader.font30, DrawString.MIDDLE, true, false);
                }
            }

            getMainPanel().spriteBatch.end();
        }
    }

    private float renderTextAndTileQuestion(String text, int realStartX, int y) {
        boolean run = true;
        float startX = realStartX;
        int startIndex = 0;
        while (run) {
            String curText = text.substring(startIndex);
            int nextIndex = text.substring(startIndex).indexOf("$");
            if (nextIndex > 0) {
                curText = text.substring(startIndex, startIndex + nextIndex);
                startIndex = startIndex + nextIndex + 2;
                Constants.glyphLayout.setText(AssetLoader.font15, curText);
            }
            getMainPanel().drawString(curText, startX, y, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, true, false);

            if (nextIndex > 0) {
                startX += Constants.glyphLayout.width + 30;

                int value = 0;
                if (text.charAt(startIndex - 1) == 'F') {
                    value =  1;
                } else if (text.charAt(startIndex - 1) == 'G') {
                    value =  2;
                } else if (text.charAt(startIndex - 1) == 'W') {
                    value =  3;
                }
                getMainPanel().spriteBatch.draw(AssetLoader.backgroundTextureRegion[value], startX - 25, y - 11, 20, 20);
            } else {
                Constants.glyphLayout.setText(AssetLoader.font15, curText);
                startX += Constants.glyphLayout.width;
                run = false;
            }
        }
        return startX;
    }

    @Override
    public void dispose() {
    }
}
