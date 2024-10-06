package com.apogames.game.archeologic;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.DrawString;
import com.apogames.backend.GameScreen;
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

import java.util.ArrayList;

public class ArcheOLogicPanel extends SequentiallyThinkingScreenModel {

    public static final int START_QUESTION_Y = 270;
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
    public static final String FUNCTION_QUESTION_QUESTION_DROPDOWN_EMPTY_NEXT_TILE = "ARCHEOLOGIC_QUESTION_QUESTION_DROPDOWN_EMPTY_NEXT_TILE";
    public static final String FUNCTION_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE = "ARCHEOLOGIC_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE";
    public static final String FUNCTION_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE_OTHER = "ARCHEOLOGIC_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE_OTHER";
    public static final String FUNCTION_QUESTION_QUESTION_DROPDOWN_HORIZONTAL = "ARCHEOLOGIC_QUESTION_QUESTION_DROPDOWN_HORIZONTAL";
    public static final String FUNCTION_QUESTION_QUESTION_DROPDOWN_CORNER = "ARCHEOLOGIC_QUESTION_QUESTION_DROPDOWN_CORNER";
    public static final String FUNCTION_QUESTION_QUESTION_DROPDOWN_STRING_SIDE = "ARCHEOLOGIC_QUESTION_QUESTION_DROPDOWN_STRING_SIDE";
    public static final String FUNCTION_QUESTION_QUESTION_CLOSE = "ARCHEOLOGIC_QUESTION_QUESTION_CLOSE";
    public static final String FUNCTION_HINT_UP = "ARCHEOLOGIC_HINT_UP";
    public static final String FUNCTION_HINT_DOWN = "ARCHEOLOGIC_HINT_DOWN";
    public static final String FUNCTION_QUESTIONS_ORIGINAL = "ARCHEOLOGIC_QUESTIONS_ORIGINAL";
    public static final String FUNCTION_QUESTIONS_OTHER = "ARCHEOLOGIC_QUESTIONS_OTHER";
    public static final String FUNCTION_QUESTIONS_NEXT_HINT = "ARCHEOLOGIC_QUESTIONS_NEXT_HINT";
    public static final String FUNCTION_QUESTIONS_HELP = "ARCHEOLOGIC_QUESTIONS_HELP";

    public static final String[] askOrder = new String[] {
            "A", "1", "B", "2", "C", "3", "D", "4", "E", "5"
    };
    private static final int ORIGINAL_QUESTIONS = 0;
    private static final int OTHER_QUESTIONS = 1;
    private static final int MAX_REPEAT = 20;

    private final Question[][] nextQuestions;

    private int curAskOrder = 0;
    private int curWantedQuestion = 0;

    private String curAddQuestionString = "";
    private final boolean[] keys = new boolean[256];

    private boolean isPressed = false;

    private GameEntity game;

    private boolean showQuestion = false;

    private int showTabIndex = ORIGINAL_QUESTIONS;

    private boolean won = false;

    private boolean checkQuestion = false;
    private String[] demandText = null;
    private DemandQuestion demandQuestion = DemandQuestion.SOLVING;

    private int nextCosts = 0;

    private Won wonEnum = Won.SOLVER;

    private boolean puzzle = false;

    public ArcheOLogicPanel(final MainPanel game) {
        super(game);

        QuestionEnum[] questionEnumForQuestionTypeOriginal = QuestionEnum.getQuestionEnumForQuestionType(0);
        nextQuestions = new Question[2][questionEnumForQuestionTypeOriginal.length];
    }

