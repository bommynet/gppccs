package de.pixlpommes.firework;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.pixlpommes.firework.stages.GameFPS;
import de.pixlpommes.firework.stages.GameTiming;
import de.pixlpommes.firework.stages.Highscores;
import de.pixlpommes.firework.stages.Title;

/**
 * 
 * 
 * This class was created as part of my contribution to the #gppcc9.
 * @author Thomas Borck
 */
public class GoFirework extends ApplicationAdapter {
	
//	private final Color _night = new Color(
//			  0f / 255f,
//			  6f / 255f,
//			 18f / 255f,
//			0.2f);
	
	private Color _black = new Color(0, 0, 0, 0.2f);
	
	/** currently running stage */
	private Stage _stage;
	
	/** something to render all the particles */
	private ShapeRenderer _sr;
	
	/** flag to allow clearing screen once */
	private boolean _isNotInitialCleared = true;
	
	/** TODO: the _highscore */
	private Highscore _highscore;

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationAdapter#create()
	 */
	@Override
	public void create() {
		_sr = new ShapeRenderer();
		_highscore = new Highscore();
		_stage = new Title();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationAdapter#render()
	 */
	@Override
	public void render() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		// clear screen with solid black once in the beginning
		if(_isNotInitialCleared) {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			_isNotInitialCleared = false;
		}
		
		_sr.begin(ShapeType.Filled);
		_sr.setColor(_black);
		_sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		_sr.end();
		
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			if(_stage instanceof Title) {
				_stage.dispose();
				_stage = new GameTiming(); //new GameFPS();
			} else if(_stage instanceof GameFPS) {
				boolean gameover = ((GameFPS)_stage).pushedOneButton();
				if(gameover) {
					// save highscore
					Highscore.Entry entry = ((GameFPS)_stage).getEntry();
					int index = _highscore.add(entry);
					
					// switch stage
					_stage.dispose();
					_stage = new Highscores(_highscore, index);
				}
			} else if(_stage instanceof GameTiming) {
				boolean gameover = ((GameTiming)_stage).pushedOneButton();
				if(gameover) {
					// save highscore
					Highscore.Entry entry = ((GameTiming)_stage).getEntry();
					int index = _highscore.add(entry);
					
					// switch stage
					_stage.dispose();
					_stage = new Highscores(_highscore, index);
				}
			} else if(_stage instanceof Highscores) {
				boolean next = ((Highscores)_stage).pushedOneButton();
				if(next) {
					// switch stage
					_stage.dispose();
					_stage = new Title();
				}
			}
		}
		
		_stage.draw();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationAdapter#dispose()
	 */
	@Override
	public void dispose() {
		_stage.dispose();
		_sr.dispose();
	}
}
