package de.pixlpommes.jam.screens.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * Draw a unit and all it's information.<br/>
 * This component shows the unit sprite, it's current/max health
 * and can be clicked.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class UnitPlate {

	private int _offsetX, _offsetY;
	
	private int _width, _height;
	
	private ProgressBar _health;
	
	/**
	 * 
	 */
	public UnitPlate() {
		_offsetX = 0;
		_offsetY = 0;
		_width   = 80;
		_height  = 80;
		
		_health = new ProgressBar(
				ProgressBar.FILE_HEALTHBAR,
				0f, 10f,
				_offsetX, _offsetY);
	}
	
	/**
	 * @param batch
	 */
	public void draw(Batch batch) {
		
		/// TODO remove border
		ShapeRenderer sr = new ShapeRenderer();
		sr.setProjectionMatrix(batch.getProjectionMatrix());
		sr.begin(ShapeType.Line);
		sr.setColor(1f, 0f, 0f, 1f);
		sr.rect(_offsetX, _offsetY, _width, _height);
		sr.end();
		
		_health.draw(batch);
	}
}
