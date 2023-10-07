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
        return Gdx.app.getPreferences("ArcheOLogicPanelPreferences");
    }

    public void writeLevel() {
        getPref().flush();
    }

    public void readLevel() {

    }

}
