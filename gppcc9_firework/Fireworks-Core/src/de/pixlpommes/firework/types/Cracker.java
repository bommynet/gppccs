package de.pixlpommes.firework.types;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * <p>A cannon cracker.</p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public class Cracker extends Standard {

	/** the cracker */
	private Particle _cracker;
	
	/**
	 * Create new cracker.
	 */
	public Cracker() {
		int radius = (int)(Math.random()*30 + 40);
		_cracker = new Particle(radius, (float)Math.random()*Gdx.graphics.getWidth(), 0);
	}
	
	/* (non-Javadoc)
	 * @see de.pixlpommes.firework.Firework#update()
	 */
	public void update() {
		_cracker.setRadius(_cracker.getRadius() - 10f);
	}
	
	/* (non-Javadoc)
	 * @see de.pixlpommes.firework.Firework#draw(com.badlogic.gdx.graphics.glutils.ShapeRenderer)
	 */
	public void draw(ShapeRenderer sr) {
		sr.setColor(Color.WHITE);
		_cracker.draw(sr);
	}
	
	/* (non-Javadoc)
	 * @see de.pixlpommes.firework.types.Standard#isDone()
	 */
	public boolean isDone() {
		return _cracker.getRadius() <= 0f;
	}
}
