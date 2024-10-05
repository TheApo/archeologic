package com.apogames.game.question;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.DrawString;
import com.apogames.backend.GameScreen;
import com.apogames.entity.ApoEntity;
import com.apogames.game.archeologic.ArcheOLogicPanel;

import java.util.ArrayList;

public abstract class Question extends ApoEntity {

    public static final int ADD_COST = 2;
    public static final int DECREASE_COST = 1;

    private QuestionEnum questionEnum;

    private int row = -1;
    private int column = -1;

    private int completeCosts = 0;

    private int addCostsBecauseLast = 0;

    private String text;

    private boolean error = false;

    public Question() {
        super(-1, -1, 200, 150);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public abstract void draw(GameScreen screen, int x, int y, int size);
    public abstract int getCosts();
    public abstract String getText();

    public abstract String getAnswer();

    public abstract ArrayList<Integer> filter(ArrayList<byte[][]> possibleSolutionsReal, ArrayList<byte[][]> possibleSolutionsPossibilities, ArrayList<byte[][]> possibleSolutionsTile);

    public int getCompleteCosts() {
        return completeCosts;
    }

    public void setCompleteCosts(int completeCosts) {
        this.completeCosts = completeCosts;
    }

    public int getAddCostsBecauseLast() {
        return addCostsBecauseLast;
    }

    public void addCostsBecauseLast() {
        this.addCostsBecauseLast = this.addCostsBecauseLast + ADD_COST;
    }

    public void decreaseAddCost() {
        this.addCostsBecauseLast -= DECREASE_COST;
        if (this.addCostsBecauseLast < 0) {
            this.addCostsBecauseLast = 0;
        }
    }

    public void setAddCostsBecauseLast(int addCostsBecauseLast) {
        this.addCostsBecauseLast = addCostsBecauseLast;
    }

    public int getCostsWithAddCosts() {
        return this.getCosts() + this.getAddCostsBecauseLast();
    }

    public QuestionEnum getQuestionEnum() {
        return questionEnum;
    }

    protected void setQuestionEnum(QuestionEnum questionEnum) {
        this.questionEnum = questionEnum;
    }

    public boolean withAskCosts() {
        return true;
    }

    public boolean hasErrors() {
        return this.error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void click(int x, int y) {
        if (this.intersects(x, y)) {
            this.setSelect(!this.isSelect());
        }
    }

    public void render(GameScreen screen, int changeX, int changeY) {
        ArcheOLogicPanel.renderTextAndTileQuestion(screen, text, (int)(this.getX() + changeX), (int)(this.getY() + changeY), this.error);

//        if (getCompleteCosts() > 0) {
//            screen.spriteBatch.draw(AssetLoader.coinTextureRegion, (Constants.GAME_WIDTH - 109), 238 + (i - this.game.getCurStartQuestion()) * 25, 20, 20);
//            screen.drawString(String.valueOf(getCompleteCosts()), Constants.GAME_WIDTH - 100, 250 + (i - this.game.getCurStartQuestion()) * 25, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, true, false);
//        }
    }

    public void renderFilled(GameScreen screen, int changeX, int changeY) {
        if (this.getX() >= 0) {
            screen.getRenderer().setColor(Constants.COLOR_BLUE_BRIGHT[0], Constants.COLOR_BLUE_BRIGHT[1], Constants.COLOR_BLUE_BRIGHT[2], 1f);
            screen.getRenderer().roundedRect(getX() + changeX, getY() + changeY, getWidth(), getHeight(), 5);
        }
    }

    public void renderLine(GameScreen screen, int changeX, int changeY) {
        if (this.getX() >= 0) {
            if (this.error) {
                screen.getRenderer().setColor(Constants.COLOR_RED[0], Constants.COLOR_RED[1], Constants.COLOR_RED[2], 1f);
            } else {
                screen.getRenderer().setColor(Constants.COLOR_WHITE[0], Constants.COLOR_WHITE[1], Constants.COLOR_WHITE[2], 1f);
            }
            screen.getRenderer().roundedRectLine(getX() + changeX, getY() + changeY, getWidth(), getHeight(), 5);
        }
    }

    public void renderSprite(GameScreen screen, int changeX, int changeY) {
        if (this.getX() >= 0) {

        }
    }
}
