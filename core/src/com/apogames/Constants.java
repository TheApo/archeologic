/* * Copyright (c) 2005-2017 Dirk Aporius <dirk.aporius@gmail.com> * All rights reserved. *  * Redistribution and use in source and binary forms, with or without * modification, are permitted provided that the following conditions * are met: * 1. Redistributions of source code must retain the above copyright *    notice, this list of conditions and the following disclaimer. * 2. Redistributions in binary form must reproduce the above copyright *    notice, this list of conditions and the following disclaimer in the *    documentation and/or other materials provided with the distribution. * 3. The name of the author may not be used to endorse or promote products *    derived from this software without specific prior written permission. *  * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */package com.apogames;import com.apogames.common.Localization;import com.badlogic.gdx.graphics.g2d.GlyphLayout;import java.util.Locale;public class Constants {    public static final String USERLEVELS_GETPHP = "";    public static Locale REGION;	    public final static GlyphLayout glyphLayout = new GlyphLayout();    public static final double VERSION = 0.01;    public static boolean FPS = false;    public static final int TILE_SIZE = 65;    public static final int MAX_SCALE = 1;    public static final int GAME_WIDTH = 1400 * MAX_SCALE;    public static final int GAME_HEIGHT = 800 * MAX_SCALE;    public static final int SAND = 3;    public static final int GRAS = 2;    public static final int FOREST = 1;    public static float[] COLOR_CLEAR = new float[]{219f / 255f, 228f / 255f, 205f / 255f, 1f};    public static final float[] COLOR_WHITE = new float[]{255f / 255f, 255f / 255f, 255f / 255f, 1f};    public static final float[] COLOR_BROWN = new float[]{60f / 255f, 40f / 255f, 30f / 255f, 1f};    public static final float[] COLOR_SELECT = new float[]{137f / 255f, 120f / 255f, 90f / 255f, 1f};    public static final float[] COLOR_BLUE_BRIGHT = new float[]{138f / 255f, 210f / 255f, 245f / 255f, 1f};    public static final float[] COLOR_BLUE = new float[]{0f / 255f, 160f / 255f, 230f / 255f, 1f};    public static final float[] COLOR_BLUE_DARK = new float[]{10f / 255f, 40f / 255f, 105f / 255f, 1f};    public static final float[] COLOR_GREEN_BRIGHT = new float[]{128f / 255f, 255f / 255f, 128f / 255f, 1f};    public static final float[] COLOR_GREEN = new float[]{70f / 255f, 160f / 255f, 70f / 255f, 1f};    public static final float[] COLOR_RED_LIGHT = new float[]{255f / 255f, 240f / 255f, 240f / 255f, 1f};    public static final float[] COLOR_RED = new float[]{255f / 255f, 0f / 255f, 0f / 255f, 1f};    public static final float[] COLOR_RED_DARK = new float[]{90f / 255f, 0f / 255f, 0f / 255f, 1f};    public static final float[] COLOR_YELLOW = new float[]{255f / 255f, 255f / 255f, 0f / 255f, 1f};    public static final float[] COLOR_YELLOW_DARK = new float[]{60f / 255f, 60f / 255f, 0f / 255f, 1f};    public static final float[] COLOR_ORANGE = new float[]{242f / 255f, 101f / 255f, 34f / 255f, 1f};    public static final float[] COLOR_BLACK = new float[]{0f / 255f, 0f / 255f, 0f / 255f, 1f};    public static final float[] COLOR_GREY = new float[]{99f / 255f, 99f / 255f, 99f / 255f, 1f};    public static final float[] COLOR_GREY_BRIGHT = new float[]{199f / 255f, 199f / 255f, 199f / 255f, 1f};    public static final float[] COLOR_PURPLE = new float[]{56f / 255f, 51f / 255f, 98f / 255f, 1f};    public static final float[] COLOR_PURPLE_MENU = new float[]{205f / 255f, 201f / 255f, 245f / 255f, 1f};    public static final float[] COLOR_PURPLE_BRIGHT = new float[]{111f / 255f, 51f / 255f, 139f / 255f, 1f};    public static final float[] COLOR_ROSA = new float[]{231f / 255f, 43f / 255f, 120f / 255f, 1f};    public static final float[] COLOR_BACKGROUND_BRIGHT = new float[]{126f / 255f, 126f / 255f, 146f / 255f, 1f};    public static final float[] COLOR_BACKGROUND = new float[]{234f / 255f, 234f / 255f, 234f / 255f, 1f};    public static final float[] COLOR_BACKGROUND_CARD = new float[]{214f / 255f, 214f / 255f, 214f / 255f, 1f};    public static final float[] COLOR_BACKGROUND_CARD_2 = new float[]{199f / 255f, 199f / 255f, 199f / 255f, 1f};    public static final float[] COLOR_CARD_UNDERGROUND = new float[]{229f / 255f, 26f / 255f, 116f / 255f, 1f};    public static final float[] COLOR_BUTTONS = new float[]{210f / 255f, 231f / 255f, 210f / 255f, 1f};    public static boolean HELP_TIMER = false;        public static boolean IS_ANDROID = false;    public static boolean IS_HTML = false;        public static String round(double zahl, int stellen) {        double d = (double) ((int) zahl + (Math.round(Math.pow(10, stellen) * (zahl - (int) zahl))) / (Math.pow(10, stellen)));        String result = String.valueOf(d);        if (result.indexOf(".") < result.length() - stellen) {            result = result.substring(0, result.indexOf(".") + stellen + 1);        } else if (result.indexOf(".") >= result.length() - stellen) {            result = result + "0";        }        return result;    }    public static final String PROPERTY_NAME_ENG = "ArcheOLogic";    public static final String PROPERTY_NAME_GER = "ArcheOLogic";        public static String PROPERTY_NAME = PROPERTY_NAME_ENG;        public static String PROGRAM_NAME = "=== "+PROPERTY_NAME+" ===";        static {		Constants.REGION = Locale.ENGLISH;		try {			Constants.REGION = Locale.getDefault();		} catch (Exception ex) {			Constants.REGION = Locale.ENGLISH;            Localization.getInstance().setLocale(Locale.ENGLISH);		}		Constants.setLanguage(Constants.REGION);	}        public static void setLanguage(final Locale region) {    	Constants.REGION = region;    	    	if ((region != null) && (region.equals(Locale.GERMAN))) {    		PROPERTY_NAME = PROPERTY_NAME_GER;    	} else {    		PROPERTY_NAME = PROPERTY_NAME_ENG;    	}		PROGRAM_NAME = "=== "+PROPERTY_NAME+" ===";    }}