    public void setNeededButtonsVisible() {
        this.setAllButtonVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_ARCHEOLOGIC_BACK).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_RESTART).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_NEW_LEVEL).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_TEST).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_CHECK).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION).setVisible(true);
        setPuzzleButtonVisibility();
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
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_EMPTY_NEXT_TILE).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE_OTHER).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_HORIZONTAL).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_CORNER).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_STRING_SIDE).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_CLOSE).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTIONS_OTHER).setVisible(visible);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTIONS_ORIGINAL).setVisible(visible);
        for (String s : askOrder) {
            getMainPanel().getButtonByFunction(FUNCTION_QUESTION_ROW + s).setVisible(visible);
        }
        for (int i = 0; i < QuestionEnum.values().length; i++) {
            getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.values()[i].name()).setVisible(visible);
        }
        setPuzzleButtonVisibility();

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

            setCurTilesForButtons();
        }
        this.curAddQuestionString = Localization.getInstance().getCommon().get("question_add_column")+" A?";

        this.won = false;
        this.checkQuestion = false;
        this.resetCurAsk();

        this.getMainPanel().resetSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        this.setNeededButtonsVisible();
    }

    private void setCurTilesForButtons() {
        setCurTileInButton(FUNCTION_QUESTION_QUESTION_DROPDOWN);
        setCurTileInButton(FUNCTION_QUESTION_QUESTION_DROPDOWN_EMPTY_NEXT_TILE);
        setCurTileInButton(FUNCTION_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE);
        setCurTileInButton(FUNCTION_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE_OTHER);
        setCurTileInButton(FUNCTION_QUESTION_QUESTION_DROPDOWN_HORIZONTAL);
        setCurTileInButton(FUNCTION_QUESTION_QUESTION_DROPDOWN_CORNER);
    }

    private void setCurTileInButton(String buttonString) {
        ApoButtonImageDropdown buttonByFunction = (ApoButtonImageDropdown)(this.getMainPanel().getButtonByFunction(buttonString));
        buttonByFunction.setCurTiles(this.game.getCurrentTiles());
    }

    public void setPuzzle(boolean puzzle) {
        this.puzzle = puzzle;
        if (this.puzzle) {
            this.setPuzzleButtonVisibility();
            this.setUpPuzzle(false);
        }
    }

    private void setUpPuzzle(boolean addRandom) {
        int hints = 7;
        ArrayList<Integer> chosenList = new ArrayList<>();
        setUpPuzzle(new ArrayList<Integer>(), 0, hints, chosenList, addRandom);
    }

    private void setUpPuzzle(ArrayList<Integer> indexList, int count, int hints, ArrayList<Integer> chosenList, boolean addRandom) {
        ArrayList<byte[][]> possibleSolutions;
        ArrayList<byte[][]> possibleSolutionsPossibilities;
        ArrayList<byte[][]> possibleSolutionsReal;
        if (indexList.isEmpty()) {
            possibleSolutions = this.game.getPossibleSolutions();
            possibleSolutionsPossibilities = this.game.getPossibleSolutionsPossibilities();
            possibleSolutionsReal = this.game.getPossibleSolutionsReal();
        } else {
            possibleSolutions = new ArrayList<>();
            possibleSolutionsPossibilities = new ArrayList<>();
            possibleSolutionsReal = new ArrayList<>();

            for (int integer : indexList) {
                possibleSolutions.add(this.game.getPossibleSolutions().get(integer));
                possibleSolutionsPossibilities.add(this.game.getPossibleSolutionsPossibilities().get(integer));
                possibleSolutionsReal.add(this.game.getPossibleSolutionsReal().get(integer));
            }
        }

        int maxOptimum = 3;
        int randomQuestion = -1;
        if ((this.getGame().getDifficulty() != Difficulty.HARD && count < hints) || (addRandom)) {
            do {
                randomQuestion = (int) (Math.random() * 8);
            } while (chosenList.contains(randomQuestion));
            chosenList.add(randomQuestion);
        }

        int smallest = possibleSolutions.size();
        if (smallest == 1 && randomQuestion == -1) {
            this.game.setMaxReset(this.game.getQuestions().size());
            return;
        }
        Question pickedQuestion = null;
        ArrayList<Integer> filterResult = null;
        if (randomQuestion == 0 || randomQuestion == -1) {
            if (randomQuestion == 0 && count > maxOptimum) {
                int counterRepeat = 0;
                ArrayList<Integer> filter;
                Question question;
                do {
                    int choose = (int) (Math.random() * 10);

                    if (choose % 2 == 0) {
                        question = new AmountTiles(choose / 2, -1, this.game.getSolution());
                    } else {
                        question = new AmountTiles(-1, (choose - 1) / 2, this.game.getSolution());
                    }
                    filter = question.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);
                    counterRepeat += 1;
                } while ((filter.size() == smallest && smallest != 1) && counterRepeat < MAX_REPEAT);
                smallest = filter.size();
                pickedQuestion = question;
                filterResult = new ArrayList<>(filter);
            } else {
                for (int i = 0; i < 5; i++) {
                    Question question = new AmountTiles(i, -1, this.game.getSolution());
                    ArrayList<Integer> filter = question.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);

                    if ((filter.size() < smallest && !filter.isEmpty()) || (filter.size() == smallest && filterResult == null)) {
                        smallest = filter.size();
                        pickedQuestion = question;
                        filterResult = new ArrayList<>(filter);
                    }

                    Question questionR = new AmountTiles(-1, 1, this.game.getSolution());
                    ArrayList<Integer> filter1 = questionR.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);

                    if (filter1.size() < smallest && !filter1.isEmpty()) {
                        smallest = filter1.size();
                        pickedQuestion = questionR;
                        filterResult = new ArrayList<>(filter1);
                    }
                }
            }
        }

        if (randomQuestion == 1 || randomQuestion == -1) {
            if (randomQuestion == 1 && count > maxOptimum) {
                int counterRepeat = 0;
                ArrayList<Integer> filter;
                Question question;
                do {
                    int choose = (int) (Math.random() * 10);
                    if (choose % 2 == 0) {
                        question = new Empty(choose / 2, -1, this.game.getRealSolution());
                    } else {
                        question = new Empty(-1, (choose - 1) / 2, this.game.getRealSolution());
                    }
                    filter = question.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);
                    counterRepeat += 1;
                } while ((filter.size() == smallest && smallest != 1) && counterRepeat < MAX_REPEAT);
                smallest = filter.size();
                pickedQuestion = question;
                filterResult = new ArrayList<>(filter);
            } else {
                for (int i = 0; i < 5; i++) {
                    Question question = new Empty(i, -1, this.game.getRealSolution());
                    ArrayList<Integer> filter = question.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);

                    if ((filter.size() < smallest && !filter.isEmpty()) || (filter.size() == smallest && filterResult == null)) {
                        smallest = filter.size();
                        pickedQuestion = question;
                        filterResult = new ArrayList<>(filter);
                    }

                    Question questionR = new Empty(-1, 1, this.game.getRealSolution());
                    ArrayList<Integer> filter1 = questionR.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);

                    if (filter1.size() < smallest && !filter1.isEmpty()) {
                        smallest = filter1.size();
                        pickedQuestion = questionR;
                        filterResult = new ArrayList<>(filter1);
                    }
                }
            }
        }

        if (randomQuestion == 2 || randomQuestion == -1) {
            if (randomQuestion == 2 && count > maxOptimum) {
                int counterRepeat = 0;
                ArrayList<Integer> filter;
                Question question;
                do {
                    int choose = (int) (Math.random() * 10);
                    if (choose % 2 == 0) {
                        question = new SandAndGrassCheck(choose / 2, -1, this.game.getRealSolution());
                    } else {
                        question = new SandAndGrassCheck(-1, (choose - 1) / 2, this.game.getRealSolution());
                    }
                    filter = question.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);
                    counterRepeat += 1;
                } while ((filter.size() == smallest && smallest != 1) && counterRepeat < MAX_REPEAT);
                smallest = filter.size();
                pickedQuestion = question;
                filterResult = new ArrayList<>(filter);
            } else {
                for (int i = 0; i < 5; i++) {
                    Question question = new SandAndGrassCheck(i, -1, this.game.getRealSolution());
                    ArrayList<Integer> filter = question.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);

                    if ((filter.size() < smallest && !filter.isEmpty()) || (filter.size() == smallest && filterResult == null)) {
                        smallest = filter.size();
                        pickedQuestion = question;
                        filterResult = new ArrayList<>(filter);
                    }

                    Question questionR = new SandAndGrassCheck(-1, i, this.game.getRealSolution());
                    ArrayList<Integer> filter1 = questionR.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);

                    if (filter1.size() < smallest && !filter1.isEmpty()) {
                        smallest = filter1.size();
                        pickedQuestion = questionR;
                        filterResult = new ArrayList<>(filter1);
                    }
                }
            }
        }

        if (randomQuestion == 3 || randomQuestion == -1) {
            if (randomQuestion == 3 && count > maxOptimum) {
                int counterRepeat = 0;
                ArrayList<Integer> filter;
                Question question;
                do {
                    int t = (int) (Math.random() * this.game.getCurrentTiles().size());
                    int i = (int) (Math.random() * 5);
                    if (Math.random() * 100 > 50) {
                        question = new OneTileCheck(i, -1, this.game.getSolution(), this.game.getRealSolution(), this.game.getCurrentTiles().get(t).getTile().getTileNumber(), this.game.getCurrentTiles().get(t).getTile().getPossibilities().get(0));
                    } else {
                        question = new OneTileCheck(-1, i, this.game.getSolution(), this.game.getRealSolution(), this.game.getCurrentTiles().get(t).getTile().getTileNumber(), this.game.getCurrentTiles().get(t).getTile().getPossibilities().get(0));
                    }
                    filter = question.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);
                    counterRepeat += 1;
                } while ((filter.size() == smallest && smallest != 1) && counterRepeat < MAX_REPEAT);
                smallest = filter.size();
                pickedQuestion = question;
                filterResult = new ArrayList<>(filter);
            } else {
                for (int t = 0; t < this.game.getCurrentTiles().size(); t++) {
                    for (int i = 0; i < 5; i++) {
                        Question question = new OneTileCheck(i, -1, this.game.getSolution(), this.game.getRealSolution(), this.game.getCurrentTiles().get(t).getTile().getTileNumber(), this.game.getCurrentTiles().get(t).getTile().getPossibilities().get(0));
                        ArrayList<Integer> filter = question.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);

                        if ((filter.size() < smallest && !filter.isEmpty()) || (filter.size() == smallest && filterResult == null)) {
                            smallest = filter.size();
                            pickedQuestion = question;
                            filterResult = new ArrayList<>(filter);
                        }

                        Question questionR = new OneTileCheck(-1, i, this.game.getSolution(), this.game.getRealSolution(), this.game.getCurrentTiles().get(t).getTile().getTileNumber(), this.game.getCurrentTiles().get(t).getTile().getPossibilities().get(0));
                        ArrayList<Integer> filter1 = questionR.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);

                        if (filter1.size() < smallest && !filter1.isEmpty()) {
                            smallest = filter1.size();
                            pickedQuestion = questionR;
                            filterResult = new ArrayList<>(filter1);
                        }
                    }
                }
            }
        }

        if (randomQuestion == 4 || randomQuestion == -1) {
            if (randomQuestion == 4 && count > maxOptimum) {
                int counterRepeat = 0;
                ArrayList<Integer> filter;
                Question question;
                do {
                    int t = (int) (Math.random() * this.game.getCurrentTiles().size());
                    int i = (int) (Math.random() * 3);
                    int column = -1;
                    int row = -1;
                    if (i == 0 || i > 1) {
                        column = 0;
                    }
                    if (i >= 1) {
                        row = 0;
                    }
                    question = new OneTileSideCheck(column, row, this.game.getSolution(), this.game.getCurrentTiles().get(t).getTile().getTileNumber(), this.game.getCurrentTiles().get(t).getTile().getPossibilities().get(0));
                    filter = question.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);
                    counterRepeat += 1;
                } while ((filter.size() == smallest && smallest != 1) && counterRepeat < MAX_REPEAT);
                smallest = filter.size();
                pickedQuestion = question;
                filterResult = new ArrayList<>(filter);
            } else {
                for (int t = 0; t < this.game.getCurrentTiles().size(); t++) {
                    for (int i = 0; i < 3; i++) {
                        int column = -1;
                        int row = -1;
                        if (i == 0 || i > 1) {
                            column = 0;
                        }
                        if (i >= 1) {
                            row = 0;
                        }
                        Question question = new OneTileSideCheck(column, row, this.game.getSolution(), this.game.getCurrentTiles().get(t).getTile().getTileNumber(), this.game.getCurrentTiles().get(t).getTile().getPossibilities().get(0));
                        ArrayList<Integer> filter = question.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);

                        if ((filter.size() < smallest && !filter.isEmpty()) || (filter.size() == smallest && filterResult == null)) {
                            smallest = filter.size();
                            pickedQuestion = question;
                            filterResult = new ArrayList<>(filter);
                        }
                    }
                }
            }
        }

        if (randomQuestion == 5 || randomQuestion == -1) {
            if (randomQuestion == 5 && count > maxOptimum) {
                int counterRepeat = 0;
                ArrayList<Integer> filter;
                Question question;
                do {
                    int tile = (int)(Math.random() * this.game.getCurrentTiles().size());
                    question = new EmptyNextTile(this.game.getSolution(), this.game.getRealSolution(), this.game.getCurrentTiles().get(tile).getTile().getTileNumber(), this.game.getCurrentTiles().get(tile).getTile().getPossibilities().get(0));
                    filter = question.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);
                    counterRepeat += 1;
                } while ((filter.size() == smallest && smallest != 1) && counterRepeat < MAX_REPEAT);
                smallest = filter.size();
                pickedQuestion = question;
                filterResult = new ArrayList<>(filter);
            } else {
                for (int i = 0; i < this.game.getCurrentTiles().size(); i++) {
                    Question question = new EmptyNextTile(this.game.getSolution(), this.game.getRealSolution(), this.game.getCurrentTiles().get(i).getTile().getTileNumber(), this.game.getCurrentTiles().get(i).getTile().getPossibilities().get(0));
                    ArrayList<Integer> filter = question.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);

                    if ((filter.size() < smallest && !filter.isEmpty()) || (filter.size() == smallest && filterResult == null)) {
                        smallest = filter.size();
                        pickedQuestion = question;
                        filterResult = new ArrayList<>(filter);
                    }
                }
            }
        }

        if (randomQuestion == 6 || randomQuestion == -1) {
            if (randomQuestion == 6 && count > maxOptimum) {
                int counterRepeat = 0;
                ArrayList<Integer> filter;
                Question question;
                do {
                    int tile = (int)(Math.random() * this.game.getCurrentTiles().size());
                    int otherTile;
                    do {
                        otherTile = (int)(Math.random() * this.game.getCurrentTiles().size());
                    } while (otherTile == tile);
                    question = new TileNextTile(this.game.getSolution(), this.game.getCurrentTiles().get(tile).getTile().getTileNumber(), this.game.getCurrentTiles().get(tile).getTile().getPossibilities().get(0), this.game.getCurrentTiles().get(otherTile).getTile().getTileNumber(), this.game.getCurrentTiles().get(otherTile).getTile().getPossibilities().get(0));
                    filter = question.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);
                    counterRepeat += 1;
                } while ((filter.size() == smallest && smallest != 1) && counterRepeat < MAX_REPEAT);
                smallest = filter.size();
                pickedQuestion = question;
                filterResult = new ArrayList<>(filter);
            } else {
                for (int i = 0; i < this.game.getCurrentTiles().size(); i++) {
                    for (int j = i + 1; j < this.game.getCurrentTiles().size(); j++) {
                        Question question = new TileNextTile(this.game.getSolution(), this.game.getCurrentTiles().get(i).getTile().getTileNumber(), this.game.getCurrentTiles().get(i).getTile().getPossibilities().get(0), this.game.getCurrentTiles().get(j).getTile().getTileNumber(), this.game.getCurrentTiles().get(j).getTile().getPossibilities().get(0));
                        ArrayList<Integer> filter = question.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);

                        if ((filter.size() < smallest && !filter.isEmpty()) || (filter.size() == smallest && filterResult == null)) {
                            smallest = filter.size();
                            pickedQuestion = question;
                            filterResult = new ArrayList<>(filter);
                        }
                    }
                }
            }
        }

        if (randomQuestion == 7 || randomQuestion == -1) {
            if (randomQuestion == 7 && count > maxOptimum) {
                int counterRepeat = 0;
                ArrayList<Integer> filter;
                Question question;
                do {
                    int t = (int) (Math.random() * this.game.getCurrentTiles().size());
                    question = new OneTileCornerCheck(this.game.getSolution(), this.game.getCurrentTiles().get(t).getTile().getTileNumber(), this.game.getCurrentTiles().get(t).getTile().getPossibilities().get(0));
                    filter = question.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);
                    counterRepeat += 1;
                } while ((filter.size() == smallest && smallest != 1) && counterRepeat < MAX_REPEAT);
                smallest = filter.size();
                pickedQuestion = question;
                filterResult = new ArrayList<>(filter);
            } else {
                for (int t = 0; t < this.game.getCurrentTiles().size(); t++) {
                    Question question = new OneTileCornerCheck(this.game.getSolution(), this.game.getCurrentTiles().get(t).getTile().getTileNumber(), this.game.getCurrentTiles().get(t).getTile().getPossibilities().get(0));
                    ArrayList<Integer> filter = question.filter(possibleSolutionsReal, possibleSolutionsPossibilities, possibleSolutions);

                    if ((filter.size() < smallest && !filter.isEmpty()) || (filter.size() == smallest && filterResult == null)) {
                        smallest = filter.size();
                        pickedQuestion = question;
                        filterResult = new ArrayList<>(filter);
                    }
                }
            }
        }

        //System.out.println("Anzahl Möglichkeiten left: " + smallest+" startedWith: "+possibleSolutions.size()+" hints: "+hints+" count: "+count);
        if (addRandom) {
            this.addQuestion(pickedQuestion);
        } else if (this.getGame().getDifficulty() != Difficulty.HARD && count < hints) {
            this.addQuestion(pickedQuestion);
            setUpPuzzleNext(indexList, count, hints, chosenList, filterResult);
        } else {
            if (smallest != indexList.size()) {
                this.addQuestion(pickedQuestion);
            }

            if (smallest > 1 && filterResult != null && smallest != indexList.size()) {
                setUpPuzzleNext(indexList, count, hints, chosenList, filterResult);
            } else {
                this.game.setMaxReset(this.game.getQuestions().size());
                //System.out.println("Anzahl Möglichkeiten left: " + smallest);
            }
        }
    }

    private void setUpPuzzleNext(ArrayList<Integer> indexList, int count, int hints, ArrayList<Integer> chosenList, ArrayList<Integer> filterResult) {
        if (!indexList.isEmpty()) {
            ArrayList<Integer> result = new ArrayList<>();
            for (int resultInteger : filterResult) {
                result.add(indexList.get(resultInteger));
            }
            setUpPuzzle(result, count + 1, hints, chosenList, false);
        } else {
            setUpPuzzle(filterResult, count + 1, hints, chosenList, false);
        }
    }

    private void setAllNextQuestions() {
        int oldValue = this.showTabIndex;
        QuestionEnum[] questionEnumForQuestionTypeOriginal = QuestionEnum.getQuestionEnumForQuestionType(0);
        QuestionEnum[] questionEnumForQuestionTypeOther = QuestionEnum.getQuestionEnumForQuestionType(1);
        this.showTabIndex = ORIGINAL_QUESTIONS;
        for (int i = 0; i < questionEnumForQuestionTypeOriginal.length; i++) {
            this.nextQuestions[0][i] = getNextQuestion(i);
        }
        this.showTabIndex = OTHER_QUESTIONS;
        for (int i = 0; i < questionEnumForQuestionTypeOther.length; i++) {
            this.nextQuestions[1][i] = getNextQuestion(i);
        }
        this.showTabIndex = oldValue;
    }

    private void setNextCosts() {
        QuestionEnum[] questionEnumForQuestionType = QuestionEnum.getQuestionEnumForQuestionType(showTabIndex);
        for (int i = 0; i < questionEnumForQuestionType.length; i++) {
            String buttonFunction = FUNCTION_QUESTION_QUESTION_ASK + questionEnumForQuestionType[i].name();
            if (getMainPanel().getButtonByFunction(buttonFunction).isSelect()) {
                setNextCosts(this.nextQuestions[this.showTabIndex][i]);
                return;
            }
        }
    }

    private void setNextCosts(Question question) {
        int nextAsk = question.withAskCosts() ? getNextCostsAsk() : 0;
        this.nextCosts = nextAsk + question.getCosts() + question.getAddCostsBecauseLast();
    }

    private int getNextCostsAsk() {
        return this.curAskOrder != this.curWantedQuestion ? 2 : 1;
    }

    public void setNewLevel(Difficulty difficulty, boolean puzzle) {
        this.puzzle = puzzle;

        this.resetTiles();
        this.game.setNewLevel(difficulty, puzzle);

        this.resetCurAsk();

        this.won = false;
        this.checkQuestion = false;

        setCurTilesForButtons();

        setAllNextQuestions();
        setNextCosts();

        this.setPuzzle(this.puzzle);

        setQuestionsOnPosition();
    }

    private void setQuestionsOnPosition() {
        int i = 0;
        for (Question nextQuestion : this.game.getQuestions()) {
            nextQuestion.setX((int) (i % 3f) * 200);
            nextQuestion.setY((int) (i / 3f) * 150);
            nextQuestion.setStartX(nextQuestion.getX());
            nextQuestion.setStartY(nextQuestion.getY());
            i += 1;
        }
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

        this.resetTiles();
        this.game.choseNewSolution();

        setCurTilesForButtons();

        setNeededButtonsVisible();

        this.setPuzzle(this.puzzle);

        this.setQuestionsOnPosition();

        this.mouseMoved(0, 0);
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
//            if (this.game.getDifficulty() == Difficulty.EASY) {
//
//            }

//            this.game.makeQuestionErrorWhenWrong();
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

    private void changeShowTabIndex(int index) {
        this.showTabIndex = index;
        for (QuestionEnum questionEnum : QuestionEnum.values()) {
            getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_ASK + questionEnum.name()).setSelect(false);
            getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_ASK + questionEnum.name()).setVisible(false);
        }
        if (index == ORIGINAL_QUESTIONS) {
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTIONS_ORIGINAL).setSelect(true);
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTIONS_OTHER).setSelect(false);
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN).setVisible(true);
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_EMPTY_NEXT_TILE).setVisible(false);
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE).setVisible(false);
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE_OTHER).setVisible(false);
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_HORIZONTAL).setVisible(false);
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_CORNER).setVisible(false);
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_STRING_SIDE).setVisible(false);
        } else {
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTIONS_ORIGINAL).setSelect(false);
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTIONS_OTHER).setSelect(true);
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN).setVisible(false);
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_EMPTY_NEXT_TILE).setVisible(true);
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE).setVisible(true);
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE_OTHER).setVisible(true);
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_HORIZONTAL).setVisible(true);
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_CORNER).setVisible(true);
            this.getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_STRING_SIDE).setVisible(true);
        }
        QuestionEnum[] questionEnumForQuestionType = QuestionEnum.getQuestionEnumForQuestionType(index);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_ASK + questionEnumForQuestionType[0].name()).setSelect(true);

        for (QuestionEnum questionEnum : questionEnumForQuestionType) {
            getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_ASK + questionEnum.name()).setVisible(true);
        }
    }

    private void setShowQuestion(boolean showQuestion) {
        this.showQuestion = showQuestion;

        this.setAllButtonVisible(false);

        if (this.showQuestion) {
            getMainPanel().getButtonByFunction(FUNCTION_HINT_UP).setVisible(false);
            getMainPanel().getButtonByFunction(FUNCTION_HINT_DOWN).setVisible(false);
            this.changeShowTabIndex(showTabIndex);
        }

        getMainPanel().getButtonByFunction(FUNCTION_ARCHEOLOGIC_BACK).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_RESTART).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_NEW_LEVEL).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_TEST).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_CHECK).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION).setVisible(!showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_REAL).setVisible(showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_CLOSE).setVisible(showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTIONS_ORIGINAL).setVisible(showQuestion);
        getMainPanel().getButtonByFunction(FUNCTION_QUESTIONS_OTHER).setVisible(showQuestion);
        for (String s : askOrder) {
            getMainPanel().getButtonByFunction(FUNCTION_QUESTION_ROW + s).setVisible(showQuestion);
        }
        setPuzzleButtonVisibility();
