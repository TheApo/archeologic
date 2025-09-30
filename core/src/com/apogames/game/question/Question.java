package com.apogames.game.question;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.DrawString;
import com.apogames.backend.GameScreen;
import com.apogames.entity.ApoEntity;
import com.apogames.game.archeologic.ArcheOLogicPanel;
import com.apogames.game.question.QuestionTextRenderer.TextSegment;

import java.util.ArrayList;
import java.util.List;

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
        // Use new flexible rendering system if question supports it
        if (supportsFlexibleRendering()) {
            renderFlexibleQuestion(screen, changeX, changeY);
        } else {
            // Fallback to enhanced system that handles both old and new formats
            QuestionTextRenderer.renderEnhancedTextAndTileQuestion(screen, text, (int)(this.getX() + changeX), (int)(this.getY() + changeY), this.error);
        }
    }

    /**
     * Override this method to use the new flexible rendering system
     */
    protected boolean supportsFlexibleRendering() {
        return false;
    }

    /**
     * Render question using the flexible text segment system
     */
    protected void renderFlexibleQuestion(GameScreen screen, int changeX, int changeY) {
        if (questionEnum != null) {
            List<TextSegment> segments = QuestionTextRenderer.parseQuestionText(questionEnum.getText());

            float startX = this.getX() + changeX;
            float startY = this.getY() + changeY;

            QuestionTextRenderer.renderQuestionSegments(
                screen, segments, startX, startY,
                AssetLoader.font20, Constants.COLOR_BLACK,
                5.0f, this
            );
        }
    }

    /**
     * Override this method to provide custom tile visual data for rendering
     */
    protected Object getTileVisualData(int segmentIndex) {
        return null;
    }

    /**
     * Override this method to provide custom dropdown data for rendering
     */
    protected Object getDropdownData(int segmentIndex) {
        return null;
    }

    public void renderFilled(GameScreen screen, int changeX, int changeY) {
        if (this.getX() >= 0) {
            screen.getRenderer().roundedRect(getX() + changeX, getY() + changeY, getWidth() - 2, getHeight() - 2, 5);
        }
    }

    public void renderLine(GameScreen screen, int changeX, int changeY) {
        if (this.getX() >= 0) {
            if (this.error) {
                screen.getRenderer().setColor(Constants.COLOR_RED[0], Constants.COLOR_RED[1], Constants.COLOR_RED[2], 1f);
            } else {
                screen.getRenderer().setColor(Constants.COLOR_BLACK[0], Constants.COLOR_BLACK[1], Constants.COLOR_BLACK[2], 1f);
            }
            screen.getRenderer().roundedRectLine(getX() + changeX, getY() + changeY, getWidth() - 2, getHeight() - 2, 5);
        }
    }

    public void renderSprite(GameScreen screen, int changeX, int changeY) {
        if (this.getX() >= 0) {
            if (getCompleteCosts() > 0) {
                screen.spriteBatch.draw(AssetLoader.coinTextureRegion, getX() + changeX + getWidth() - 35, getY() + changeY + 5, 30, 30);
                screen.drawString(String.valueOf(getCompleteCosts()), getX() + changeX + getWidth() - 35 + 14, getY() + changeY + 5 + 17, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, true, false);
            }
            renderTextAndTile(screen, changeX, changeY);
        }
    }

    public void renderTextAndTile(GameScreen screen, int changeX, int changeY) {
        if (this.getX() < 0) {
            return;
        }

        String[] myText = this.getText().split(";");
        int i = 0;
        for (String text : myText) {
            ArcheOLogicPanel.renderTextAndTileQuestion(screen, text, (int)(changeX + getX() + 10), (int)(changeY + getY() + 100 + i * 20), hasErrors());
            i += 1;
        }
    }

    public void renderIsle(GameScreen screen, int changeX, int changeY, int size, byte[][] tile) {
        float startX = changeX + getX() + getWidth()/2 - tile[0].length * size / 2f;
        float startY = changeY + getY() + 10;

        for (int y = 0; y < tile.length; y++) {
            for (int x = 0; x < tile[0].length; x++) {
                if (tile[y][x] != 0) {
                    int curTile = tile[y][x];
                    if (curTile == 3) {
                        curTile = 0;
                    }
                    screen.spriteBatch.draw(AssetLoader.backgroundTextureRegion[curTile], startX + x * size, startY + y * size, size, size);
                }
            }
        }
    }

    public void renderIsleFilled(GameScreen screen, int changeX, int changeY, int size, byte[][] tile) {
        float startX = changeX + getX() + getWidth()/2 - tile[0].length * size / 2f;
        float startY = changeY + getY() + 10;

        screen.getRenderer().setColor(Constants.COLOR_BLACK[0], Constants.COLOR_BLACK[1], Constants.COLOR_BLACK[2], 1f);
        for (int y = 0; y < tile.length; y++) {
            for (int x = 0; x < tile[0].length; x++) {
                if (tile[y][x] != 0) {
                    screen.getRenderer().rect(startX + x * size - 1, startY + y * size - 1, size + 2, size + 2);
                }
            }
        }
    }
}
