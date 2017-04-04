package de.pixlpommes.obfflf;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

/**
 * <p>Player</p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public class Player {
	
	/** offset from bottom line (game input area) */
	private final int OFFSET = 80;
	
	/** point at which the sprite is drawn */
	private final Vector2 _playerPos;
	
	private final boolean _isPlayerTwo;

	/** players life */
	private int _hp, _hpMax;

	/** players image/sprite */
	private Texture _sprite;
	
	/**
	 * 
	 */
	public Player(boolean playerTwo) {
		_sprite = new Texture(Gdx.files.internal("player_idle_0.png"));
		if(!playerTwo)
			_playerPos = new Vector2(109 - _sprite.getWidth()/2, 16 + OFFSET);
		else
			_playerPos = new Vector2(74 - _sprite.getWidth()/2, 32 + OFFSET);
		
		_isPlayerTwo = playerTwo;
		
		_hp = _hpMax = 100;
	}
	
	
	/**
	 * TODO:
	 * @param value
	 */
	public void hit(int value) {
		// TODO: do something with HP
		// TODO: animate HP gain/loss
		System.out.println("Player:hit(" + value + ")");
	}
	
	/**
	 * TODO:
	 */
	public void buff() {
		// TODO: do something like 'add regen' or 'remove poison'
		// TODO: select regen or cure by player state
		// TODO: animate regen or cure
		System.out.println("Player:buff()");
	}
	
	/**
	 * TODO:
	 */
	public void debuff() {
		// TODO: do something like 'add poison'
		// TODO: animate poison
		System.out.println("Player:debuff()");
	}
	
	
	/**
	 * TODO: attack
	 */
	public void attack(Enemy target, int value) {
		// hit enemy with given value
		// TODO: PRECOND! value has to be negative
		target.hit(value);
	}
	
	/**
	 * TODO: heal
	 */
	public void heal(Player target, int value) {
		// hit enemy with given value
		// TODO: PRECOND! value has to be positive
		target.hit(value);
	}
	
	/**
	 * TODO: support
	 */
	public void support(Player target) {
		// buffs player
		target.buff();
	}
	
	
	
	/**
	 * TODO: describe behavior
	 * @param dt
	 */
	public void update(float dt) {
		
	}
	
	/**
	 * TODO: describe behavior
	 * @param batch
	 */
	public void draw(Batch batch) {
		batch.begin();
		batch.draw(_sprite, _playerPos.x, _playerPos.y);
		batch.end();
//		ShapeRenderer sr = new ShapeRenderer();
//		sr.begin(ShapeType.Filled);
//		sr.setColor(Color.BLUE);
//		sr.rect(25, 100, 75, 100);
//		sr.end();
//		sr.dispose();
	}
	

	
	/**
	 * Get current player life.
	 * @return
	 */
	public int getHp() {
		return _hp;
	}


	/**
	 * <p>Set current player life to a new value.</p>
	 * <p>Each negative hp-value will be set to zero.</p>
	 * @param hp new player life 
	 */
	public void setHp(int hp) {
		if(hp < 0)
			this._hp = 0;
		else
			this._hp = hp;
	}


	/**
	 * Get maximum player life.
	 * @return
	 */
	public int getHpMax() {
		return _hpMax;
	}


	/**
	 * <p>Set maximum player life to a new value.</p>
	 * <p>Each negative hp-value will be set to zero.</p>
	 * @param hp
	 */
	public void setHpMax(int hp) {
		if(hp < 0)
			this._hpMax = 0;
		else
			this._hpMax = hp;
	}
}
