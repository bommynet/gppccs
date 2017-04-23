package de.pixlpommes.gppcc10.skyway;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * A tile of the skyway.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.3
 */
public class Tile {

	/** is this tile not blocked */
	private boolean _isPassable;
	
	/** is this tile visible */
	private boolean _isVisible;

	/** position of the tile */
	private float _x, _y;

	/**
	 * Create new tile.
	 * 
	 * @param passable
	 * @param x
	 * @param y
	 */
	public Tile(boolean passable, float x, float y) {
		set(passable, x, y);
	}

	/**
	 * Set new values.
	 * 
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
	 * Set new values.
	 * 
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
			batch.draw(Skyway.TILESET, _x, _y, 0, 0, 64, 64);
			//batch.draw(Skyway.TILE_NORMAL, _x, _y);
		
		if(_isVisible && !_isPassable)
			batch.draw(Skyway.TILE_BLOCKED, _x, _y);
	}

	/**
	 * @return true if tile is not blocked
	 */
	public boolean isPassable() {
		return _isPassable;
	}
	
	/**
	 * @return true if tile is visible
	 */
	public boolean isVisible() {
		return _isVisible;
	}

	/**
	 * @return position x
	 */
	public float getX() {
		return _x;
	}

	/**
	 * @return position y
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
	 * Set this tile as passable or not passable.
	 * 
	 * @param passable
	 */
	public void setPassable(boolean passable) {
		_isPassable = false;
	}
}
