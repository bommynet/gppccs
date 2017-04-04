package de.pixlpommes.firework.types;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import de.pixlpommes.firework.stages.RandomFirework;

/**
 * <p>A simple fireworks.</p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public class Simple extends Standard {

	/**
	 * A new simple fireworks.
	 */
	public Simple() {
		float min = 8f;
		float max = 20f;
		float speed = (float)(Math.random() * (max-min)) + min;
		
		Vector2 pos = new Vector2((float)Math.random()*Gdx.graphics.getWidth(), 0);
		Vector2 target = new Vector2(
				(float)Math.random()*Gdx.graphics.getWidth()/2f + Gdx.graphics.getWidth()/4f,
				Gdx.graphics.getHeight());
		
		Vector2 vel = new Vector2(target.x - pos.x, target.y - pos.y);
		vel.nor();
		vel.scl(speed);
		
		_rocket = new Particle(2, pos.x, pos.y, vel.x, vel.y);
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
			vel.scl((float) (Math.random()*5 + 1));
			
			if(_rocket.getVelocity().x < 0) {
				if(vel.x > 0) vel.x *= -1;
			} else {
				if(vel.x < 0) vel.x *= -1;
			}
			
			// setup new particle
			_particles[i] = new Particle(1, _rocket.getX(), _rocket.getY(), _rocket.getVelocity().x + vel.x, vel.y);
		}
	}

	/* (non-Javadoc)
	 * @see de.pixlpommes.firework.types.Standard#update()
	 */
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
				_particles[i].getVelocity().scl(0.92f, 0.87f);
				_particles[i].applyForce(RandomFirework.gravity);
				_particles[i].update();
			}
			
			// count life span down
			_lifeSpan -= 4;
			if(this._lifeSpan < 0)
				_lifeSpan = 0;
		}
	}
}
