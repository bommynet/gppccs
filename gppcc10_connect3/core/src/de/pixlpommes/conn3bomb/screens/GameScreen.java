package de.pixlpommes.conn3bomb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import de.pixlpommes.conn3bomb.GameApp;
import de.pixlpommes.conn3bomb.Tiles;
import de.pixlpommes.conn3bomb.game.Arena;
import de.pixlpommes.conn3bomb.game.Inserter;
import de.pixlpommes.conn3bomb.game.Player;

/**
 * <p>
 * Game screen.
 * </p>
 *
 * @author Thomas Borck - http://www.pixlpommes.de
 */
public class GameScreen extends ScreenAdapter {

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
		super(app);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		// game objects
		int x = -((Arena.COLS * Tiles.TILESIZE) / 2);
		int y = -((Arena.ROWS * Tiles.TILESIZE) / 2);
		_arena = new Arena(_app, x, y);

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
	 * @see de.pixlpommes.conn3bomb.screens.ScreenAdapter#draw()
	 */
	public void draw() {
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
	 * @see de.pixlpommes.conn3bomb.screens.ScreenAdapter#update(float)
	 */
	public void update(float delta) {
		// update logic
		_arena.update(delta);
		_insert.update(delta);
		_player.update(delta);
	}
}
