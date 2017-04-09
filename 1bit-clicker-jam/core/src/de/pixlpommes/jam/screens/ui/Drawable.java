package de.pixlpommes.jam.screens.ui;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * A drawable component should include this interface.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 1.0
 */
public interface Drawable {

	/**
	 * Update components logic.
	 * @param delta
	 */
	public void update(float delta);
	
	/**
	 * Draw component.
	 * @param batch
	 */
	public void draw(Batch batch);
}
