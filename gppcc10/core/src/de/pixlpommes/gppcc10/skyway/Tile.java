package de.pixlpommes.gppcc10.skyway;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * A tile of the skyway.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Tile {

	/** TODO: describe '_isPassable' */
	private boolean _isPassable;
	
	/** TODO: describe '_isVisible' */
	private boolean _isVisible;

	/** TODO: describe '_x' */
	private float _x, _y;

	/**
	 * @param passable
	 * @param x
	 * @param y
	 */
	public Tile(boolean passable, float x, float y) {
		set(passable, x, y);
	}

	/**
	 * @param passable
	 * @param x
	 * @param y
	 */
	public void set(boolean passable, float x, float y) {
		_isPassable = passable;
		_isVisible = true;
		_x = x;
		_y = y;
	}
	
	/**
	 * @param y
	 * @param passable
	 * @param visible
	 */
	public void set(float y, boolean passable, boolean visible) {
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
			batch.draw(Skyway.TILE_NORMAL, _x, _y);
		
		if(_isVisible && !_isPassable)
			batch.draw(Skyway.TILE_BLOCKED, _x, _y);
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
