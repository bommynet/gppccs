package de.pixlpommes.firework.stages;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.pixlpommes.firework.types.Cracker;
import de.pixlpommes.firework.types.DualColor;
import de.pixlpommes.firework.types.Firework;
import de.pixlpommes.firework.types.Simple;
import de.pixlpommes.firework.types.Standard;

/**
 * <p>Manage and show all fireworks.</p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public class RandomFirework extends Stage {
	
	/** global gravity for all objects */
	public final static Vector2 gravity = new Vector2(0, -0.2f);
	
	/** the fireworks */
	private List<Firework> _fireworks;
	
	/**
	 * Create stage.
	 */
	public RandomFirework() {
		_fireworks = new ArrayList<Firework>();
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.scenes.scene2d.Stage#draw()
	 */
	public void draw() {
		// spawn new random fireworks
		if(Math.random() < 0.15) {
			Standard newFw;
			
			double switchFw = Math.random() * 3.5;
			
			if(switchFw < 1.0) {
				newFw = new Standard();
			} else if (switchFw < 2.0) {
				newFw = new DualColor();
			} else if (switchFw < 3.0) {
				newFw = new Simple();
			} else {
				newFw = new Cracker();
			}
			
			_fireworks.add(newFw);
		}
		
		ShapeRenderer sr = new ShapeRenderer();
		sr.begin(ShapeType.Filled);
		
		// update and remove fireworks
		for(int i = 0; i < _fireworks.size(); i++) {
			_fireworks.get(i).update();
			_fireworks.get(i).draw(sr);
			
			if(_fireworks.get(i).isDone()) {
				_fireworks.remove(i);
			}
		}
		
		sr.end();
		sr.dispose();
	}
}
