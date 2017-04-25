package de.pixlpommes.gppcc10.iceway;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

import de.pixlpommes.gppcc10.Gppcc10;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Item {
	
	/** TODO: describe '_y' */
	private float _x, _y;
	
	/** TODO: describe '_type' */
	private Type _type;
	
	/** TODO: describe '_isAlive' */
	private boolean _isAlive;
	
	
	/**
	 * @param x
	 * @param y
	 */
	public Item(float x, float y, Type type) {
		_x = x;
		_y = y;
		_type = type;
		_isAlive = true;
	}
	
	/**
	 * TODO: describe function
	 * @param deltaSpeed
	 */
	public void update(float deltaSpeed) {
		_y += deltaSpeed;
		
		// remove items out of screen
		if(_y < -Gppcc10.HALF_HEIGHT - 2*Iceway.TILESIZE) {
			kill();
		}
	}
	
	/**
	 * TODO: describe function
	 * @param batch
	 */
	public void draw(Batch batch) {
		switch(_type) {
			case SNOWMAN:
				batch.draw(Iceway.TILESET,
						_x,
						_y,
						0, 64, // tile position in tile set
						64, 128); // tile size
				break;
		}
	}
	
	/**
	 * TODO: describe function
	 * @return
	 */
	public float getY() {
		return _y;
	}
	
	/**
	 * TODO: describe function
	 * @return
	 */
	public Rectangle getBounds() {
		return new Rectangle(_x, _y, 64, 64);
	}
	
	/**
	 * Kill this item.
	 */
	public void kill() {
		/// TODO: init death animation
		_isAlive = false;
	}
	
	/**
	 * @return Is item killed and animation done?
	 */
	public boolean isRemovable() {
		/// TODO: wait 'til animation is done
		return !_isAlive;
	}
	
	
	/**
	 * @author Thomas Borck - http://www.pixlpommes.de
	 * @version 0.5
	 */
	public static enum Type {
		SNOWMAN;
	}
}
