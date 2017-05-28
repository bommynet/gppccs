package de.pixlpommes.conn3bomb;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * <p>
 * TODO: short class description.
 * </p>
 *
 * <p>
 * TODO: detailed class description.
 * </p>
 *
 * @author Thomas Borck
 */
public class Explosion {

	/** TODO: describe '_explosions' */
	private List<Element> _explosions;

	/** time between each frame */
	private float _maxDelay = 0.05f;

	/** frames per animation */
	private int _maxFrames = 7;

	public Explosion() {
		_explosions = new ArrayList<>();
	}

	/**
	 * TODO: describe function
	 * 
	 * @param batch
	 */
	public void draw(Batch batch) {
		_explosions.stream()
				.forEach(element -> Tiles.drawExplosion(batch, element.x, element.y, element.colorId, element.frame));
	}

	/**
	 * TODO: describe function
	 * 
	 * @param delta
	 */
	public void update(float delta) {
		// reverse iterate to avoid errors while removing elements
		for (int i = _explosions.size() - 1; i >= 0; i--) {
			_explosions.get(i).timer -= delta;

			if (_explosions.get(i).timer < 0) {
				_explosions.get(i).frame++;
				_explosions.get(i).timer = _maxDelay;

				if (_explosions.get(i).frame >= _maxFrames)
					_explosions.remove(i);
			}
		}
	}
	
	public void add(float x, float y, int colorId) {
		Element e = new Element();
		e.x = x;
		e.y = y;
		e.colorId = colorId;
		
		e.frame = 0;
		e.timer = _maxDelay;
		
		_explosions.add(e);
	}

	/**
	 * <p>
	 * TODO: short class description.
	 * </p>
	 *
	 * <p>
	 * TODO: detailed class description.
	 * </p>
	 *
	 * @author Thomas Borck
	 */
	private class Element {
		public float x;
		public float y;
		public int colorId;
		public int frame;
		public float timer;
	}
}
