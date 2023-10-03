package com.apogames.game.menu;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.DrawString;
import com.apogames.backend.SequentiallyThinkingScreenModel;
import com.apogames.common.Localization;
import com.apogames.entity.ApoButton;
import com.apogames.game.MainPanel;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Locale;

public class Menu extends SequentiallyThinkingScreenModel {

    public static final String FUNCTION_BACK = "MENU_QUIT";

    public static final String FUNCTION_PLAY = "MENU_PLAY";
    public static final String FUNCTION_PLAYER_LEFT = "MENU_PLAYER_LEFT";
    public static final String FUNCTION_PLAYER_RIGHT = "MENU_PLAYER_RIGHT";

    public static final String FUNCTION_LANGUAGE = "MENU_LANGUAGE";

    private final boolean[] keys = new boolean[256];

    private boolean isPressed = false;

    private boolean german = true;

    public Menu(final MainPanel game) {
        super(game);
    }

    public void setNeededButtonsVisible() {
        getMainPanel().getButtonByFunction(FUNCTION_BACK).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_PLAY).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_LANGUAGE).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_PLAYER_LEFT).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_PLAYER_RIGHT).setVisible(true);
    }

    @Override
    public void init() {
        if (getGameProperties() == null) {
            setGameProperties(new MenuPreferences(this));
            loadProperties();
        }

        this.getMainPanel().resetSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        this.german = Localization.getInstance().getLocale().getLanguage().equals("de");

        this.setNeededButtonsVisible();
        this.setButtonsVisibility();
    }

    @Override
    public void keyPressed(int keyCode, char character) {
        super.keyPressed(keyCode, character);

        keys[keyCode] = true;
    }

    @Override
    public void keyButtonReleased(int keyCode, char character) {
        super.keyButtonReleased(keyCode, character);

        keys[keyCode] = false;
    }

    public void mouseMoved(int mouseX, int mouseY) {
    }

    public void mouseButtonReleased(int mouseX, int mouseY, boolean isRightButton) {
        this.isPressed = false;
    }

    public void mousePressed(int x, int y, boolean isRightButton) {
        if (isRightButton && !this.isPressed) {
            this.isPressed = true;
        }
    }

    public void mouseDragged(int x, int y, boolean isRightButton) {
        if (isRightButton) {
            if (!this.isPressed) {
                this.mousePressed(x, y, isRightButton);
            }
        }
    }

    @Override
    public void mouseButtonFunction(String function) {
        super.mouseButtonFunction(function);
        switch (function) {
            case Menu.FUNCTION_BACK:
                quit();
                break;
            case Menu.FUNCTION_PLAY:
                getMainPanel().changeToGame();
                break;
            case Menu.FUNCTION_LANGUAGE:
                this.german = !this.german;

                if (this.german) {
                    Localization.getInstance().setLocale(Locale.GERMAN);
                } else {
                    Localization.getInstance().setLocale(Locale.ENGLISH);
                }

                changeIDForLanguage(function);
                break;
        }
    }

    private void setButtonsVisibility() {
        changeIDForLanguage(FUNCTION_LANGUAGE);

        if (Constants.IS_HTML) {
            getMainPanel().getButtonByFunction(Menu.FUNCTION_BACK).setVisible(false);
        }
    }

    private void changeIDForLanguage(String functionLanguage) {
        if (this.german) {
            getMainPanel().getButtonByFunction(functionLanguage).setId("button_language_de");
        } else {
            getMainPanel().getButtonByFunction(functionLanguage).setId("button_language_en");
        }
    }

    public void mouseWheelChanged(int changed) {
    }

    @Override
    protected void quit() {
        getMainPanel().quitGame();
    }

    @Override
    public void doThink(float delta) {

    }

    @Override
    public void render() {
        int startY = (int)(Constants.GAME_HEIGHT/2f + 50);

        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);

        getMainPanel().getRenderer().setColor(Constants.COLOR_PURPLE_MENU[0], Constants.COLOR_PURPLE_MENU[1], Constants.COLOR_PURPLE_MENU[2], 1f);

        getMainPanel().getRenderer().end();

        getMainPanel().spriteBatch.begin();

        getMainPanel().spriteBatch.draw(AssetLoader.hudMenuTextureRegion, Constants.GAME_WIDTH/2f - AssetLoader.hudMenuTextureRegion.getRegionWidth()/2f, 5);

        getMainPanel().drawString(Localization.getInstance().getCommon().get("title"), Constants.GAME_WIDTH/2f, 90, Constants.COLOR_WHITE, AssetLoader.font40, DrawString.MIDDLE, true, false);
        getMainPanel().drawString(Localization.getInstance().getCommon().get("title_description"), Constants.GAME_WIDTH/2f, 135, Constants.COLOR_WHITE, AssetLoader.font25, DrawString.MIDDLE, true, false);

        int hudY = 180;

        getMainPanel().drawString(Localization.getInstance().getCommon().get("hud_rules")+":", Constants.GAME_WIDTH/2f, hudY, Constants.COLOR_WHITE, AssetLoader.font30, DrawString.MIDDLE, false, false);
        String[] rules = Localization.getInstance().getCommon().get("hud_rules_text").split(";");
        for (int i = 0; i < rules.length; i++) {
            getMainPanel().drawString(rules[i], Constants.GAME_WIDTH/2f, hudY + 40 + i * 25, Constants.COLOR_WHITE, AssetLoader.font20, DrawString.MIDDLE, false, false);
        }

        ApoButton buttonLeft = getMainPanel().getButtonByFunction(FUNCTION_PLAYER_LEFT);
        getMainPanel().drawString(Localization.getInstance().getCommon().get("menu_player"), Constants.GAME_WIDTH/2f, buttonLeft.getY() - 20, Constants.COLOR_WHITE, AssetLoader.font25, DrawString.MIDDLE, true, false);

        getMainPanel().spriteBatch.end();

        for (ApoButton button : this.getMainPanel().getButtons()) {
            button.render(this.getMainPanel());
        }
    }

//	        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
//			Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);
//
//			getMainPanel().getRenderer().begin(ShapeType.Line);
//			getMainPanel().getRenderer().setColor(Constants.COLOR_WHITE[0], Constants.COLOR_WHITE[1], Constants.COLOR_WHITE[2], 1f);
//			getMainPanel().getRenderer().roundedRectLine((WIDTH - width)/2f, startY, width, height, 5);
//			getMainPanel().getRenderer().end();


    public void drawOverlay() {
    }

    @Override
    public void dispose() {
    }
}
