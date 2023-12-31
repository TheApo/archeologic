package com.apogames.entity;

import com.apogames.Constants;
import com.apogames.backend.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class ApoButtonSolutionPresent extends ApoButton {

	public static enum PRESENT {
		CIRCLE,
		PRESENT
	};
	
	private final float[] COLOR_GOLD = new float[] {182f/255f, 155f/255f, 84f/255f, 1f};
	private float[][] colors;
	private int curColor = 0;
	private PRESENT present;
	
	public ApoButtonSolutionPresent(int x, int y, int width, int height, String function, String text, float[][] colors) {
		super(x, y, width, height, function, text);
		
		this.colors = colors;
		curColor = 0;
		this.present = PRESENT.CIRCLE;
	}

	public PRESENT getPresent() {
		return present;
	}

	public void setPresent(PRESENT present) {
		this.present = present;
	}

	public void addColor() {
		this.curColor += 1;
		if (curColor >= colors.length) {
			curColor = 0;
		}
	}
	
	public int getCurColor() {
		return curColor;
	}

	public void setCurColor(int curColor) {
		this.curColor = curColor;
	}

	public void render(GameScreen screen, int changeX, int changeY, boolean bShowTextOnly ) {
		if (this.isVisible()) {
			float rem = 0;
			if (getStroke() > 1) {
				rem = getStroke()/2f;
			}
			
			if (present.equals(PRESENT.CIRCLE)) {
				screen.getRenderer().begin(ShapeType.Filled);
				screen.getRenderer().setColor(colors[curColor][0], colors[curColor][1], colors[curColor][2], 1f);
				screen.getRenderer().ellipse(this.getX() + rem + changeX, this.getY() + rem + changeY, (this.getWidth() - 1 - rem*2), (this.getHeight() - 1 - rem*2));
				screen.getRenderer().end();
				
				screen.getRenderer().begin(ShapeType.Line);
				screen.getRenderer().setColor(Constants.COLOR_WHITE[0], Constants.COLOR_WHITE[1], Constants.COLOR_WHITE[2], 1f);
				if (this.isBPressed()) {
					screen.getRenderer().setColor(255f/ 255.0f, 0f / 255.0f, 0f / 255.0f, 1f);
				} else if (this.isBOver()) {
					screen.getRenderer().setColor(255f/ 255.0f, 255f / 255.0f, 0f / 255.0f, 1f);
				}
				if (getStroke() > 1) {
					Gdx.gl20.glLineWidth(getStroke());
				}
				screen.getRenderer().ellipse(this.getX() + rem + changeX, this.getY() + rem + changeY, (this.getWidth() - 1 - rem*2), (this.getHeight() - 1 - rem*2));
				screen.getRenderer().end();
				Gdx.gl20.glLineWidth(1f);
			} else {
				screen.getRenderer().begin(ShapeType.Filled);
				screen.getRenderer().setColor(colors[curColor][0], colors[curColor][1], colors[curColor][2], 1f);
				screen.getRenderer().roundedRect(this.getX() + rem + changeX, this.getY() + rem + changeY, (this.getWidth() - 1 - rem*2), (this.getHeight() - 1 - rem*2), getRounded());
				
				float height = getHeight() * 0.2f;
				screen.getRenderer().setColor(COLOR_GOLD[0], COLOR_GOLD[1], COLOR_GOLD[2], 1f);
				screen.getRenderer().rect(this.getX() + rem + changeX, this.getY() + getHeight()/2 - height/2 + rem + changeY, (this.getWidth() - 1 - rem*2), (height - 1 - rem*2));
				screen.getRenderer().rect(this.getX() + getWidth()/2 - height/2 + rem + changeX, this.getY() + rem + changeY, (height - 1 - rem*2), (getHeight() - 1 - rem*2));
				screen.getRenderer().end();
				
				screen.getRenderer().begin(ShapeType.Line);
				screen.getRenderer().setColor(Constants.COLOR_WHITE[0], Constants.COLOR_WHITE[1], Constants.COLOR_WHITE[2], 1f);
				if (this.isBPressed()) {
					screen.getRenderer().setColor(255f/ 255.0f, 0f / 255.0f, 0f / 255.0f, 1f);
				} else if (this.isBOver()) {
					screen.getRenderer().setColor(255f/ 255.0f, 255f / 255.0f, 0f / 255.0f, 1f);
				}
				if (getStroke() > 1) {
					Gdx.gl20.glLineWidth(getStroke());
				}
				screen.getRenderer().roundedRectLine(this.getX() + rem + changeX, this.getY() + rem + changeY, (this.getWidth() - 1 - rem*2), (this.getHeight() - 1 - rem*2), getRounded());
				screen.getRenderer().end();
				Gdx.gl20.glLineWidth(1f);
			}
			
			drawString(screen, changeX + 1, changeY + 1, Constants.COLOR_BLACK);
			drawString(screen, changeX, changeY, Constants.COLOR_WHITE);
		}
	}

}
