package com.apogames.game.question;

import com.apogames.backend.GameScreen;

import java.util.ArrayList;

public abstract class Question {

    public static final int ADD_COST = 2;
    public static final int DECREASE_COST = 1;

    private QuestionEnum questionEnum;

    private boolean correct = true;
    private int row = -1;
    private int column = -1;

    private int completeCosts = 0;

    private int addCostsBecauseLast = 0;

    private String text;

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

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public abstract void draw(GameScreen screen, int x, int y, int size);
    public abstract int getCosts();
    public abstract String getText();

    public abstract String getAnswer();

    public abstract ArrayList<Integer> filter(ArrayList<byte[][]> possibleSolutionsReal, ArrayList<byte[][]> possibleSolutionsPossibilities, ArrayList<byte[][]> possibleSolutions);

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
}
