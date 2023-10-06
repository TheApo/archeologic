package com.apogames.game.archeologic;

import com.apogames.backend.GameProperties;
import com.apogames.backend.SequentiallyThinkingScreenModel;
import com.apogames.game.knuthAlgoX.AlgorithmX;
import com.apogames.game.knuthAlgoX.MyPuzzleADayBinary;
import com.apogames.game.tiles.ArcheOLogicTiles;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.Arrays;

public class ArcheOLogicPreferences extends GameProperties {

    public ArcheOLogicPreferences(SequentiallyThinkingScreenModel mainPanel) {
        super(mainPanel);
    }

    @Override
    public Preferences getPreferences() {
        return Gdx.app.getPreferences("ArcheOLogicGamePreferences");
    }

    public void writeLevel() {


        ArcheOLogicTiles logic = new ArcheOLogicTiles();
        MyPuzzleADayBinary myPuzzleADayBinary = new MyPuzzleADayBinary(logic.getAllTiles());
        byte[][] matrix = myPuzzleADayBinary.getMatrix(MyPuzzleADayBinary.GOAL);

        int xSize = 5;
        int ySize = 5;

        AlgorithmX algoX = new AlgorithmX();
        ArrayList<byte[][]> run = algoX.run(5, 5, 9, matrix);


        getPref().putInteger("size", run.size());
        getPref().putInteger("xSize", xSize);
        getPref().putInteger("ySize", ySize);

        int index = 0;
        for (byte[][] level : run) {
            for (int y = 0; y < level.length; y++) {
                for (int x = 0; x < level[0].length; x++) {
                    getPref().putInteger(index+"_"+x+"_"+y, level[y][x]);
                }
            }
            index += 1;
        }

        index = 0;
        for (byte[][] level : algoX.allValueSolutions) {
            for (int y = 0; y < level.length; y++) {
                for (int x = 0; x < level[0].length; x++) {
                    getPref().putInteger(index+"_"+x+"_"+y+"_", level[y][x]);
                }
            }
            index += 1;
        }

        getPref().flush();
    }

    public void readLevel() {
        ArrayList<byte[][]> solutions = new ArrayList<>();
        ArrayList<byte[][]> solutionsPossibilities = new ArrayList<>();
        int size = getPref().getInteger("size", 0);

        int xSize = getPref().getInteger("xSize", 5);
        int ySize = getPref().getInteger("ySize", 5);

        for (int i = 0; i < size; i++) {
            byte[][] level = new byte[ySize][xSize];
            for (int y = 0; y < level.length; y++) {
                for (int x = 0; x < level[0].length; x++) {
                    level[y][x] = (byte)getPref().getInteger(i+"_"+x+"_"+y, 0);
                }
            }
            solutions.add(level);
        }

        for (int i = 0; i < size; i++) {
            byte[][] level = new byte[ySize][xSize];
            for (int y = 0; y < level.length; y++) {
                for (int x = 0; x < level[0].length; x++) {
                    level[y][x] = (byte)getPref().getInteger(i+"_"+x+"_"+y+"_", 0);
                }
            }
            solutionsPossibilities.add(level);
        }

        ArcheOLogicPanel panel = (ArcheOLogicPanel)getMainPanel();
        panel.getGame().setAllSolutions(solutions, solutionsPossibilities);
    }

}
