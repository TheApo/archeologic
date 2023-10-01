package com.apogames;

import com.apogames.asset.AssetLoader;
import com.apogames.backend.Game;
import com.apogames.game.MainPanel;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class ArcheOLogic extends Game {
	@Override
	public void create () {
		AssetLoader.load();
		setScreen(new MainPanel());
		if (Constants.IS_HTML) {
			Gdx.graphics.setContinuousRendering(false);
			Gdx.graphics.requestRendering();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}

	public void resume() {
		super.resume();
		AssetLoader.load();
	}
}
