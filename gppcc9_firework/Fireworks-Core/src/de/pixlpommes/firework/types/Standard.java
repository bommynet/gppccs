package de.pixlpommes.firework.types;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import de.pixlpommes.firework.stages.RandomFirework;

/**
 * <p>A standard fireworks.</p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public class Standard implements Firework {

	/** particles to show after explosion */
	protected final int PARTICLES = 100;
	
	/** the rocket */
	protected Particle _rocket;
	
	/** the particles */
	protected Particle[] _particles;
	
	/** flag to switch logic, if rocket explodes */
	protected boolean _isExploded;
	
	/** time to life after explosion */
	protected float _lifeSpan = 255;
	
	/** color values of this fireworks */
	protected float _r, _g, _b;
	
	/**
	 * A new fireworks.
	 */
	public Standard() {
		float min = 8f;
		float max = 16f; //20f;
		float speed = (float)(Math.random() * (max-min)) + min;
		
		_rocket = new Particle(2, (float)Math.random()*Gdx.graphics.getWidth(), 0, 0, speed);
		_isExploded = false;
		_r = (float)Math.random();
		_g = (float)Math.random();
		_b = (float)Math.random();
	}
	
	/**
	 * Rocket explodes in many particles.
	 */
	public void explode() {
		_particles = new Particle[PARTICLES];
		for(int i = 0; i < PARTICLES; i++) {
			// each particle has a random direction and speed
			Vector2 vel = new Vector2().setToRandomDirection();
			vel.scl((float) (Math.random()*4 + 2));
			
			// setup new particle
			_particles[i] = new Particle(1, _rocket.getX(), _rocket.getY(), vel.x, vel.y);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.pixlpommes.firework.types.Firework#update()
	 */
	@Override
	public void update() {
		// this part the rocket goes upwards
		if(!_isExploded) {
			// update rocket and slow it down
			_rocket.applyForce(RandomFirework.gravity);
			_rocket.update();
			
			// on zenith it will explode
			if(_rocket.getVelocity().y <= 0) {
				_isExploded = true;
				explode();
			}
		}
		// this part the rocket exploded and particles going downwards
		else if(_particles != null) {
			// each particle falls down and gets slower
			for(int i = 0; i < _particles.length; i++) {
				_particles[i].getVelocity().scl(0.9f);
				_particles[i].applyForce(RandomFirework.gravity);
				_particles[i].update();
			}
			
			// count life span down
			_lifeSpan -= 4;
			if(this._lifeSpan < 0)
				_lifeSpan = 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see de.pixlpommes.firework.types.Firework#draw(com.badlogic.gdx.graphics.glutils.ShapeRenderer)
	 */
	@Override
	public void draw(ShapeRenderer sr) {
		// set color to draw with
		sr.setColor(_r, _g, _b, _lifeSpan/255);
		
		// draw rocket...
		if(!_isExploded) {
			_rocket.draw(sr);
		}
		// ...or particles
		else if(_particles != null) {
			for(int i = 0; i < _particles.length; i++) {
				_particles[i].draw(sr);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.pixlpommes.firework.types.Firework#isDone()
	 */
	@Override
	public boolean isDone() {
		return _lifeSpan <= 0;
	}
}
