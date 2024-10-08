package com.apogames.game.question;

import com.apogames.ArcheOLogic;
import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.GameScreen;
import com.apogames.common.Localization;
import com.apogames.game.archeologic.ArcheOLogicPanel;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class AllOneSpecificPosition extends Question {

    private class SpecificPosition {
        private final int column;
        private final int row;
        private final int answer;

        public SpecificPosition(int column, int row, int answer) {
            this.column = column;
            this.row = row;
            this.answer = answer;
        }

        public int getColumn() {
            return column;
        }

        public int getRow() {
            return row;
        }

        public int getAnswer() {
            return answer;
        }
    }

    private final List<SpecificPosition> answers;

    public AllOneSpecificPosition(final List<OneSpecificPosition> questions) {
        super();

        this.answers = new ArrayList<>();
        for (OneSpecificPosition question : questions) {
            this.answers.add(new SpecificPosition(question.getColumn(), question.getRow(), question.getOneSpecificPositionAnswer()));
        }
    }

    @Override
    public int getCosts() {
        return 0;
    }

    @Override
    public String getText() {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for (SpecificPosition myAnswer : answers) {
            String answer = "";
            answer = Localization.getInstance().getCommon().get("question_one_specific");
            answer = answer.replace("{x}", String.valueOf(myAnswer.getRow()+1));
            char r = (char)(String.valueOf(myAnswer.getColumn()).charAt(0) + 17);
            answer = answer.replace("{y}", String.valueOf(r));
            answer = answer.replace("{answer}", this.getAnswer(myAnswer));

            result.append(answer);
            i += 1;
            if (i != this.answers.size()) {
                result.append(" - ");

                if (i % 2 == 0) {
                    result.append(";");
                }
            }
        }

        return result.toString();
    }

    @Override
    public String getAnswer() {
        return "";
    }

    private String getAnswer(SpecificPosition myAnswer) {
        if (myAnswer.getAnswer() == 2) {
            return Localization.getInstance().getCommon().get("question_grass");
        } else if (myAnswer.getAnswer() == 3) {
            return Localization.getInstance().getCommon().get("question_sand");
        } else if (myAnswer.getAnswer() == 1) {
            return Localization.getInstance().getCommon().get("question_forest");
        } else {
            return Localization.getInstance().getCommon().get("question_water");
        }
    }

    @Override
    public ArrayList<Integer> filter(ArrayList<byte[][]> solutionsReal, ArrayList<byte[][]> possibleSolutionsPossibilities, ArrayList<byte[][]> solutions) {
        ArrayList<Integer> results = new ArrayList<>();
        for (int i = 0; i < solutionsReal.size(); i++) {
            byte[][] currentSolution = solutionsReal.get(i);

            int answerCount = 0;
            for (SpecificPosition myAnswer : answers) {
                if (currentSolution[myAnswer.getRow()][myAnswer.getColumn()] == myAnswer.getAnswer()) {
                    answerCount += 1;
                }
            }

            if (answerCount == this.answers.size()) {
                results.add(i);
            }
        }
        return results;
    }

    @Override
    public void draw(GameScreen screen, int x, int y, int size) {

    }

    public void renderSprite(GameScreen screen, int changeX, int changeY) {
        if (this.getX() < 0) {
            return;
        }
        super.renderSprite(screen, changeX, changeY);
        int size = 80;
        float startX = getX() + changeX + getWidth()/2f - size/2f;
        float startY = getY() + changeY + 5;
        screen.spriteBatch.draw(AssetLoader.boardTextureLittleRegion, startX, startY, size, size);

        float calc = size / (float)AssetLoader.boardTextureLittleRegion.getRegionWidth();
        for (SpecificPosition myAnswer : answers) {
            int answer = myAnswer.getAnswer();
            if (answer == 3) {
                answer = 0;
            }
            screen.spriteBatch.draw(AssetLoader.backgroundTextureRegion[answer], startX + 18 * calc + 17 * calc * myAnswer.getColumn(), startY + 18 * calc + 17 * calc * myAnswer.getRow(), 17 * calc, 17 * calc);
        }
    }
}
