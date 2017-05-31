package de.pixlpommes.conn3bomb.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.pixlpommes.conn3bomb.Tiles;

/**
 * <p>
 * Explosion handler.
 * </p>
 *
 * @author Thomas Borck
 */
public class Explosion {

	/** all existing explosions */
	private List<Element> _explosions;

	/** time between each frame */
	private float _maxDelay = 0.05f;

	/** frames per animation */
	private int _maxFrames = 7;

	/**
	 * Instantiate explosion handler.
	 */
	public Explosion() {
		_explosions = new ArrayList<>();
	}

	/**
	 * Draw explosions.
	 * 
	 * @param batch
	 */
	public void draw(Batch batch, float offsetX, float offsetY) {
		_explosions.stream().forEach(element -> Tiles.drawExplosion(batch, element.x + offsetX, element.y + offsetY,
				element.colorId, element.frame));
	}

	/**
	 * Update explosions.
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

	/**
	 * Add a new explosion to handle.
	 * 
	 * @param x
	 *            screen x
	 * @param y
	 *            screen y
	 * @param colorId
	 *            id [0,3]
	 */
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
	 * Explosions as atom element.
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
