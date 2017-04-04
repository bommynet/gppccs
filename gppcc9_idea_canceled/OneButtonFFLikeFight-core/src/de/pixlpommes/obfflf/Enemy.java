package de.pixlpommes.obfflf;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * <p>Enemy</p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public class Enemy {

	/** enemies life */
	private int _hp, _hpMax;
	
	/** enemies image/sprite */
	private Texture _sprite;
	
	/**
	 * 
	 */
	public Enemy() {
		_hp = _hpMax = 100;
		
	}
	
	
	/**
	 * TODO:
	 * @param value
	 */
	public void hit(int value) {
		// TODO: do something with HP
		// TODO: animate HP gain/loss
		System.out.println("Enemy:hit(" + value + ")");
	}
	
	/**
	 * TODO:
	 */
	public void buff() {
		// TODO: do something like 'add regen' or 'remove poison'
		// TODO: select regen or cure by player state
		// TODO: animate regen or cure
		System.out.println("Enemy:buff()");
	}
	
	/**
	 * TODO:
	 */
	public void debuff() {
		// TODO: do something like 'add poison'
		// TODO: animate poison
		System.out.println("Enemy:debuff()");
	}
	
	
	/**
	 * TODO: attack
	 */
	public void attack(Player target, int value) {
		// hit player with given value
		// TODO: PRECOND! value has to be negative
		target.hit(value);
	}
	
	/**
	 * TODO: heal
	 */
	public void heal(Enemy target, int value) {
		// hit enemy with given value
		// TODO: PRECOND! value has to be positive
		target.hit(value);
	}
	
	/**
	 * TODO: support
	 */
	public void support(Enemy target) {
		// buffs enemy
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
		ShapeRenderer sr = new ShapeRenderer();
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.RED);
		sr.rect(Config.WIDTH-150, 100, 125, 100);
		sr.end();
		sr.dispose();
	}
}