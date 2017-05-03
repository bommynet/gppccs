package de.pixlpommes.gppcc10.iceway;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * The player.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.5
 */
public class Player {
	
	/** TODO: describe 'ATLAS_SPEED' */
	public final static TextureAtlas ATLAS_PLAYER = new TextureAtlas(Gdx.files.internal("graphics/banana.atlas"));
	
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
	
	
	/** TODO: describe '_region' */
	private TextureRegion _region;
	
	/** TODO: describe '_aniDelay' */
	private float _aniTimer = 0.1f;
	private float _aniDelay = 0.1f;
	
	/** TODO: describe '_maxFrame' */
	private int _frame = 0;
	private int _frameCount = 24;
	
	
	/**
	 * Create new player.
	 * 
	 * @param x
	 * @param y
	 */
	public Player() {
		_state = State.RUN;
		
		_region = ATLAS_PLAYER.findRegion("banana_fall", 0);
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
		switch(_state) {
			case FALL: // play falling animation
				batch.draw(_region,	_x, _y);
				break;
				
			default: // player is on 2. col, 2. row, 64w, 128h
				batch.draw(Iceway.TILESET,
						_x, _y, // position on screen
						64, 64, // position on tile set
						64, 128); // size on tile set
				break;
		}
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
				if(_aniTimer > 0)
					_aniTimer -= delta;
				else {
					_aniTimer = _aniDelay;
					
					_frame++;
					if(_frame >= _frameCount) {
						_frame = _frameCount-1;
						_state = State.DEATH;
					}
					
					_region = ATLAS_PLAYER.findRegion("banana_fall", _frame);
				}
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
	 * @return current y of players position
	 */
	public float getY() {
		return _y;
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
	 * TODO: describe function
	 * @return
	 */
	public Rectangle getBounds() {
		return new Rectangle(_x, _y, 64, 64); /// TODO magic numbers
	}
	
	/**
	 * Get player sprite's center.
	 * @return
	 */
	public Vector2 getCenterPoint() {
		return new Vector2(_x + 32, _y + 32); /// TODO magic numbers
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
		JUMP,
		
		DEATH;
	}
}
