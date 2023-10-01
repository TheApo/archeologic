package com.apogames.game;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.DrawString;
import com.apogames.backend.GameScreen;
import com.apogames.backend.ScreenModel;
import com.apogames.common.Localization;
import com.apogames.game.archeologic.ArcheOLogicPanel;
import com.apogames.game.menu.Menu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import java.util.Locale;

public class MainPanel extends GameScreen {

    private ArcheOLogicPanel game;
    private Menu menu;
    
    //private FPSLogger logger = new FPSLogger();

    public MainPanel() {
        super();
        if ((this.getButtons() == null) || (this.getButtons().size() == 0)) {
            ButtonProvider button = new ButtonProvider(this);
            button.init();
        }

        if (Constants.IS_HTML) {
            Gdx.graphics.setContinuousRendering(false);
        }
        Localization.getInstance().setLocale(Locale.getDefault());

        if (this.game == null) {
            this.game = new ArcheOLogicPanel(this);
        }
        if (this.menu == null) {
            this.menu = new Menu(this);
        }

        this.changeToMenu();
    }

    public void changeToMenu() {
        this.changeModel(this.menu);
    }

    public void changeToGame() {
        this.changeModel(this.game);
    }

    /**
     * Quit game.
     */
    public final void quitGame() {
        this.saveProperties();
        Gdx.app.exit();
    }

    /**
     * Update level chooser.
     */
    public void saveProperties() {
    }

    private void changeModel(final ScreenModel model) {
        if (this.model != null) {
            this.model.dispose();
        }

        this.model = model;

        this.setButtonsInvisible();
        this.model.setNeededButtonsVisible();
        this.model.init();
    }
    
    public final void setButtonsInvisible() {
    	for (int i = 0; i < this.getButtons().size(); i++) {
            this.getButtons().get(i).setVisible(false);
        }
    }

    public void think(final float delta) {
        super.think(delta);
        if (model != null) model.think(delta);
    }

    public void render(float delta) {
        super.render(delta);

        this.spriteBatch.begin();
        this.spriteBatch.draw(AssetLoader.backgroundMainTextureRegion, 0, 0);
        this.spriteBatch.end();

        if (model != null) {
            model.render();
            model.drawOverlay();
        }

        this.spriteBatch.begin();
        this.drawString(String.valueOf(Gdx.graphics.getFramesPerSecond()), 5, 5, Constants.COLOR_PURPLE, AssetLoader.font15, DrawString.BEGIN, false, false);
        this.spriteBatch.end();
    }

    public void renderBackground() {
        this.getRenderer().begin(ShapeType.Filled);
        this.getRenderer().setColor(Constants.COLOR_BACKGROUND[0], Constants.COLOR_BACKGROUND[1], Constants.COLOR_BACKGROUND[2], 1);
        this.getRenderer().rect(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        this.getRenderer().end();
    }
}
