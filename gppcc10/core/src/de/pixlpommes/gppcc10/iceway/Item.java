package de.pixlpommes.gppcc10.iceway;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Item {
	
	/** TODO: describe '_y' */
	private float _x, _y;
	
	private Type _type;
	
	
	/**
	 * @param x
	 * @param y
	 */
	public Item(float x, float y, Type type) {
		_x = x;
		_y = y;
		_type = type;
	}
	
	/**
	 * TODO: describe function
	 * @param deltaSpeed
	 */
	public void update(float deltaSpeed) {
		_y += deltaSpeed;
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
	 * @author Thomas Borck - http://www.pixlpommes.de
	 * @version 0.5
	 */
	public static enum Type {
		SNOWMAN;
	}
}
