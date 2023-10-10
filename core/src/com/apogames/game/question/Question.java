package com.apogames.game.question;

import com.apogames.backend.GameScreen;

import java.util.ArrayList;

public abstract class Question {

    private int row = -1;
    private int column = -1;

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

    public abstract void draw(GameScreen screen, int x, int y, int size);
    public abstract int getCosts();
    public abstract String getText();

    public abstract String getAnswer();

    public abstract ArrayList<Integer> filter(ArrayList<byte[][]> solutionsReal, ArrayList<byte[][]> solutions);
}
