package de.pixlpommes.obfflf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * 
 * 
 * This class was created as part of my contribution to the #gppcc9.
 * @author Thomas Borck
 */
public class OneButtonFFFight extends ApplicationAdapter {
	
	/** currently running stage */
	private Stage _stage;

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationAdapter#create()
	 */
	@Override
	public void create() {
		// TODO: should show Title/Menu first instead of Fight
		_stage = new Fight();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationAdapter#render()
	 */
	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		_stage.draw();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationAdapter#dispose()
	 */
	@Override
	public void dispose() {
		_stage.dispose();
	}
}
