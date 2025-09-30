package com.apogames.entity;

import com.apogames.Constants;
import com.apogames.backend.DrawString;
import com.apogames.backend.GameScreen;
import com.apogames.backend.ModalManager;
import com.apogames.common.Localization;
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

	private ArrayList<String> values;

	private int curTileChose;

    public ApoButtonImageDropdown(int x, int y, int width, int height, String function, String text, TextureRegion[] images) {
		super(x, y, width, height, function, text, images);

		this.orgHeight = height;
		this.curTiles = new ArrayList<>();
		this.values = new ArrayList<>();
	}

	public ArrayList<String> getValues() {
		return values;
	}

	public void setValues(ArrayList<String> values) {
		this.values = new ArrayList<>(values);
	}

	public void setCurTiles(ArrayList<GameTile> curTiles) {
		this.curTiles = new ArrayList<>(curTiles);
	}

	public int getTileNumber() {
		if (!this.values.isEmpty()) {
			return this.curTileChose;
		}
		return this.curTiles.get(this.curTileChose).getTile().getTileNumber();
	}

	public byte[][] getTileArray() {
		return this.curTiles.get(this.curTileChose).getTile().getPossibilities().get(0);
	}

	public void setSelect(boolean select) {
		boolean wasSelected = this.isSelect();
		super.setSelect(select);

		if (select && !wasSelected) {
			// Modal öffnen
			if (!this.curTiles.isEmpty()) {
				this.setHeight(curTiles.size() * this.orgHeight);
			} else if (!this.values.isEmpty()) {
				this.setHeight(this.values.size() * this.orgHeight);
			}
			ModalManager.openModal(this);
		} else if (!select && wasSelected) {
			// Modal schließen
			this.setHeight(this.orgHeight);
			ModalManager.closeModal(this);
		}
	}

	public boolean getReleased( int x, int y ) {
		boolean result = this.intersectsExpanded(x, y);

		if (this.isSelect() && result) {
			// Berechne welches Item geklickt wurde (auch obere Items)
			int clickedIndex = (int)((y - this.getY()) / this.orgHeight);

			// Validiere Index
			int maxIndex = 0;
			if (!this.curTiles.isEmpty()) {
				maxIndex = this.curTiles.size() - 1;
			} else if (!this.values.isEmpty()) {
				maxIndex = this.values.size() - 1;
			}

			if (clickedIndex >= 0 && clickedIndex <= maxIndex) {
				this.curTileChose = clickedIndex;
				this.setSelect(false);
				return true;
			} else {
				// Außerhalb geklickt - Modal schließen
				this.setSelect(false);
				return false;
			}
		} else if (this.isSelect() && !result) {
			// Außerhalb des erweiterten Bereichs geklickt - Modal schließen
			this.setSelect(false);
			return false;
		} else if (!this.isSelect() && this.intersects(x, y)) {
			// Normaler Button-Bereich geklickt - Modal öffnen
			this.setSelect(true);
			return true;
		}
		return false;
	}

	/**
	 * Prüft Kollision mit dem erweiterten Dropdown-Bereich
	 */
	private boolean intersectsExpanded(int x, int y) {
		if (!this.isVisible()) {
			return false;
		}

		int expandedHeight = (int)this.getHeight(); // Bereits erweiterte Höhe durch setSelect()

		return x >= this.getX() &&
			   x <= this.getX() + this.getWidth() &&
			   y >= this.getY() &&
			   y <= this.getY() + expandedHeight;
	}

	/**
	 * Gibt den Index des gehovered Items zurück, oder -1 wenn nicht gehovered
	 */
	public int getHoveredItemIndex(int x, int y) {
		if (!this.isVisible() || !this.isSelect()) {
			return -1;
		}

		if (!intersectsExpanded(x, y)) {
			return -1;
		}

		// Berechne welches Item gehovered wird
		int hoveredIndex = (int)((y - this.getY()) / this.orgHeight);

		// Validiere Index
		int maxIndex = 0;
		if (!this.curTiles.isEmpty()) {
			maxIndex = this.curTiles.size() - 1;
		} else if (!this.values.isEmpty()) {
			maxIndex = this.values.size() - 1;
		}

		if (hoveredIndex >= 0 && hoveredIndex <= maxIndex) {
			return hoveredIndex;
		}

		return -1;
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
				// Rendere alle Dropdown-Items mit korrekter Hover-Detection
				for (int i = 1; i < this.curTiles.size(); i++) {
					renderImageAtIndex(screen, changeX, changeY, i);
				}
				for (int i = 1; i < this.values.size(); i++) {
					renderImageAtIndex(screen, changeX, changeY, i);
				}

				int addY = 0;
				for (String value : this.values) {
					String v = Localization.getInstance().getCommon().get(value);
					screen.drawString(v, this.getX() + changeX + this.getWidth() / 2f, addY + this.getY() + changeY + orgHeight / 2f, Constants.COLOR_BLACK, getFont(), DrawString.MIDDLE, true, false);

					addY += this.orgHeight;
				}
			} else if (!this.values.isEmpty()) {
				String v = Localization.getInstance().getCommon().get(this.values.get(curTileChose));
				screen.drawString(v, this.getX() + changeX + this.getWidth() / 2f, this.getY() + changeY + orgHeight / 2f, Constants.COLOR_BLACK, getFont(), DrawString.MIDDLE, true, false);
			}

			if (needNewSpriteBatch) {
				screen.spriteBatch.end();
			}
			renderOutline(screen, changeX, changeY);

			// Rendere gelbe Umrandung für gehovered Items
			if (this.isSelect()) {
				renderHoverBorders(screen, changeX, changeY);
			}
		}
	}

	public void renderImage(GameScreen screen, int changeX, int changeY) {
		renderImageAtIndex(screen, changeX, changeY, 0);
	}

	/**
	 * Rendert ein einzelnes Item an einem bestimmten Index
	 */
	private void renderImageAtIndex(GameScreen screen, int changeX, int changeY, int itemIndex) {
		int itemY = changeY + itemIndex * orgHeight;

		// Prüfe ob dieses spezifische Item gehovered ist
		boolean isItemHovered = ModalManager.isModalItemHovered(this, itemIndex);

		if (this.isBPressed() || this.isSelect()) {
			screen.spriteBatch.draw(this.getImages()[2], this.getX() + changeX, this.getY() + itemY, getWidth(), orgHeight);
		} else if (this.isBOver() || isItemHovered) {
			// Verwende Hover-State für das gehovered Item
			screen.spriteBatch.draw(this.getImages()[1], this.getX() + changeX, this.getY() + itemY, getWidth(), orgHeight);
		} else {
			screen.spriteBatch.draw(this.getImages()[0], this.getX() + changeX, this.getY() + itemY, getWidth(), orgHeight);
		}
	}

	public void renderOutline(GameScreen screen, int changeX, int changeY) {
		if (this.curTiles.isEmpty()) {
			return;
		}
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

	/**
	 * Rendert gelbe Umrandung für gehovered Modal-Items
	 */
	private void renderHoverBorders(GameScreen screen, int changeX, int changeY) {
		int hoveredIndex = ModalManager.getHoveredItemIndex();
		if (hoveredIndex < 0 || !ModalManager.isModalItemHovered(this, hoveredIndex)) {
			return;
		}

		// Gelbe Umrandung um das gehovered Item
		screen.getRenderer().begin(ShapeRenderer.ShapeType.Line);
		screen.getRenderer().setColor(Constants.COLOR_YELLOW[0], Constants.COLOR_YELLOW[1], Constants.COLOR_YELLOW[2], 1f);

		int itemY = (int)(this.getY() + changeY + hoveredIndex * orgHeight);
		int borderWidth = 2;

		// Äußere gelbe Umrandung
		screen.getRenderer().rect(
			this.getX() + changeX - borderWidth,
			itemY - borderWidth,
			this.getWidth() + 2 * borderWidth,
			orgHeight + 2 * borderWidth
		);

		screen.getRenderer().end();
	}
}
