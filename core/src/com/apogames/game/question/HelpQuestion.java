package com.apogames.game.question;

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
}