//        for (int i = 0; i < QuestionEnum.values().length; i++) {
//            if (QuestionEnum.values()[i].getQuestionType() == showTabIndex) {
//                getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.values()[i].name()).setVisible(showQuestion);
//            } else {
//                getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.values()[i].name()).setVisible(false);
//            }
//        }
    }

    private void setPuzzleButtonVisibility() {
        if (this.puzzle) {
            getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION).setVisible(false);
            if (this.game.getDifficulty() != Difficulty.HARD) {
//                getMainPanel().getButtonByFunction(FUNCTION_QUESTIONS_NEXT_HINT).setVisible(true);
                getMainPanel().getButtonByFunction(FUNCTION_QUESTIONS_HELP).setVisible(true);
            }
        }
    }

    @Override
    public void mouseButtonFunction(String function) {
        this.isPressed = false;
        if (this.showQuestion) {
            if (function.equals(ArcheOLogicPanel.FUNCTION_QUESTIONS_ORIGINAL)) {
                this.changeShowTabIndex(ORIGINAL_QUESTIONS);
                this.setNextCosts();
                return;
            }
            if (function.equals(ArcheOLogicPanel.FUNCTION_QUESTIONS_OTHER)) {
                this.changeShowTabIndex(OTHER_QUESTIONS);
                this.setNextCosts();
                return;
            }
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
            if (function.equals(ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_DROPDOWN_EMPTY_NEXT_TILE)) {
                setAllNextQuestions();
                mouseButtonFunction(FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.EMPTY_NEXT_TILE.name());
                return;
            }
            if (function.equals(ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE)) {
                setAllNextQuestions();
                mouseButtonFunction(FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.TILE_NEXT_TILE.name());
                return;
            }
            if (function.equals(ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE_OTHER)) {
                setAllNextQuestions();
                mouseButtonFunction(FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.TILE_NEXT_TILE.name());
                return;
            }
            if (function.equals(ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_DROPDOWN_HORIZONTAL) || function.equals(ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_DROPDOWN_STRING_SIDE)) {
                setAllNextQuestions();
                mouseButtonFunction(FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.HORIZONTAL_VERTICAL_BORDER_CHECK.name());
                return;
            }
            if (function.equals(ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_DROPDOWN_CORNER)) {
                setAllNextQuestions();
                mouseButtonFunction(FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.CORNER_BORDER_CHECK.name());
                return;
            }
            if (this.showTabIndex != OTHER_QUESTIONS) {
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
            }
            QuestionEnum[] questionEnumForQuestionType = QuestionEnum.getQuestionEnumForQuestionType(this.showTabIndex);
            for (int i = 0; i < questionEnumForQuestionType.length; i++) {
                String buttonFunction = FUNCTION_QUESTION_QUESTION_ASK + questionEnumForQuestionType[i].name();
                if (function.equals(buttonFunction)) {
                    for (int j = 0; j < QuestionEnum.values().length; j++) {
                        String buttonDeselect = FUNCTION_QUESTION_QUESTION_ASK + QuestionEnum.values()[j].name();
                        getMainPanel().getButtonByFunction(buttonDeselect).setSelect(false);
                    }
                    getMainPanel().getButtonByFunction(buttonFunction).setSelect(true);
                    setNextCosts(this.nextQuestions[this.showTabIndex][i]);
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
                            setQuestionRedWhenError();
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
                case ArcheOLogicPanel.FUNCTION_QUESTIONS_NEXT_HINT:
                    this.setUpPuzzle(true);
                    break;
                case ArcheOLogicPanel.FUNCTION_QUESTIONS_HELP:
                    this.game.makeQuestionErrorWhenWrong();

                    Question nextQuestion = new HelpQuestion();
                    nextQuestion.setCompleteCosts(nextQuestion.getCosts());
                    this.game.setCosts(this.getGame().getCosts() + nextQuestion.getCosts());
                    this.game.getQuestions().add(nextQuestion);
                    break;
                case ArcheOLogicPanel.FUNCTION_QUESTION_CHECK:
                    if (this.game.isEveryTileIn()) {
                        setCheckQuestion(true, Localization.getInstance().getCommon().get("demand_solve").split(";"), DemandQuestion.SOLVING);
                    } else {
                        setCheckQuestion(true, Localization.getInstance().getCommon().get("demand_error_check").split(";"), DemandQuestion.ERROR);
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
                    //this.setButtonsVisible(false);
                    this.setShowQuestion(false);
                    this.game.addCurStartQuestion(1000);
                    break;
            }
        }
    }

    private void setQuestionRedWhenError() {
        this.game.makeQuestionErrorWhenWrong();
    }

    private void resetTiles() {
        this.won = false;
        this.checkQuestion = false;
        this.game.resetTiles();
        if (!this.puzzle) {
            this.game.setCosts(0);
        }
        this.resetCurAsk();
        this.setNeededButtonsVisible();
        for (Question[] nextQuestion : nextQuestions) {
            for (int j = 0; j < nextQuestion.length; j++) {
                if (nextQuestion[j] != null) {
                    nextQuestion[j].setAddCostsBecauseLast(0);
                }
            }
        }
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

            this.wonEnum = Won.getRankForPoints(this.game.getCosts());
        }
    }

    private Question getNextQuestion() {
        return getNextQuestion(-1);
    }

    private Question getNextQuestion(int selectedQuestion) {
        QuestionEnum[] values = QuestionEnum.getQuestionEnumForQuestionType(this.showTabIndex);
        for (int i = 0; i < values.length; i++) {
            String buttonFunction = FUNCTION_QUESTION_QUESTION_ASK + values[i].name();
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
                if (values[i] == QuestionEnum.EMPTY) {
                    nextQuestion = new Empty(column, row, this.game.getRealSolution());
                } else if (values[i] == QuestionEnum.EMPTY_NEXT_TILE) {
                    ApoButtonImageDropdown buttonByFunction = (ApoButtonImageDropdown)getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_EMPTY_NEXT_TILE);
                    nextQuestion = new EmptyNextTile(this.game.getSolution(), this.game.getRealSolution(), buttonByFunction.getTileNumber(), buttonByFunction.getTileArray());
                } else if (values[i] == QuestionEnum.TILE_NEXT_TILE) {
                    ApoButtonImageDropdown buttonByFunction = (ApoButtonImageDropdown)getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE);
                    ApoButtonImageDropdown buttonOtherByFunction = (ApoButtonImageDropdown)getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE_OTHER);
                    nextQuestion = new TileNextTile(this.game.getSolution(), buttonByFunction.getTileNumber(), buttonByFunction.getTileArray(), buttonOtherByFunction.getTileNumber(), buttonOtherByFunction.getTileArray());
                } else if (values[i] == QuestionEnum.AMOUNT_TILES) {
                    nextQuestion = new AmountTiles(column, row, this.game.getSolution());
                } else if (values[i] == QuestionEnum.SAND_AND_GRASS_CHECK) {
                    nextQuestion = new SandAndGrassCheck(column, row, this.game.getRealSolution());
                } else if (values[i] == QuestionEnum.ONE_TILE_CHECK) {
                    ApoButtonImageDropdown buttonByFunction = (ApoButtonImageDropdown)getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN);
                    nextQuestion = new OneTileCheck(column, row, this.game.getSolution(), this.game.getRealSolution(), buttonByFunction.getTileNumber(), buttonByFunction.getTileArray());
                } else if (values[i] == QuestionEnum.HORIZONTAL_VERTICAL_BORDER_CHECK) {
                    ApoButtonImageDropdown buttonByFunction = (ApoButtonImageDropdown)getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_HORIZONTAL);

                    ApoButtonImageDropdown buttonByFunctionString = (ApoButtonImageDropdown)getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_STRING_SIDE);

                    if (buttonByFunctionString.getTileNumber() == 0) {
                        nextQuestion = new OneTileSideCheck(-1, 1, this.game.getSolution(), buttonByFunction.getTileNumber(), buttonByFunction.getTileArray());
                    } else if (buttonByFunctionString.getTileNumber() == 1) {
                        nextQuestion = new OneTileSideCheck(1, -1, this.game.getSolution(), buttonByFunction.getTileNumber(), buttonByFunction.getTileArray());
                    } else {
                        nextQuestion = new OneTileSideCheck(1, 1, this.game.getSolution(), buttonByFunction.getTileNumber(), buttonByFunction.getTileArray());
                    }
                } else if (values[i] == QuestionEnum.CORNER_BORDER_CHECK) {
                    ApoButtonImageDropdown buttonByFunction = (ApoButtonImageDropdown)getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_DROPDOWN_CORNER);

                    nextQuestion = new OneTileCornerCheck(this.game.getSolution(), buttonByFunction.getTileNumber(), buttonByFunction.getTileArray());

                }

                if (nextQuestion != null) {
                    setAddCostForQuestionOnly(nextQuestion);
                }
                return nextQuestion;
            }
        }
        return null;
    }

    private void setAddCostForQuestionOnly(Question question) {
        QuestionEnum questionEnum = question.getQuestionEnum();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < this.nextQuestions[0].length; j++) {
                if (this.nextQuestions[i][j] != null && this.nextQuestions[i][j].getQuestionEnum().equals(questionEnum)) {
                    question.setAddCostsBecauseLast(this.nextQuestions[i][j].getAddCostsBecauseLast());
                    return;
                }
            }
        }
    }

    private void addQuestion() {
        addQuestion(getNextQuestion());
    }

    private void addQuestion(Question nextQuestion) {
        int completeCost = 0;

        if (nextQuestion != null) {

            completeCost = nextQuestion.getCosts() + nextQuestion.getAddCostsBecauseLast();
            this.getGame().setCosts(this.getGame().getCosts() + nextQuestion.getCosts() + nextQuestion.getAddCostsBecauseLast());
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
            setAddCostForQuestion(nextQuestion);
        }

        this.game.addCurStartQuestion(1);

        this.setShowQuestion(false);
    }

    private void setAddCostForQuestion(Question question) {
        QuestionEnum questionEnum = question.getQuestionEnum();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < this.nextQuestions[0].length; j++) {
                if (this.nextQuestions[i][j] != null && !this.nextQuestions[i][j].getQuestionEnum().equals(questionEnum)) {
                    this.nextQuestions[i][j].decreaseAddCost();
                }
                if (this.nextQuestions[i][j] != null && this.nextQuestions[i][j].getQuestionEnum().equals(questionEnum)) {
                    this.nextQuestions[i][j].addCostsBecauseLast();

                    question.setAddCostsBecauseLast(this.nextQuestions[i][j].getAddCostsBecauseLast());
                }
            }
        }
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

        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);

        getMainPanel().getRenderer().setColor(Constants.COLOR_GREY[0], Constants.COLOR_GREY[1], Constants.COLOR_GREY[2], 0.8f);
        for (GameTile tile : this.game.getCurrentTiles()) {
            tile.renderShadow(getMainPanel());
        }

        if (this.puzzle) {
            for (int i = this.game.getCurStartQuestion(); i < this.game.getCurStartQuestion() + GameEntity.MAX_SHOWN_QUESTION && i < this.game.getQuestions().size(); i++) {
                getMainPanel().getRenderer().setColor(Constants.COLOR_WHITE[0], Constants.COLOR_WHITE[1], Constants.COLOR_WHITE[2], 0.2f);
                this.game.getQuestions().get(i).renderFilled(getMainPanel(), Constants.GAME_WIDTH - 620, 210);
            }
        }

        getMainPanel().getRenderer().end();
        Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);

        if (this.puzzle) {
            getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Line);

            for (int i = this.game.getCurStartQuestion(); i < this.game.getCurStartQuestion() + GameEntity.MAX_SHOWN_QUESTION && i < this.game.getQuestions().size(); i++) {
                this.game.getQuestions().get(i).renderLine(getMainPanel(), Constants.GAME_WIDTH - 620, 210);
            }

            getMainPanel().getRenderer().end();
        }

        getMainPanel().spriteBatch.begin();

        getMainPanel().drawString(Constants.PROPERTY_NAME, (Constants.GAME_WIDTH - 300), 100, Constants.COLOR_BLACK, AssetLoader.font30, DrawString.MIDDLE, true, false);

        getMainPanel().spriteBatch.draw(AssetLoader.coinTextureRegion, (Constants.GAME_WIDTH - 150), 80, 60, 60);
        getMainPanel().drawString(String.valueOf(this.game.getCosts()), (Constants.GAME_WIDTH - 120), 113, Constants.COLOR_BLACK, AssetLoader.font30, DrawString.MIDDLE, true, false);

        if (!this.puzzle) {
            getMainPanel().drawString(Localization.getInstance().getCommon().get("question_order"), Constants.GAME_WIDTH - 600, 170, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.BEGIN, true, false);

            for (int i = 0; i < askOrder.length; i++) {
                float[] color = Constants.COLOR_BLACK;
                if (i == this.curAskOrder) {
                    color = Constants.COLOR_RED_DARK;
                }
                getMainPanel().drawString(askOrder[i], Constants.GAME_WIDTH - 450 + i * 45, 170, color, AssetLoader.font30, DrawString.MIDDLE, true, false);
            }
            getMainPanel().drawString(Localization.getInstance().getCommon().get("question_hint"), Constants.GAME_WIDTH - 600, 220, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.BEGIN, true, false);
        } else {
            getMainPanel().drawString(Localization.getInstance().getCommon().get("puzzle_mode") + " - " + this.game.getDifficulty().getText(), Constants.GAME_WIDTH - 600, 170, Constants.COLOR_BLACK, AssetLoader.font30, DrawString.BEGIN, true, false);
        }


        for (int i = this.game.getCurStartQuestion(); i < this.game.getCurStartQuestion() + GameEntity.MAX_SHOWN_QUESTION && i < this.game.getQuestions().size(); i++) {
            String text = this.game.getQuestions().get(i).getText();
            int costs = this.game.getQuestions().get(i).getCompleteCosts();

            if (this.puzzle) {
                //renderTextAndTileQuestion(getMainPanel(), text, (int)(Constants.GAME_WIDTH - 600 + i%3 * this.game.getQuestions().get(i).getWidth()), 340 + (int)(i / 3f) * (int)this.game.getQuestions().get(i).getHeight(), this.game.getQuestions().get(i).hasErrors());
            } else {
                renderTextAndTileQuestion(getMainPanel(), text, Constants.GAME_WIDTH - 575, 250 + (i - this.game.getCurStartQuestion()) * 25, this.game.getQuestions().get(i).hasErrors());

                if (costs > 0) {
                    getMainPanel().spriteBatch.draw(AssetLoader.coinTextureRegion, (Constants.GAME_WIDTH - 109), 238 + (i - this.game.getCurStartQuestion()) * 25, 20, 20);
                    getMainPanel().drawString(String.valueOf(costs), Constants.GAME_WIDTH - 100, 250 + (i - this.game.getCurStartQuestion()) * 25, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, true, false);
                }
            }
        }

        for (GameTile tile : this.game.getCurrentTiles()) {
            tile.render(getMainPanel());
        }

        if (this.puzzle) {
            for (int i = this.game.getCurStartQuestion(); i < this.game.getCurStartQuestion() + GameEntity.MAX_SHOWN_QUESTION && i < this.game.getQuestions().size(); i++) {
                this.game.getQuestions().get(i).renderSprite(getMainPanel(), Constants.GAME_WIDTH - 620, 210);
            }
        }

        getMainPanel().spriteBatch.end();

        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);

        getMainPanel().getRenderer().setColor(Constants.COLOR_BLACK[0], Constants.COLOR_BLACK[1], Constants.COLOR_BLACK[2], 1f);
        for (int i = this.game.getCurStartQuestion(); i < this.game.getCurStartQuestion() + GameEntity.MAX_SHOWN_QUESTION && i < this.game.getQuestions().size(); i++) {
            //this.game.getQuestions().get(i).renderFilled(getMainPanel(), Constants.GAME_WIDTH - 600, 250 + (i-this.game.getCurStartQuestion()) * 25);
            if (this.puzzle) {

            } else {
                this.game.getQuestions().get(i).draw(getMainPanel(), Constants.GAME_WIDTH - 600, 250 + (i - this.game.getCurStartQuestion()) * 25, 6);
            }
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

            getMainPanel().drawString(Localization.getInstance().getCommon().get("question_title"), Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() / 2f - 15, 35, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, true, false);

            getMainPanel().drawString(Localization.getInstance().getCommon().get("question_second"), Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth()/2f - 15, ArcheOLogicPanel.START_QUESTION_Y - 10, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, true, false);
            getMainPanel().drawString(Localization.getInstance().getCommon().get("question_third"), Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth()/2f - 15, Constants.GAME_HEIGHT - 105, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, true, false);

            QuestionEnum[] questionEnumForQuestionType = QuestionEnum.getQuestionEnumForQuestionType(showTabIndex);
            for (int i = 0; i < questionEnumForQuestionType.length; i++) {
                getMainPanel().spriteBatch.draw(AssetLoader.coinTextureRegion, Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() - 10 + 85, ArcheOLogicPanel.START_QUESTION_Y + 25 + i * (50 + 10) - 17, 35, 35);
                getMainPanel().drawString(String.valueOf(this.nextQuestions[this.showTabIndex][i].getCostsWithAddCosts()), Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() - 10 + 101, ArcheOLogicPanel.START_QUESTION_Y + 29 + i * (50 + 10), Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, true, false);

                BitmapFont font15 = AssetLoader.font15;
                String text = Localization.getInstance().getCommon().get(questionEnumForQuestionType[i].getText())+" ";

                float startX = renderTextAndTileQuestion(getMainPanel(), text, Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() - 10 + 130, ArcheOLogicPanel.START_QUESTION_Y + 27 + i * (50 + 10), false);

                //getMainPanel().drawString(text, Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() - 10 + 130, 277 + i * (50 + 10), Constants.COLOR_BLACK, font15, DrawString.BEGIN, true, false);
                if (this.showTabIndex != OTHER_QUESTIONS) {
                    getMainPanel().drawString(this.curAddQuestionString, startX, ArcheOLogicPanel.START_QUESTION_Y + 27 + i * (50 + 10), Constants.COLOR_BLACK, font15, DrawString.BEGIN, true, false);
                }
                ApoButton button = getMainPanel().getButtonByFunction(FUNCTION_QUESTION_QUESTION_ASK + questionEnumForQuestionType[i].name());
                if (button.isSelect() && this.showTabIndex != OTHER_QUESTIONS) {
                    showCoinNextCostAsk = true;
                }
            }

            if (showCoinNextCostAsk) {
                getMainPanel().drawString(Localization.getInstance().getCommon().get("question_first").split(";")[0], Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth()/2f - 10, 140, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, true, false);
                //getMainPanel().drawString(Localization.getInstance().getCommon().get("question_first").split(";")[1], Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth()/2f - 10, 145, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, true, false);

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

            if (this.demandQuestion == DemandQuestion.SOLVING) {
                getMainPanel().drawString(Localization.getInstance().getCommon().get("demand_question"), Constants.GAME_WIDTH / 2f, Constants.GAME_HEIGHT / 2f - height / 2f + 40, Constants.COLOR_BLACK, AssetLoader.font30, DrawString.MIDDLE, true, false);
            } else {
                getMainPanel().drawString(Localization.getInstance().getCommon().get("demand_question_error"), Constants.GAME_WIDTH / 2f, Constants.GAME_HEIGHT / 2f - height / 2f + 40, Constants.COLOR_BLACK, AssetLoader.font30, DrawString.MIDDLE, true, false);
            }

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

            String[] split = Localization.getInstance().getCommon().get(this.wonEnum.getText()).split(";");
            String s = split[0];
            getMainPanel().drawString(s, Constants.GAME_WIDTH/2f, Constants.GAME_HEIGHT/2f - height/2f + 45, Constants.COLOR_BLACK, AssetLoader.font25, DrawString.MIDDLE, true, false);

            s = Localization.getInstance().getCommon().get("won_text");
            s = s.replace("{x}", String.valueOf(this.game.getQuestions().size()));
            s = s.replace("{y}", String.valueOf(this.game.getCosts()));
            getMainPanel().drawString(s, Constants.GAME_WIDTH/2f, Constants.GAME_HEIGHT/2f - height/2f + 100, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, true, false);

            for (int i = 1; i < split.length; i++) {
                s = split[i];
                getMainPanel().drawString(s, Constants.GAME_WIDTH/2f, Constants.GAME_HEIGHT/2f - height/2f + 140 + 20 * (i - 1), Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, true, false);
            }

            getMainPanel().spriteBatch.end();
        }

        for (ApoButton button : this.getMainPanel().getButtons()) {
            if (!showCoinNextCostAsk && this.showQuestion && button.getFunction().startsWith(FUNCTION_QUESTION_ROW)) {
                continue;
            }
            button.render(this.getMainPanel());
        }

        for (ApoButton button : this.getMainPanel().getButtons()) {
            if (!showCoinNextCostAsk && this.showQuestion && this.showTabIndex == OTHER_QUESTIONS) {
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

    public static float renderTextAndTileQuestion(GameScreen screen, String text, int realStartX, int y, boolean error) {
        return renderTextAndTileQuestion(screen, text, realStartX, y, error, DrawString.BEGIN);
    }

    public static float renderTextAndTileQuestion(GameScreen screen, String text, int realStartX, int y, boolean error, DrawString drawString) {
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
            float[] color = Constants.COLOR_BLACK;
            if (error) {
                color = Constants.COLOR_RED;
            }
            screen.drawString(curText, startX, y, color, AssetLoader.font15, drawString, true, false);

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
                screen.spriteBatch.draw(AssetLoader.backgroundTextureRegion[value], startX - 25, y - 11, 20, 20);
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
