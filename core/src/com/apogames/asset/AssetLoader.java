/*
 * Copyright (c) 2005-2020 Dirk Aporius <dirk.aporius@gmail.com>
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

package com.apogames.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The type Asset loader.
 */
public class AssetLoader {

	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";

	private static Texture backgroundTexture;
	public static TextureRegion[] backgroundTextureRegion;

	private static Texture backgroundMainTexture;
	public static TextureRegion backgroundMainTextureRegion;
	private static Texture backgroundQuestionTexture;
	public static TextureRegion backgroundQuestionTextureRegion;

	private static Texture boardMainTexture;
	public static TextureRegion boardMainTextureRegion;

	private static Texture boardTexture;
	public static TextureRegion boardTextureRegion;

	private static Texture boardTextureLittle;
	public static TextureRegion boardTextureLittleRegion;

	private static Texture hudMenuTexture;
	public static TextureRegion hudMenuTextureRegion;
	private static Texture xTexture;
	public static TextureRegion xTextureRegion;

	private static Texture waterTexture;
	public static TextureRegion[] waterTextureRegion;
	private static Texture buttonXTexture;
	public static TextureRegion[] buttonXTextureRegion;
	private static Texture buttonHelpTexture;
	public static TextureRegion[] buttonHelpTextureRegion;
	private static Texture buttonFixTexture;
	public static TextureRegion[] buttonFixTextureRegion;
	private static Texture buttonRestartTexture;
	public static TextureRegion[] buttonRestartTextureRegion;
	private static Texture buttonRightTexture;
	public static TextureRegion[] buttonRightTextureRegion;
	private static Texture buttonLeftTexture;
	public static TextureRegion[] buttonLeftTextureRegion;
	private static Texture buttonBlancoTexture;
	public static TextureRegion[] buttonBlancoTextureRegion;
	private static Texture buttonBlancoSmallTexture;
	public static TextureRegion[] buttonBlancoSmallTextureRegion;
	private static Texture coinTexture;
	public static TextureRegion coinTextureRegion;
	public static BitmapFont font40;
	public static BitmapFont font20;
	public static BitmapFont font15;
	public static BitmapFont font25;
	public static BitmapFont font30;



