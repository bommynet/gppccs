package de.pixlpommes.conn3bomb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pixlpommes.conn3bomb.GameApp;
import de.pixlpommes.conn3bomb.Tiles;
import de.pixlpommes.conn3bomb.game.Arena;
import de.pixlpommes.conn3bomb.game.Inserter;
import de.pixlpommes.conn3bomb.game.Player;

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
	/** the game app */
	private final GameApp _app;

	// GAME
	/** the game mainly happens in the arena */
	private Arena _arena;

	/** the inserter changes the arena randomly */
	private Inserter _insert;

	/** the player changes the arena controlled */
	private Player _player;

	/**
	 * Create simple game screen.
	 * 
	 * @param app
	 *            the one and only game app
	 */
	public GameScreen(final GameApp app) {
		// the game application
		_app = app;

		// game objects
		int x = -((Arena.COLS * Tiles.TILESIZE) / 2);
		int y = -((Arena.ROWS * Tiles.TILESIZE) / 2);
		_arena = new Arena(app, x, y);

		// inserter area is above the arena
		_insert = new Inserter(_arena);
		x = (int) (_arena.getOffsetX());
		y = (int) (_arena.getOffsetY() + Arena.ROWS * Tiles.TILESIZE);
		_insert.setOffset(x, y);

		// player area is below the arena
		_player = new Player(_arena);
		x = (int) (_arena.getOffsetX());
		y = (int) (_arena.getOffsetY() - Tiles.TILESIZE);
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
		_app.batch.begin();

		_arena.draw(_app.batch);
		_insert.draw(_app.batch);
		_player.draw(_app.batch);

		_app.batch.end();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		Gdx.app.log("BattleScreen", "resize called (" + width + "x" + height + ")");

		// update camera
		_app.camera.viewportWidth = width;
		_app.camera.viewportHeight = height;
		_app.camera.update();

		// update renderer
		_app.batch.setProjectionMatrix(_app.camera.combined);
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
