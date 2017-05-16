package de.pixlpommes.conn3bomb;

import com.badlogic.gdx.Screen;

/**
 * <p>TODO: short class description.</p>
 *
 * <p>TODO: detailed class description.</p>
 *
 * @author Thomas Borck
 */
public class GameScreen implements Screen {
	
	/** TODO: describe _arena */
	private Arena _arena;
	
	/** TODO: describe _insert */
	private Inserter _insert;
	
	/** TODO: describe _player */
	private Player _player;
	
	
	public GameScreen() {
		_arena = new Arena();
		_arena.setOffset(0, 0);
		
		_insert = new Inserter();
		_insert.setOffset(0, 10);
		
		_player = new Player();
		_player.setOffset(0, -10);
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
	}

}
