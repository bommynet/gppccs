package de.pixlpommes.gppcc10;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * The player.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Player {

	/** TODO: describe '_tex' */
	private Texture _tex;
	
	/** TODO: describe '_x' */
	private float _x, _y;
	
	/** set true if player switches between columns */
	private boolean _isSwitching;
	
	/** TODO: describe 'switchingTime' */
	private float _switchingTime = 0.3f;
	
	private float _switchingTimeCurrent;

	private float _switchingTarget;

	private float _switchingStep;
	
	/**
	 * @param x
	 * @param y
	 */
	public Player(float x, float y) {
		_x = x;
		_y = y;
		
		_tex = new Texture(Gdx.files.internal("player.png"));
		_isSwitching = false;
	}
	
	/**
	 * @param batch
	 */
	public void draw(Batch batch) {
		batch.draw(_tex, _x, _y);
	}
	
	/**
	 * @param delta
	 */
	public void update(float delta) {
		if(_isSwitching) {
			_x += _switchingStep * delta;
			_switchingTimeCurrent -= delta;
			
			if(_switchingTimeCurrent <= 0) {
				_x = _switchingTarget;
				_isSwitching = false;
			}
		}
	}
	
	/**
	 * @param newPos
	 */
	public void switchPos(float newPos) {
		if(_isSwitching) return;
		
		_isSwitching = true;
		_switchingTarget = newPos;
		_switchingStep = (newPos - _x) / _switchingTime;
		_switchingTimeCurrent = _switchingTime;
	}
	
	/**
	 * @return
	 */
	public float getX() {
		return _x;
	}
}
