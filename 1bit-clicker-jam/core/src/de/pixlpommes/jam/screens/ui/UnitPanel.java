package de.pixlpommes.jam.screens.ui;

import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import de.pixlpommes.jam.units.base.Unit;

/**
 * Draw a unit and all it's information.<br/>
 * This component shows the unit sprite, it's current/max health
 * and can be clicked.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class UnitPanel implements Observer {

	private int _offsetX, _offsetY;
	
	private int _width, _height;
	
	private ProgressBar _health;
	
	/**
	 * 
	 */
	public UnitPanel(float offsetX, float offsetY) {
		_offsetX = (int)offsetX;
		_offsetY = (int)offsetY;
		_width   = 80;
		_height  = 80;
		
		_health = new ProgressBar(
				ProgressBar.FILE_HEALTHBAR,
				0f, 0f,
				_offsetX, _offsetY - 9);
	}
	
	/**
	 * Reset data to 0.
	 */
	public void reset() {
		_health.setValues(0f, 0f, 0f);
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
		
		if(_health.getValueMax() > 0) {
			_health.draw(batch);
		}
	}

	/**
	 * @param obs
	 * @param o
	 */
	@Override
	public void update(Observable obs, Object o) {
		// check for type
		if(obs == null || !(obs instanceof Unit)) return;
		
		Unit unit = (Unit)obs;
		_health.setValues(
				0.0f,
				unit.getHpMax(),
				unit.getHpCurrent());
	}
}