	public static void load() {
		backgroundTexture = new Texture(Gdx.files.internal("images/background_2.png"));
		backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		backgroundTextureRegion = new TextureRegion[5];
		for (int i = 0; i < backgroundTextureRegion.length; i++) {
			backgroundTextureRegion[i] = new TextureRegion(backgroundTexture, 128 * i, 0, 128, 128);
			backgroundTextureRegion[i].flip(false, true);
		}

		backgroundMainTexture = new Texture(Gdx.files.internal("images/background_main.png"));
		backgroundMainTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		backgroundMainTextureRegion = new TextureRegion(backgroundMainTexture, 0, 0, 1422, 800);
		backgroundMainTextureRegion.flip(false, true);

		boardMainTexture = new Texture(Gdx.files.internal("images/board_main.png"));
		boardMainTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		boardMainTextureRegion = new TextureRegion(boardMainTexture, 0, 0, 1422, 800);
		boardMainTextureRegion.flip(false, true);

		backgroundQuestionTexture = new Texture(Gdx.files.internal("images/background_question.png"));
		backgroundQuestionTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		backgroundQuestionTextureRegion = new TextureRegion(backgroundQuestionTexture, 0, 0, 700, 700);
		backgroundQuestionTextureRegion.flip(false, true);

		boardTexture = new Texture(Gdx.files.internal("images/gameboard.png"));
		boardTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		boardTextureRegion = new TextureRegion(boardTexture, 0, 0, 455, 455);
		boardTextureRegion.flip(false, true);


		boardTextureLittle = new Texture(Gdx.files.internal("images/gameboard_little.png"));
		boardTextureLittle.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		boardTextureLittleRegion = new TextureRegion(boardTextureLittle, 0, 0, 120, 120);
		boardTextureLittleRegion.flip(false, true);


		coinTexture = new Texture(Gdx.files.internal("images/coin.png"));
		coinTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		coinTextureRegion = new TextureRegion(coinTexture, 0, 0, 128, 128);
		coinTextureRegion.flip(false, true);

		hudMenuTexture = new Texture(Gdx.files.internal("images/hud_menu.png"));
		hudMenuTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		hudMenuTextureRegion = new TextureRegion(hudMenuTexture, 0, 0, 767, 674);
		hudMenuTextureRegion.flip(false, true);

		xTexture = new Texture(Gdx.files.internal("images/x.png"));
		xTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		xTextureRegion = new TextureRegion(xTexture, 0, 0, 90, 90);
		xTextureRegion.flip(false, true);

		waterTexture = new Texture(Gdx.files.internal("images/water.png"));
		waterTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		waterTextureRegion = new TextureRegion[16];
		for (int i = 0; i < waterTextureRegion.length; i++) {
			int x = i;
			int y = 0;
			if (x >= 8) {
				x = i - 8;
				y = 1;
			}

			waterTextureRegion[i] = new TextureRegion(waterTexture, 128 * x, y * 128, 128, 128);
			waterTextureRegion[i].flip(false, true);
		}

		buttonXTexture = new Texture(Gdx.files.internal("images/buttons_x.png"));
		buttonXTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		buttonXTextureRegion = new TextureRegion[3];
		for (int i = 0; i < buttonXTextureRegion.length; i++) {
			buttonXTextureRegion[i] = new TextureRegion(buttonXTexture, 70 * i, 0, 70, 70);
			buttonXTextureRegion[i].flip(false, true);
		}

		buttonHelpTexture = new Texture(Gdx.files.internal("images/buttons_help.png"));
		buttonHelpTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		buttonHelpTextureRegion = new TextureRegion[3];
		for (int i = 0; i < buttonHelpTextureRegion.length; i++) {
			buttonHelpTextureRegion[i] = new TextureRegion(buttonHelpTexture, 70 * i, 0, 70, 70);
			buttonHelpTextureRegion[i].flip(false, true);
		}

		buttonFixTexture = new Texture(Gdx.files.internal("images/buttons_fix.png"));
		buttonFixTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		buttonFixTextureRegion = new TextureRegion[3];
		for (int i = 0; i < buttonFixTextureRegion.length; i++) {
			buttonFixTextureRegion[i] = new TextureRegion(buttonFixTexture, 70 * i, 0, 70, 70);
			buttonFixTextureRegion[i].flip(false, true);
		}

		buttonRestartTexture = new Texture(Gdx.files.internal("images/buttons_restart.png"));
		buttonRestartTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		buttonRestartTextureRegion = new TextureRegion[3];
		for (int i = 0; i < buttonRestartTextureRegion.length; i++) {
			buttonRestartTextureRegion[i] = new TextureRegion(buttonRestartTexture, 70 * i, 0, 70, 70);
			buttonRestartTextureRegion[i].flip(false, true);
		}

		buttonRightTexture = new Texture(Gdx.files.internal("images/buttons_right.png"));
		buttonRightTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		buttonRightTextureRegion = new TextureRegion[3];
		for (int i = 0; i < buttonRightTextureRegion.length; i++) {
			buttonRightTextureRegion[i] = new TextureRegion(buttonRightTexture, 70 * i, 0, 70, 70);
			buttonRightTextureRegion[i].flip(false, true);
		}

		buttonLeftTexture = new Texture(Gdx.files.internal("images/buttons_left.png"));
		buttonLeftTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		buttonLeftTextureRegion = new TextureRegion[3];
		for (int i = 0; i < buttonLeftTextureRegion.length; i++) {
			buttonLeftTextureRegion[i] = new TextureRegion(buttonLeftTexture, 70 * i, 0, 70, 70);
			buttonLeftTextureRegion[i].flip(false, true);
		}

		buttonBlancoTexture = new Texture(Gdx.files.internal("images/buttons_blanco.png"));
		buttonBlancoTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		buttonBlancoTextureRegion = new TextureRegion[3];
		for (int i = 0; i < buttonBlancoTextureRegion.length; i++) {
			buttonBlancoTextureRegion[i] = new TextureRegion(buttonBlancoTexture, 142 * i, 0, 142, 70);
			buttonBlancoTextureRegion[i].flip(false, true);
		}

		buttonBlancoSmallTexture = new Texture(Gdx.files.internal("images/buttons_blanco_small.png"));
		buttonBlancoSmallTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		buttonBlancoSmallTextureRegion = new TextureRegion[3];
		for (int i = 0; i < buttonBlancoSmallTextureRegion.length; i++) {
			buttonBlancoSmallTextureRegion[i] = new TextureRegion(buttonBlancoSmallTexture, 64 * i, 0, 64, 64);
			buttonBlancoSmallTextureRegion[i].flip(false, true);
		}

		// Always use bitmap fonts for HTML5 compatibility
		// TTF fonts are loaded in platform-specific code (Desktop/Android only)
		font40 = new BitmapFont(Gdx.files.internal("fonts/pirate40.fnt"), Gdx.files.internal("fonts/pirate40.png"), true);
		font20 = new BitmapFont(Gdx.files.internal("fonts/pirate20.fnt"), Gdx.files.internal("fonts/pirate20.png"), true);
		font15 = new BitmapFont(Gdx.files.internal("fonts/pirate15.fnt"), Gdx.files.internal("fonts/pirate15.png"), true);
		font25 = new BitmapFont(Gdx.files.internal("fonts/pirate25.fnt"), Gdx.files.internal("fonts/pirate25.png"), true);
		font30 = new BitmapFont(Gdx.files.internal("fonts/pirate30.fnt"), Gdx.files.internal("fonts/pirate30.png"), true);
	}

	public static void dispose() {
		backgroundTexture.dispose();
		backgroundMainTexture.dispose();
		backgroundQuestionTexture.dispose();
		boardMainTexture.dispose();
		boardTexture.dispose();
		boardTextureLittle.dispose();
		waterTexture.dispose();
		coinTexture.dispose();
		buttonXTexture.dispose();
		buttonFixTexture.dispose();
		buttonHelpTexture.dispose();
		buttonRestartTexture.dispose();
		hudMenuTexture.dispose();
		xTexture.dispose();
		buttonRightTexture.dispose();
		buttonLeftTexture.dispose();
		buttonBlancoTexture.dispose();
		buttonBlancoSmallTexture.dispose();
		font40.dispose();
		font30.dispose();
		font25.dispose();
		font20.dispose();
		font15.dispose();
//        click.dispose();
	}

}

