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
	
	/** TODO: describe '_state' */
	private State _state;
	
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
		setPosition(x, y);
		
		_tex = new Texture(Gdx.files.internal("player.png"));
		_state = State.RUN;
	}
	
	/**
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y) {
		_x = x;
		_y = y;
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
		switch(_state) {
			case JUMP: // jumping between columns
				_x += _switchingStep * delta;
				_switchingTimeCurrent -= delta;
				
				if(_switchingTimeCurrent <= 0) {
					_x = _switchingTarget;
					_state = State.RUN;
				}
				break;
			case FALL: // falling from skyway
				System.out.println("fall");
				_state = State.RUN;
				break;
			case RUN: // running on skyway
				break;
		}
	}
	
	/**
	 * @param newPos
	 */
	public void switchPos(float newPos) {
		if(isSwitching()) return;
		
		_state = State.JUMP;
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
	
	/**
	 * @return true if player switches column currently
	 */
	public boolean isSwitching() {
		return _state == State.JUMP;
	}
	
	
	
	/**
	 * Define collision types for player.
	 * 
	 * @author Thomas Borck - http://www.pixlpommes.de
	 * @version 1.0
	 */
	public static enum Collide {
		/** player collides with skyway tile */
		TILE,
		
		/** player collides with skyway hole */
		HOLE,
		
		/** player collides with block */
		BLOCK,
		
		/** player collides with powerup */
		POWERUP;
	}
	
	/**
	 * Define player states.
	 * 
	 * @author Thomas Borck - http://www.pixlpommes.de
	 * @version 1.0
	 */
	public static enum State {
		/** player running */
		RUN,
		
		/** player falling from skyway */
		FALL,
		
		/** player jumping/switching between columns */
		JUMP;
	}
}
