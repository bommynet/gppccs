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
public class Timing implements Firework {

	public static final int PERFECT = 0,
							GOOD = 1,
							BAD = 2,
							ARRAYLENGTH = 3;
	
	/** particles to show for each explosion */
	protected final int PARTICLES = 100;
	
	/** the rocket */
	protected Particle _rocket;
	
	/** the particles */
	protected Particle[][] _particles;
	
	/** flag to switch logic, if rocket explodes */
	protected boolean _isExploded;
	
	/** time to life after explosion */
	protected float _lifeSpan = 255;
	
	/** color values of this fireworks */
	protected float[] _r, _g, _b;
	
	/**
	 * Gravitation for this rocket.
	 * <p>Just to get more control over rocket flying time and height.</p>
	 */
	protected Vector2 _gravity = new Vector2(0f, -0.2f);
	
	/**
	 * A new game.
	 */
	public Timing() {
		float min = 8f;
		float max = 16f; //20f;
		float speed = (float)(Math.random() * (max-min)) + min;
		
		_rocket = new Particle(2, (float)Math.random()*Gdx.graphics.getWidth(), 0, 0, speed);
		_isExploded = false;
		
		_r = new float[]{(float)Math.random(), (float)Math.random(), (float)Math.random()};
		_g = new float[]{(float)Math.random(), (float)Math.random(), (float)Math.random()};
		_b = new float[]{(float)Math.random(), (float)Math.random(), (float)Math.random()};
		
		_particles = new Particle[ARRAYLENGTH][];
	}
	
	/**
	 * Rocket explodes in many particles.
	 * @param timed
	 */
	private void explode(int timed) {
		// if timed is PERFECT -> 3 explosions
		// if timed is GOOD    -> 2 explosions
		// if timed is BAD     -> 1 explosion
		// else ??? TODO: *poof* smoke?
		switch(timed) {
			case PERFECT:
				_particles[PERFECT] = new Particle[PARTICLES];
				for(int i = 0; i < PARTICLES; i++) {
					// each particle has a random direction and speed
					Vector2 vel = new Vector2().setToRandomDirection();
					vel.scl((float) (Math.random()*4 + 2));
					
					// setup new particle
					_particles[PERFECT][i] = new Particle(1,
							_rocket.getX(), _rocket.getY(), vel.x, vel.y);
				}
				
			case GOOD:
				_particles[GOOD] = new Particle[PARTICLES];
				for(int i = 0; i < PARTICLES; i++) {
					// each particle has a random direction and speed
					Vector2 vel = new Vector2().setToRandomDirection();
					vel.scl((float) (Math.random()*4 + 2));
					
					// setup new particle
					_particles[GOOD][i] = new Particle(1,
							_rocket.getX(), _rocket.getY(), vel.x, vel.y);
				}
				
			case BAD:
				_particles[BAD] = new Particle[PARTICLES];
				for(int i = 0; i < PARTICLES; i++) {
					// each particle has a random direction and speed
					Vector2 vel = new Vector2().setToRandomDirection();
					vel.scl((float) (Math.random()*4 + 2));
					
					// setup new particle
					_particles[BAD][i] = new Particle(1,
							_rocket.getX(), _rocket.getY(), vel.x, vel.y);
				}
		}
		
		_isExploded = true;
	}
	
	/* (non-Javadoc)
	 * @see de.pixlpommes.firework.types.Firework#update()
	 */
	@Override
	public void update() {
		// this part the rocket goes upwards
		if(!_isExploded) {
			// update rocket and slow it down
			_rocket.applyForce(_gravity);
			_rocket.update();
		}
		// this part the rocket exploded and particles going downwards
		else if(_particles != null) {
			// each particle falls down and gets slower
			for(int i = 0; i < _particles.length; i++) {
				if(_particles[i] != null) {
					// each particle falls down and gets slower
					for(int j = 0; j < _particles[i].length; j++) {
						_particles[i][j].getVelocity().scl(0.9f);
						_particles[i][j].applyForce(RandomFirework.gravity);
						_particles[i][j].update();
					}
				}
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
		
		
		// draw rocket...
		if(!_isExploded) {
			_rocket.draw(sr);
		}
		// ...or particles
		else if(_particles != null) {
			for(int i = 0; i < _particles.length; i++) {
				// draw existing particles only
				if(_particles[i] == null) continue;
				
				// set color to draw with
				sr.setColor(_r[i], _g[i], _b[i], _lifeSpan/255);
				
				// draw particles
				for(int j = 0; j < _particles[i].length; j++) {
					_particles[i][j].draw(sr);
				}
			}
		}
	}
	
	/**
	 * TODO: describe behavior
	 */
	public void doExplosion() {
		explode(PERFECT);
	}
	
	/* (non-Javadoc)
	 * @see de.pixlpommes.firework.types.Firework#isDone()
	 */
	@Override
	public boolean isDone() {
		return _lifeSpan <= 0;
	}
}
