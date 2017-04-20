package de.pixlpommes.gppcc10;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * The player.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.5
 */
public class Player {

	/** basic player texture */
	private Texture _tex;
	
	/** lower left corner of players position */
	private float _x, _y;
	
	/** current player state */
	private State _state;
	
	/** time needed for player to switch between two columns */
	private float _switchingTime = 0.3f;
	
	/** time currently elapsed */
	private float _switchingTimeCurrent;

	/** target x position for current switch */
	private float _switchingTarget;

	/** pixels to move per frame while switching */
	private float _switchingStep;
	
	/**
	 * Create new player.
	 * 
	 * @param x
	 * @param y
	 */
	public Player() {
		_tex = new Texture(Gdx.files.internal("player.png"));
		_state = State.RUN;
	}
	
	/**
	 * Set player to new position.
	 * 
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y) {
		_x = x;
		_y = y;
	}
	
	/**
	 * Draw player.
	 * 
	 * @param batch
	 */
	public void draw(Batch batch) {
		batch.draw(_tex, _x, _y);
	}
	
	/**
	 * Update player logic.
	 * 
	 * @param delta time elapsed since last update()
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
			case BLOCK: // blocked by ice cube
				System.out.println("block");
				_state = State.RUN;
				break;
			case RUN: // running on skyway
				break;
		}
	}
	
	/**
	 * Start switching to new position.
	 * 
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
	 * @return current x of players position
	 */
	public float getX() {
		return _x;
	}
	
	/**
	 * @return true if player is switching
	 */
	public boolean isSwitching() {
		return _state == State.JUMP;
	}
	
	/**
	 * Set new player state.
	 * 
	 * @param state
	 */
	public void changeState(State state) {
		_state = state;
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
		
		/** player was blocked */
		BLOCK,
		
		/** player jumping/switching between columns */
		JUMP;
	}
}
