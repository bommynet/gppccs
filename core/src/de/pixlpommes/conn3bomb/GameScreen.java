package de.pixlpommes.conn3bomb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * <p>
 * TODO: short class description.
 * </p>
 *
 * <p>
 * TODO: detailed class description.
 * </p>
 *
 * @author Thomas Borck
 */
public class GameScreen implements Screen {

	// LIBGDX
	/** batch to render everything on screen */
	private SpriteBatch _batch;

	/** standard 2D camera */
	private OrthographicCamera _cam;

	// GAME
	/** the game mainly happens in the arena */
	private Arena _arena;

	/** the inserter changes the arena randomly */
	private Inserter _insert;

	/** the player changes the arena controlled */
	private Player _player;

	/**
	 * Create simple game screen.
	 */
	public GameScreen() {

		// libGdx
		_cam = new OrthographicCamera();
		_batch = new SpriteBatch();

		// game objects
		_arena = new Arena();
		int x = -((Arena.COLS * Arena.TILESIZE) / 2);
		int y = -((Arena.ROWS * Arena.TILESIZE) / 2);
		_arena.setOffset(x, y);

		// inserter area is above the arena
		_insert = new Inserter(_arena);
		x = (int) (_arena.getOffsetX());
		y = (int) (_arena.getOffsetY() + Arena.ROWS * Arena.TILESIZE);
		_insert.setOffset(x, y);

		// player area is below the arena
		_player = new Player(_arena);
		x = (int) (_arena.getOffsetX());
		y = (int) (_arena.getOffsetY() - Arena.TILESIZE);
		_player.setOffset(x, y);
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
		// update logic
		_arena.update(delta);
		_insert.update(delta);
		_player.update(delta);

		// clear screen
		Gdx.gl.glClearColor(0.553f, 0.651f, 0.711f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// draw content
		_batch.begin();

		_arena.draw(_batch);
		_insert.draw(_batch);
		_player.draw(_batch);

		_batch.end();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		Gdx.app.log("BattleScreen",
				"resize called (" + width + "x" + height + ")");

		// update camera
		_cam.viewportWidth = width;
		_cam.viewportHeight = height;
		_cam.update();

		// update renderer
		_batch.setProjectionMatrix(_cam.combined);
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
