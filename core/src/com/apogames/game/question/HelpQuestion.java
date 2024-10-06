package com.apogames.game.question;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.DrawString;
import com.apogames.backend.GameScreen;
import com.apogames.common.Localization;

import java.util.ArrayList;

public class HelpQuestion extends Question {

    public HelpQuestion() {
        super();
    }

    @Override
    public void draw(GameScreen screen, int addX, int addY, int size) {
    }

    @Override
    public int getCosts() {
        return 1;
    }

    public boolean withAskCosts() {
        return false;
    }

    @Override
    public String getText() {
        return getAnswer();
    }

    @Override
    public String getAnswer() {
        return Localization.getInstance().getCommon().get("question_help_text");
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
        float startX = getX() + changeX + getWidth()/2f;
        float startY = getY() + changeY + 40;
        screen.drawString(Localization.getInstance().getCommon().get("demand_question_help"), startX, startY, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, true, false);
    }
}
