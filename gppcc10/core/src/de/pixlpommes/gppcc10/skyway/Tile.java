package de.pixlpommes.gppcc10.skyway;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * A tile of the skyway.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Tile {

	/** TODO: describe '_tex' */
	private Texture _tex;

	/** TODO: describe '_isPassable' */
	private boolean _isPassable;
	
	/** TODO: describe '_isVisible' */
	private boolean _isVisible;

	/** TODO: describe '_x' */
	private float _x, _y;

	/**
	 * @param texture
	 * @param passable
	 * @param x
	 * @param y
	 */
	public Tile(Texture texture, boolean passable, float x, float y) {
		set(texture, passable, x, y);
	}

	/**
	 * @param texture
	 * @param passable
	 * @param x
	 * @param y
	 */
	public void set(Texture texture, boolean passable, float x, float y) {
		_tex = texture;
		_isPassable = passable;
		_isVisible = true;
		_x = x;
		_y = y;
	}
	
	/**
	 * @param tex
	 * @param y
	 * @param passable
	 * @param visible
	 */
	public void set(Texture tex, float y, boolean passable, boolean visible) {
		_tex = tex;
		_y = y;
		_isPassable = passable;
		_isVisible = visible;
	}

	/**
	 * Update position.
	 * 
	 * @param diffY
	 */
	public void updateScroll(float diffY) {
		_y += diffY;
	}

	/**
	 * Draw tile.
	 * 
	 * @param batch
	 */
	public void draw(Batch batch) {
		if(_isVisible)
			batch.draw(_tex, _x, _y);
		
		if(_isVisible && !_isPassable)
			batch.draw(Skyway.TILE_BLOCKED, _x, _y);
	}

	/**
	 * @return
	 */
	public Texture getTex() {
		return _tex;
	}

	/**
	 * @return
	 */
	public boolean isPassable() {
		return _isPassable;
	}
	
	/**
	 * @return
	 */
	public boolean isVisible() {
		return _isVisible;
	}

	/**
	 * @return
	 */
	public float getX() {
		return _x;
	}

	/**
	 * @return
	 */
	public float getY() {
		return _y;
	}
	
	/**
	 * Set new position.
	 * 
	 * @param y
	 */
	public void setY(float y) {
		_y = y;
	}

	/**
	 * @param passable
	 */
	public void setPassable(boolean passable) {
		_isPassable = false;
	}
}
