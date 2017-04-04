package de.pixlpommes.firework.types;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * <p>A particle.</p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public class Particle {

	/** position on screen */
	private Vector2 _pos;
	
	/** current velocity */
	private Vector2 _vel;
	
	/** current acceleration */
	private Vector2 _acc;
	
	/** particle size as radius */
	private float _radius;
	
	/**
	 * Create new particle.
	 * @param radius
	 * @param x
	 * @param y
	 */
	public Particle(int radius, float x, float y) {
		this(radius, x, y, 0, 0);
	}
	
	/**
	 * Create new particle and give it a initial velocity.
	 * @param radius
	 * @param x
	 * @param y
	 * @param veloX
	 * @param veloY
	 */
	public Particle(int radius, float x, float y, float veloX, float veloY) {
		_pos = new Vector2(x, y);
		_vel = new Vector2(veloX, veloY);
		_acc = new Vector2(0, 0);
		_radius = radius;
	}
	
	/**
	 * Apply a force to the particle.
	 * @param force
	 */
	public void applyForce(Vector2 force) {
		_acc.add(force);
	}
	
	/**
	 * @return current velocity of particle
	 */
	public Vector2 getVelocity() {
		return _vel;
	}
	
	/**
	 * @return current x component of particle's position
	 */
	public float getX() {
		return _pos.x;
	}
	
	/**
	 * @return current y component of particle's position
	 */
	public float getY() {
		return _pos.y;
	}
	
	/**
	 * TODO: describe behavior
	 * @return
	 */
	public float getRadius() {
		return _radius;
	}
	
	public void setRadius(float radius) {
		_radius = radius;
	}
	
	/**
	 * Update logic.
	 */
	public void update() {
		_vel.add(_acc);
		_pos.add(_vel);
		_acc.set(0, 0);
	}
	
	/**
	 * Draw particle.
	 * @param sr
	 */
	public void draw(ShapeRenderer sr) {
		sr.circle(_pos.x, _pos.y, _radius);
	}
}
