package de.pixlpommes.conn3bomb;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * <p>A basic screen object.</p>
 *
 * <p>Each drawable game class should inherit this class for basic function.</p>
 *
 * @author Thomas Borck
 */
public abstract class ScreenObject {
	
	/** lower left corner of this object */
	protected float _offsetX, _offsetY;
	
	/**
	 * Draw object on screen.
	 * 
	 * @param batch handles graphical functions
	 */
	public abstract void draw(Batch batch);
	
	/**
	 * Update objects logic.
	 * 
	 * @param delta time elapsed since last update
	 */
	public abstract void update(float delta);
	
	
	/**
	 * Set offset to position object on screen.
	 * 
	 * @param x
	 * @param y
	 */
	public void setOffset(float x, float y) {
		_offsetX = x;
		_offsetY = y;
	}
	
	/**
	 * Set offset for x to position object on screen.
	 * 
	 * @param x
	 */
	public void setOffsetX(float x) {
		_offsetX = x;
	}

	/**
	 * Set offset for y to position object on screen.
	 * 
	 * @param y
	 */
	public void setOffsetY(float y) {
		_offsetY = y;
	}
	
	/**
	 * @return x offset as position of this object on screen
	 */
	public float getOffsetX() {
		return _offsetX;
	}
	
	/**
	 * @return y offset as position of this object on screen
	 */
	public float getOffsetY() {
		return _offsetY;
	}
}
