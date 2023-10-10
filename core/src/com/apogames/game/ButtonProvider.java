/*
 * Copyright (c) 2005-2013 Dirk Aporius <dirk.aporius@gmail.com>
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.apogames.game;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.entity.ApoButton;
import com.apogames.entity.ApoButtonImageThree;
import com.apogames.entity.ApoButtonImageWithThree;
import com.apogames.game.archeologic.ArcheOLogicPanel;
import com.apogames.game.menu.Menu;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class ButtonProvider {

	private final MainPanel game;

	public ButtonProvider(MainPanel game) {
		this.game = game;
	}

	public void init() {
		if ((this.game.getButtons() == null) || (this.game.getButtons().size() <= 0)) {
			this.game.getButtons().clear();

			BitmapFont font = AssetLoader.font25;
			String text = "";
			String function = Menu.FUNCTION_BACK;
			int width = 64;
			int height = 64;
			int x = Constants.GAME_WIDTH - width - 15;
			int y = Constants.GAME_HEIGHT - height - 5;
			ApoButton button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonXTextureRegion);
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(1);
			button.setFont(AssetLoader.font40);
			this.game.getButtons().add(button);

			text = "X";
			function = ArcheOLogicPanel.FUNCTION_TIWANAKU_BACK;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH - width - 15;
			y = Constants.GAME_HEIGHT - height - 5;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonXTextureRegion);
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(1);
			button.setFont(AssetLoader.font40);
			this.game.getButtons().add(button);

			text = "Play";
			function = Menu.FUNCTION_PLAY;
			width = 164;
			height = 80;
			x = Constants.GAME_WIDTH/2 - width/2;
			y = Constants.GAME_HEIGHT/2 + 110 + 50;
			button = new ApoButtonImageThree(x, y, width, height, function, text, 0, 0, width, height, Constants.COLOR_CLEAR, "button_play");
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(1);
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			text = "de";
			function = Menu.FUNCTION_LANGUAGE;
			width = 64;
			height = 64;
			x = 15;
			y = Constants.GAME_HEIGHT - height - 5;
			button = new ApoButtonImageThree(x, y, width, height, function, text, 0, 0, width, height, Constants.COLOR_CLEAR, "button_language_de");
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			int addValue = 100;

			text = "<";
			function = Menu.FUNCTION_AMOUNTTILES_LEFT;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 - width - addValue;
			y = Constants.GAME_HEIGHT/2 - 40;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonLeftTextureRegion);
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			text = ">";
			function = Menu.FUNCTION_AMOUNTTILES_RIGHT;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 + addValue;
			y = Constants.GAME_HEIGHT/2 - 40;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonRightTextureRegion);
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);


			text = "<";
			function = Menu.FUNCTION_DIFFICULTY_LEFT;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 - width - addValue;
			y = Constants.GAME_HEIGHT/2 + 80;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonLeftTextureRegion);
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			text = ">";
			function = Menu.FUNCTION_DIFFICULTY_RIGHT;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 + addValue;
			y = Constants.GAME_HEIGHT/2 + 80;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonRightTextureRegion);
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);


			text = "new Level";
			function = ArcheOLogicPanel.FUNCTION_NEW_LEVEL;
			width = 180;
			height = 64;
			x = Constants.GAME_WIDTH - width - 30;
			y = Constants.GAME_HEIGHT - (height + 5) * 3;
			button = new ApoButtonImageThree(x, y, width, height, function, text, 0, 0, width, height, Constants.COLOR_CLEAR, "button_newlevel");
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = "Test";
			function = ArcheOLogicPanel.FUNCTION_QUESTION_TEST;
			width = 180;
			height = 64;
			x = Constants.GAME_WIDTH - width - 30;
			y = Constants.GAME_HEIGHT - (height + 5) * 4;
			button = new ApoButtonImageThree(x, y, width, height, function, text, 0, 0, width, height, Constants.COLOR_CLEAR, "button_question");
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = "restart";
			function = ArcheOLogicPanel.FUNCTION_RESTART;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH - AssetLoader.hudRightTextureRegion.getRegionWidth() + 5;
			y = Constants.GAME_HEIGHT - (height + 5);
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonRestartTextureRegion);
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			text = "new Level";
			function = ArcheOLogicPanel.FUNCTION_FINISH_NEW_LEVEL;
			width = 180;
			height = 64;
			x = Constants.GAME_WIDTH/2 - AssetLoader.hudRightTextureRegion.getRegionWidth()/2 - width/2;
			y = Constants.GAME_HEIGHT/2 + AssetLoader.wonTextureRegion.getRegionHeight()/2 - (height + 50);
			button = new ApoButtonImageThree(x, y, width, height, function, text, 0, 0, width, height, Constants.COLOR_CLEAR, "button_newlevel");
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			text = "restart";
			function = ArcheOLogicPanel.FUNCTION_FINISH_RESTART;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 - AssetLoader.hudRightTextureRegion.getRegionWidth()/2 - AssetLoader.wonTextureRegion.getRegionWidth()/2 + 55;
			y = Constants.GAME_HEIGHT/2 + AssetLoader.wonTextureRegion.getRegionHeight()/2 - (height + 50);
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonRestartTextureRegion);
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			text = "back";
			function = ArcheOLogicPanel.FUNCTION_FINISH_BACK;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 - AssetLoader.hudRightTextureRegion.getRegionWidth()/2 + AssetLoader.wonTextureRegion.getRegionWidth()/2 - 55 - width;
			y = Constants.GAME_HEIGHT/2 + AssetLoader.wonTextureRegion.getRegionHeight()/2 - (height + 50);
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonXTextureRegion);
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			for (int i = 0; i < this.game.getButtons().size(); i++) {
				this.game.getButtons().get(i).setBOpaque(false);
			}
		}
	}
}
