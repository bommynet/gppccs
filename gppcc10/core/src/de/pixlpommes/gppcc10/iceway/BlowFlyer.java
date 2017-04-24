package de.pixlpommes.gppcc10.iceway;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * A swarm blow-flyers that will melt the iceway.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class BlowFlyer {

	/** TODO: describe '_sr' */
	private ShapeRenderer _sr;
	
	/** TODO: describe '_y' */
	private float _x, _y;
	
	/** TODO: describe '_speed' */
	private float _speed;
	
	
	
	/**
	 * @param x
	 * @param y
	 */
	public BlowFlyer(float x, float y) {
		_x = x;
		_y = y;
		_speed = 170;
		
		_sr = new ShapeRenderer();
	}
	
	/**
	 * TODO: describe function
	 * @param delta
	 * @param icewaySpeed
	 */
	public void update(float delta, float icewaySpeed) {
		float diffSpeed = _speed - icewaySpeed;
		_y += diffSpeed * delta;
	}
	
	/**
	 * TODO: describe function
	 * @param batch
	 */
	public void draw(Batch batch) {
		// draw flyers
		for(int i=0; i<Iceway.COLS; i++) {
			batch.draw(Iceway.TILESET,
				_x + i * Iceway.TILESIZE,
				_y - Iceway.TILESIZE - 3,
				0, 192, // tile position in tile set
				64, 64); // tile size
		}
		
		// draw melt border
		batch.end(); // switch renderer, so temporary stop batch
		_sr.setProjectionMatrix(batch.getProjectionMatrix());
		_sr.begin(ShapeType.Line);
		_sr.setColor(Color.RED);
		_sr.line(_x - Iceway.TILESIZE/2, _y,
				 _x + Iceway.TILESIZE/2 + Iceway.COLS*Iceway.TILESIZE, _y);
		_sr.end();
		batch.begin();
	}
	
	/**
	 * TODO: describe function
	 * @return
	 */
	public float getY() {
		return _y;
	}
}
