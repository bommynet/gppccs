package de.pixlpommes.firework.types;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * <p>Fireworks interface.</p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public interface Firework {

	/**
	 * Update positions.
	 */
	void update();

	/**
	 * Draw rocket or particles.
	 * @param sr
	 */
	void draw(ShapeRenderer sr);

	/**
	 * @return if fireworks can be removed
	 */
	boolean isDone();

}