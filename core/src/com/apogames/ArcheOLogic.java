package com.apogames;

import com.apogames.asset.AssetLoader;
import com.apogames.backend.Game;
import com.apogames.backend.HtmlInputProcessor;
import com.apogames.backend.HtmlRenderingController;
import com.apogames.game.MainPanel;
import com.badlogic.gdx.Gdx;

public class ArcheOLogic extends Game {
	@Override
	public void create () {
		AssetLoader.load();
		setScreen(new MainPanel());

		if (Constants.IS_HTML) {
			// HTML5: Manual Rendering Control (setContinuousRendering funktioniert nicht in GWT)
			HtmlRenderingController.setContinuousRendering(false);
			System.out.println("HTML5: Manual rendering control aktiviert");
		} else {
			// Desktop/Android: Standard non-continuous rendering
			Gdx.graphics.setContinuousRendering(false);
			Gdx.graphics.requestRendering();
			System.out.println("Desktop/Android: Non-continuous rendering aktiviert");
		}
	}

	@Override
	public void render() {
		if (Constants.IS_HTML) {
			// HTML5: Nur rendern wenn explizit erlaubt
			if (HtmlRenderingController.shouldRender()) {
				super.render();
			}
			// Sonst: Nichts tun = kein Rendering!
		} else {
			// Desktop/Android: Normal rendern
			super.render();
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
