package de.pixlpommes.firework.types;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * <p>A standard fireworks that explodes in two colors.</p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public class DualColor extends Standard {

	/** particles to show after explosion */
	protected final int PARTICLES_HALF = PARTICLES / 2;
	
	/** second color values of this fireworks */
	protected float _r2, _g2, _b2;
	
	/**
	 * A new fireworks with two colors.
	 */
	public DualColor() {
		// create standard firework
		super();
		
		// add second color
		_r2 = 1f - _r;
		_g2 = 1f - _g;
		_b2 = 1f - _b;
	}
	
	/**
	 * Draw rocket or particles.
	 * @param sr
	 */
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
				
				// change color to second one on half
				if(i == PARTICLES_HALF) {
					// set second color to draw with
					sr.setColor(_r2, _g2, _b2, _lifeSpan/255);
				}
			}
		}
	}
}
