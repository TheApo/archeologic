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
import com.apogames.entity.ApoButtonImageDropdown;
import com.apogames.entity.ApoButtonImageThree;
import com.apogames.entity.ApoButtonImageWithThree;
import com.apogames.game.archeologic.ArcheOLogicPanel;
import com.apogames.game.menu.Menu;
import com.apogames.game.question.QuestionEnum;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;

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

			text = "";
			function = ArcheOLogicPanel.FUNCTION_ARCHEOLOGIC_BACK;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH - width - 25;
			y = Constants.GAME_HEIGHT - height - 60;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonXTextureRegion);
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			((ApoButtonImageWithThree)(button)).setMouseOverText(AssetLoader.buttonBlancoTextureRegion[0], "Menu");
			button.setStroke(1);
			button.setFont(AssetLoader.font20);
			this.game.getButtons().add(button);

			text = "Play";
			function = Menu.FUNCTION_PLAY;
			width = 164;
			height = 80;
			x = Constants.GAME_WIDTH/2 - width - 40;
			y = Constants.GAME_HEIGHT/2 + 110 + 50;
			button = new ApoButtonImageThree(x, y, width, height, function, text, 0, 0, width, height, Constants.COLOR_BLACK, "button_play");
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(1);
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			text = "Puzzle";
			function = Menu.FUNCTION_PUZZLE;
			width = 164;
			height = 80;
			x = Constants.GAME_WIDTH/2 + 40;
			y = Constants.GAME_HEIGHT/2 + 110 + 50;
			button = new ApoButtonImageThree(x, y, width, height, function, text, 0, 0, width, height, Constants.COLOR_BLACK, "button_puzzle");
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
			button = new ApoButtonImageThree(x, y, width, height, function, text, 0, 0, width, height, Constants.COLOR_BLACK, "button_language_de");
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			int addValue = 100;

			text = "";
			function = Menu.FUNCTION_AMOUNTTILES_LEFT;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 - width - addValue;
			y = Constants.GAME_HEIGHT/2 - 80;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonLeftTextureRegion);
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			text = "";
			function = Menu.FUNCTION_AMOUNTTILES_RIGHT;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 + addValue;
			y = Constants.GAME_HEIGHT/2 - 80;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonRightTextureRegion);
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);


			text = "";
			function = Menu.FUNCTION_DIFFICULTY_LEFT;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 - width - addValue;
			y = Constants.GAME_HEIGHT/2 + 60;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonLeftTextureRegion);
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			text = "";
			function = Menu.FUNCTION_DIFFICULTY_RIGHT;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 + addValue;
			y = Constants.GAME_HEIGHT/2 + 60;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonRightTextureRegion);
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);


			text = "";
			function = ArcheOLogicPanel.FUNCTION_NEW_LEVEL;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH - 600 + width + 20;
			y = 70;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonRightTextureRegion);
			button.setFont(AssetLoader.font20);
			((ApoButtonImageWithThree)(button)).setMouseOverText(AssetLoader.buttonBlancoTextureRegion[0], "New Level");
			this.game.getButtons().add(button);

			text = "";
			function = ArcheOLogicPanel.FUNCTION_QUESTION_TEST;
			width = 64;
			height = 64;
			x = 10;
			y = 10;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonHelpTextureRegion);
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = "check";
			function = ArcheOLogicPanel.FUNCTION_QUESTION_CHECK;
			width = 180;
			height = 64;
			x = Constants.GAME_WIDTH - 360 + 20;
			y = Constants.GAME_HEIGHT - 60 - height;
			button = new ApoButtonImageThree(x, y, width, height, function, text, 0, 0, width, height, Constants.COLOR_BLACK, "button_check");
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = "frage";
			function = ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION;
			width = 180;
			height = 64;
			x = Constants.GAME_WIDTH - width - 430;
			y = Constants.GAME_HEIGHT - 60 - height;
			button = new ApoButtonImageThree(x, y, width, height, function, text, 0, 0, width, height, Constants.COLOR_BLACK, "button_question");
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);


			text = "next hint";
			function = ArcheOLogicPanel.FUNCTION_QUESTIONS_NEXT_HINT;
			width = 200;
			height = 64;
			x = Constants.GAME_WIDTH - width - 410;
			y = Constants.GAME_HEIGHT - 60 - height;
			button = new ApoButtonImageThree(x, y, width, height, function, text, 0, 0, width, height, Constants.COLOR_BLACK, "button_next_hint");
			button.setFont(AssetLoader.font20);
			this.game.getButtons().add(button);

			text = "Help";
			function = ArcheOLogicPanel.FUNCTION_QUESTIONS_HELP;
			width = 200;
			height = 64;
			x = Constants.GAME_WIDTH - width - 410;
			y = Constants.GAME_HEIGHT - 60 - height;
			button = new ApoButtonImageThree(x, y, width, height, function, text, 0, 0, width, height, Constants.COLOR_BLACK, "button_help");
			button.setFont(AssetLoader.font20);
			this.game.getButtons().add(button);

			text = "";
			function = ArcheOLogicPanel.FUNCTION_RESTART;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH - 600;
			y = 70;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonRestartTextureRegion);
			button.setFont(AssetLoader.font25);
			((ApoButtonImageWithThree)(button)).setMouseOverText(AssetLoader.buttonBlancoTextureRegion[0], "Restart");
			this.game.getButtons().add(button);

			int size = 300;

			text = "level";
			function = ArcheOLogicPanel.FUNCTION_FINISH_NEW_LEVEL;
			width = 180;
			height = 64;
			x = Constants.GAME_WIDTH/2 - width/2;
			y = Constants.GAME_HEIGHT/2 + size/2 - (height + 20);
			button = new ApoButtonImageThree(x, y, width, height, function, text, 0, 0, width, height, Constants.COLOR_BLACK, "button_newlevel");
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = "";
			function = ArcheOLogicPanel.FUNCTION_FINISH_RESTART;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 - 135 - width;
			y = Constants.GAME_HEIGHT/2 + size/2 - (height + 20);
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonRestartTextureRegion);
			button.setFont(AssetLoader.font25);
			((ApoButtonImageWithThree)(button)).setMouseOverText(AssetLoader.buttonBlancoTextureRegion[0], "Restart");
			this.game.getButtons().add(button);

			text = "";
			function = ArcheOLogicPanel.FUNCTION_FINISH_BACK;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 + 135;
			y = Constants.GAME_HEIGHT/2 + size/2 - (height + 20);
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonXTextureRegion);
			((ApoButtonImageWithThree)(button)).setMouseOverText(AssetLoader.buttonBlancoTextureRegion[0], "Menu");
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			String[] values = ArcheOLogicPanel.askOrder;
			for (int i = 0; i < values.length; i++) {
				text = values[i];
				function = ArcheOLogicPanel.FUNCTION_QUESTION_ROW + text;
				width = 64;
				height = 64;
				x = Constants.GAME_WIDTH - 10 - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() + 30 + i * width;
				y = 160;
				button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonBlancoSmallTextureRegion);
				button.setFont(AssetLoader.font30);
				this.game.getButtons().add(button);

				if (i == 0) {
					button.setSelect(true);
				}
			}

			QuestionEnum[] enumValues = QuestionEnum.getQuestionEnumForQuestionType(0);
			createAskButtons(enumValues);

			QuestionEnum[] enumValuesOther = QuestionEnum.getQuestionEnumForQuestionType(1);
			createAskButtons(enumValuesOther);

			text = "frage";
			function = ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_REAL;
			width = 180;
			height = 64;
			x = Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth()/2 - width/2;
			y = Constants.GAME_HEIGHT - 20 - height;
			button = new ApoButtonImageThree(x, y, width, height, function, text, 0, 0, width, height, Constants.COLOR_BLACK, "button_question");
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = "ORIGINAL";
			function = ArcheOLogicPanel.FUNCTION_QUESTIONS_ORIGINAL;
			width = AssetLoader.backgroundQuestionTextureRegion.getRegionWidth()/2;
			height = 64;
			x = Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth()/2 - width - 10;
			y = 50;
			button = new ApoButtonImageThree(x, y, width, height, function, text, 0, 0, width, height, Constants.COLOR_BLACK, "button_questions_original");
			button.setFont(AssetLoader.font25);
			button.setSelect(true);
			this.game.getButtons().add(button);

			text = "OTHER";
			function = ArcheOLogicPanel.FUNCTION_QUESTIONS_OTHER;
			width = AssetLoader.backgroundQuestionTextureRegion.getRegionWidth()/2;
			height = 64;
			x = Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth()/2 - 10;
			y = 50;
			button = new ApoButtonImageThree(x, y, width, height, function, text, 0, 0, width, height, Constants.COLOR_BLACK, "button_questions_other");
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = "";
			function = ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_DROPDOWN;
			width = 40;
			height = 40;
			x = Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth()/2 - width/2 - 32;
			y = ArcheOLogicPanel.START_QUESTION_Y;
			button = new ApoButtonImageDropdown(x, y, width, height, function, text, AssetLoader.buttonBlancoSmallTextureRegion);
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = "";
			function = ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_DROPDOWN_EMPTY_NEXT_TILE;
			width = 40;
			height = 40;
			x = Constants.GAME_WIDTH - 240;
			y = ArcheOLogicPanel.START_QUESTION_Y + 60;
			button = new ApoButtonImageDropdown(x, y, width, height, function, text, AssetLoader.buttonBlancoSmallTextureRegion);
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = "";
			function = ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE;
			width = 40;
			height = 40;
			x = Constants.GAME_WIDTH - 460;
			y = ArcheOLogicPanel.START_QUESTION_Y + 60 * 2;
			button = new ApoButtonImageDropdown(x, y, width, height, function, text, AssetLoader.buttonBlancoSmallTextureRegion);
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = "";
			function = ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_DROPDOWN_TILE_NEXT_TILE_OTHER;
			width = 40;
			height = 40;
			x = Constants.GAME_WIDTH - 305;
			y = ArcheOLogicPanel.START_QUESTION_Y + 60 * 2;
			button = new ApoButtonImageDropdown(x, y, width, height, function, text, AssetLoader.buttonBlancoSmallTextureRegion);
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = "";
			function = ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_DROPDOWN_HORIZONTAL;
			width = 40;
			height = 40;
			x = Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth()/2 - width/2 - 72;
			y = ArcheOLogicPanel.START_QUESTION_Y;
			button = new ApoButtonImageDropdown(x, y, width, height, function, text, AssetLoader.buttonBlancoSmallTextureRegion);
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = "";
			function = ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_DROPDOWN_CORNER;
			width = 40;
			height = 40;
			x = Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth()/2 - width/2 - 72;
			y = ArcheOLogicPanel.START_QUESTION_Y + 60 * 3;
			button = new ApoButtonImageDropdown(x, y, width, height, function, text, AssetLoader.buttonBlancoSmallTextureRegion);
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = "";
			function = ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_DROPDOWN_STRING_SIDE;
			width = 330;
			height = 40;
			x = Constants.GAME_WIDTH - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth()/2 - 22;
			y = ArcheOLogicPanel.START_QUESTION_Y;
			button = new ApoButtonImageDropdown(x, y, width, height, function, text, AssetLoader.buttonBlancoSmallTextureRegion);
			button.setFont(AssetLoader.font15);
			ArrayList<String> strings = new ArrayList<>();
			strings.add("question_horizontal");
			strings.add("question_vertical");
			strings.add("question_complete");
			((ApoButtonImageDropdown)(button)).setValues(strings);
			this.game.getButtons().add(button);

			text = "";
			function = ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_CLOSE;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH - width - 25;
			y = Constants.GAME_HEIGHT - height - 20;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonXTextureRegion);
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(1);
			button.setFont(AssetLoader.font40);
			this.game.getButtons().add(button);


			text = "";
			function = ArcheOLogicPanel.FUNCTION_HINT_UP;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH - width - 20;
			y = 230;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonLeftTextureRegion);
			button.setFont(AssetLoader.font30);
			((ApoButtonImageWithThree)(button)).setRotate(90);
			this.game.getButtons().add(button);

			text = "";
			function = ArcheOLogicPanel.FUNCTION_HINT_DOWN;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH - width - 20;
			y = Constants.GAME_HEIGHT - height - 150;
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonRightTextureRegion);
			button.setFont(AssetLoader.font30);
			((ApoButtonImageWithThree)(button)).setRotate(90);
			this.game.getButtons().add(button);


			text = "";
			function = ArcheOLogicPanel.FUNCTION_CANCEL;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 - 135 - width;
			y = Constants.GAME_HEIGHT/2 + size/2 - (height + 20);
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonXTextureRegion);
			button.setFont(AssetLoader.font25);
			((ApoButtonImageWithThree)(button)).setMouseOverText(AssetLoader.buttonBlancoTextureRegion[0], "Cancel");
			this.game.getButtons().add(button);

			text = "";
			function = ArcheOLogicPanel.FUNCTION_OK;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 + 135;
			y = Constants.GAME_HEIGHT/2 + size/2 - (height + 20);
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonRightTextureRegion);
			button.setFont(AssetLoader.font25);
			((ApoButtonImageWithThree)(button)).setMouseOverText(AssetLoader.buttonBlancoTextureRegion[0], "Ok");
			this.game.getButtons().add(button);

			for (int i = 0; i < this.game.getButtons().size(); i++) {
				this.game.getButtons().get(i).setBOpaque(false);
			}
		}
	}

	private void createAskButtons(QuestionEnum[] enumValuesOther) {
		String text;
		String function;
		int width;
		int height;
		int x;
		int y;
		ApoButton button;
		for (int i = 0; i < enumValuesOther.length; i++) {
			text = "";
			function = ArcheOLogicPanel.FUNCTION_QUESTION_QUESTION_ASK + enumValuesOther[i].name();
			width = 50;
			height = 50;
			x = Constants.GAME_WIDTH - 10 - AssetLoader.backgroundQuestionTextureRegion.getRegionWidth() + 30;
			y = ArcheOLogicPanel.START_QUESTION_Y + i * (height + 10);
			button = new ApoButtonImageWithThree(x, y, width, height, function, text, AssetLoader.buttonBlancoSmallTextureRegion);
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			if (i == 0) {
				button.setSelect(true);
			}
		}
	}
}
