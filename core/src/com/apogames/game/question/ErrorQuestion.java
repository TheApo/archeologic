package com.apogames.game.question;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.DrawString;
import com.apogames.backend.GameScreen;
import com.apogames.common.Localization;
import com.apogames.game.archeologic.ArcheOLogicPanel;
import com.apogames.help.Helper;

import java.util.ArrayList;

public class ErrorQuestion extends Question {

    public ErrorQuestion() {
        super();
    }

    @Override
    public void draw(GameScreen screen, int addX, int addY, int size) {
    }

    public boolean withAskCosts() {
        return false;
    }

    @Override
    public int getCosts() {
        return 8;
    }

    @Override
    public String getText() {
        return getAnswer();
    }

    @Override
    public String getAnswer() {
        return Localization.getInstance().getCommon().get("question_error");
    }

    @Override
    public ArrayList<Integer> filter(ArrayList<byte[][]> solutionsReal, ArrayList<byte[][]> possibleSolutionsPossibilities, ArrayList<byte[][]> solutions) {
        ArrayList<Integer> results = new ArrayList<>();
        for (int i = 0; i < solutionsReal.size(); i++) {
            results.add(i);
        }
        return results;
    }

    public void renderSprite(GameScreen screen, int changeX, int changeY) {
        if (this.getX() < 0) {
            return;
        }
        super.renderSprite(screen, changeX, changeY);
        float startX = getX() + changeX + getWidth()/2f - 10;
        float startY = getY() + changeY + 50;
        screen.drawString(Localization.getInstance().getCommon().get("demand_question_error"), startX, startY, Constants.COLOR_RED_DARK, AssetLoader.font25, DrawString.MIDDLE, true, false);
    }
}
