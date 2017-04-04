package de.pixlpommes.firework.stages;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.pixlpommes.firework.Config;
import de.pixlpommes.firework.font.RetroFont;
import de.pixlpommes.firework.types.Cracker;
import de.pixlpommes.firework.types.DualColor;
import de.pixlpommes.firework.types.Firework;
import de.pixlpommes.firework.types.Simple;
import de.pixlpommes.firework.types.Standard;

/**
 * <p>Title screen.</p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public class Title extends Stage {
	
	/** the fireworks */
	private List<Firework> _fireworks;
	
	/** renderer for particles */
	private ShapeRenderer _sr;
	
	/** renderer for images */
	private SpriteBatch _sb;
	
	/** simple bitmap font */
	private RetroFont _rf;
	
	/** flashing text maximum timer */
	private float _flashDelay = 0.75f;
	
	/** flashing text timer */
	private float _flashTimer = 0f;
	
	/** flashing text flag */
	private boolean _flashTextShow = true;
	
	
	/**
	 * Create stage.
	 */
	public Title() {
		_sr = new ShapeRenderer();
		_sb = new SpriteBatch();
		
		_fireworks = new ArrayList<Firework>();
		_rf = new RetroFont();
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.scenes.scene2d.Stage#draw()
	 */
	@Override
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
		
		// start drawing fireworks
		_sr = new ShapeRenderer();
		_sr.begin(ShapeType.Filled);
		
		// update and remove fireworks
		for(int i = 0; i < _fireworks.size(); i++) {
			_fireworks.get(i).update();
			_fireworks.get(i).draw(_sr);
			
			if(_fireworks.get(i).isDone()) {
				_fireworks.remove(i);
			}
		}
		
		// end drawing fireworks
		_sr.end();
		_sr.dispose();
		
		// update flashing text
		if(_flashTimer < _flashDelay) {
			_flashTimer += Gdx.graphics.getDeltaTime();
		} else {
			_flashTimer = 0f;
			_flashTextShow = !_flashTextShow;
		}
		
		// start drawing images
		_sb = new SpriteBatch();
		_sb.begin();
		
		int size = 70;
		_rf.draw(_sb,
				Config.TITLE,
				Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() * 2/3,
				size, size,
				true, true);
		_rf.draw(_sb,
				Config.SUBTITLE,
				Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() * 2/3 - size,
				size * 3/10, size * 3/10,
				true, true);
		
		if(_flashTextShow) {
			_rf.draw(_sb,
					"press SPACE to play",
					Gdx.graphics.getWidth() / 2,
					Gdx.graphics.getHeight() * 1/4,
					size * 2/10, size * 2/10,
					true, true);
		}
		
		// end drawing images
		_sb.end();
		_sb.dispose();
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.scenes.scene2d.Stage#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
	}
}
