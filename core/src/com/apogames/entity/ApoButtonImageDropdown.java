package com.apogames.entity;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.DrawString;
import com.apogames.backend.GameScreen;
import com.apogames.game.archeologic.GameTile;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

/**
 * The type Apo button image.
 */
public class ApoButtonImageDropdown extends ApoButtonImageWithThree {

	private int orgHeight = 0;

	private ArrayList<GameTile> curTiles;

	private int curTileChose;

    public ApoButtonImageDropdown(int x, int y, int width, int height, String function, String text, TextureRegion[] images) {
		super(x, y, width, height, function, text, images);

		this.orgHeight = height;
		this.curTiles = new ArrayList<>();
	}

	public void setCurTiles(ArrayList<GameTile> curTiles) {
		this.curTiles = new ArrayList<>(curTiles);
	}

	public int getTileNumber() {
		return this.curTiles.get(this.curTileChose).getTile().getTileNumber();
	}

	public byte[][] getTileArray() {
		return this.curTiles.get(this.curTileChose).getTile().getPossibilities().get(0);
	}

	public void setSelect(boolean select) {
		super.setSelect(select);
		if (select) {
			this.setHeight(curTiles.size() * this.orgHeight);
		} else {
			this.setHeight(this.orgHeight);
		}
	}

	public boolean getReleased( int x, int y ) {
		boolean result = super.getReleased(x, y);
		if (this.isSelect() && result) {
			this.curTileChose = (int)((y-this.getY())/this.orgHeight);
			this.setSelect(false);
		} else if (this.isSelect()) {
			this.setSelect(false);
		} else if (!this.isSelect() && result) {
			this.setSelect(true);
		}
		return result;
	}

	public void render(GameScreen screen, int changeX, int changeY ) {
		render(screen, changeX, changeY, true);
	}

	public void render(GameScreen screen, int changeX, int changeY, boolean needNewSpriteBatch) {
		if (this.isVisible()) {
			if (needNewSpriteBatch) {
				screen.spriteBatch.begin();
				//screen.spriteBatch.enableBlending();
			}
			renderImage(screen, changeX, changeY);

			if (this.isSelect()) {
				for (int i = 1; i < this.curTiles.size(); i++) {
					renderImage(screen, changeX, changeY + i * orgHeight);
				}
			}
			if (needNewSpriteBatch) {
				screen.spriteBatch.end();
			}
			renderOutline(screen, changeX, changeY);
		}
	}

	public void renderImage(GameScreen screen, int changeX, int changeY) {
		if (this.isBPressed() || this.isSelect()) {
			screen.spriteBatch.draw(this.getImages()[2], this.getX() + changeX, this.getY() + changeY, getWidth(), orgHeight);
		} else if (this.isBOver()) {
			screen.spriteBatch.draw(this.getImages()[1], this.getX() + changeX, this.getY() + changeY, getWidth(), orgHeight);
		} else {
			screen.spriteBatch.draw(this.getImages()[0], this.getX() + changeX, this.getY() + changeY, getWidth(), orgHeight);
		}
	}

	public void renderOutline(GameScreen screen, int changeX, int changeY) {
		screen.getRenderer().begin(ShapeRenderer.ShapeType.Filled);
		screen.getRenderer().setColor(0f, 0f, 0f, 1f);
		int size = 6;
		int addY = 0;
		if (!this.isSelect() && !this.curTiles.isEmpty()) {
			byte[][] bytes = this.curTiles.get(this.curTileChose).getTile().getPossibilities().get(0);
			renderTile(screen, changeX, changeY, bytes, size, addY);
		} else {
			for (GameTile tile : this.curTiles) {
				byte[][] bytes = tile.getTile().getPossibilities().get(0);
				renderTile(screen, changeX, changeY, bytes, size, addY);
				addY += orgHeight;
			}
		}
		screen.getRenderer().end();
	}

	private void renderTile(GameScreen screen, int changeX, int changeY, byte[][] bytes, int size, int addY) {
		int startY = (int)(this.getY() + orgHeight/2 + changeY - bytes.length*size/2 + addY);
		for (int y = 0; y < bytes.length; y++) {
			for (int x = 0; x < bytes[0].length; x++) {
				if (bytes[y][x] != 0) {
					screen.getRenderer().rect(this.getX() + changeX + 10 + 1 + x * size, startY + 1 + y * size, size - 2, size - 2);
				}
			}
		}
	}
}
