package de.pixlpommes.conn3bomb.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import de.pixlpommes.conn3bomb.GameApp;
import de.pixlpommes.conn3bomb.Tiles;
import de.pixlpommes.conn3bomb.game.Arena;
import de.pixlpommes.conn3bomb.game.ArenaGui;
import de.pixlpommes.conn3bomb.game.GameInput;
import de.pixlpommes.conn3bomb.game.Inserter;
import de.pixlpommes.conn3bomb.game.Player;

/**
 * <p>
 * Game screen.
 * </p>
 *
 * @author Thomas Borck - http://www.pixlpommes.de
 */
public class GameScreen extends ScreenAdapter implements ScoreListener {

	// GAME
	/** the game mainly happens in the arena */
	private Arena _arena;

	/** the inserter changes the arena randomly */
	private Inserter _insert;

	/** the player changes the arena controlled */
	private Player _player;
	
	/** TODO: describe '_gui' */
	private ArenaGui _gui;

	/** player input handling */
	private GameInput _input;
	
	/** TODO: describe '_scoreAnimation' */
	private List<ScoreAnimation> _scoreAnimation;

	// TEXTURES
	/** TODO: describe '_texBackground' */
	private Texture _texConvoyerBand;

	/** TODO: describe '_texBackground' */
	private Texture _texBackground;

	/**
	 * Create simple game screen.
	 * 
	 * @param app
	 *            the one and only game app
	 */
	public GameScreen(final GameApp app) {
		super(app);

		_texConvoyerBand = _app.assets.get("graphics/convoyerband.png");
		_texBackground = _app.assets.get("graphics/arenabg.png");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		// for debug purposes only
		super.show();

		// game objects
		int x = -GameApp.HALF_WIDTH + 256; // -((Arena.COLS * Tiles.TILESIZE) / 2);
		int y = -GameApp.HALF_HEIGHT + 88; //-((Arena.ROWS * Tiles.TILESIZE) / 2);
		_arena = new Arena(_app, x, y);
		_arena.addScoreListener(this);

		// inserter area is above the arena
		_insert = new Inserter(_arena, _app.assets.get("tiles.png"), _app.assets.get("graphics/band.png"));
		x = (int) (_arena.getOffsetX());
		y = (int) (_arena.getOffsetY() + Arena.ROWS * Tiles.TILESIZE);
		_insert.setOffset(x, y);

		// player area is below the arena
		_player = new Player(_app.assets.get("tiles.png"));
		x = (int) (_arena.getOffsetX());
		y = (int) (_arena.getOffsetY() - Tiles.TILESIZE);
		_player.setOffset(x, y);
		
		// GUI
		_gui = new ArenaGui(_app);
		_gui.setOffset(GameApp.HALF_WIDTH - 225, -200); /// TODO positioning
		
		// score animator
		_scoreAnimation = new ArrayList<>();
		
		// user input
		_input = new GameInput(_arena, _player);
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

		// draw background
		_app.batch.draw(_texBackground, -GameApp.HALF_WIDTH, -GameApp.HALF_HEIGHT);
		_app.batch.draw(_texConvoyerBand, _arena.getOffsetX(), _arena.getOffsetY());

		_insert.draw(_app.batch);
		_arena.draw(_app.batch);
		_player.draw(_app.batch);
		_gui.draw(_app.batch);

		_scoreAnimation.stream().forEach(score -> score.draw(_app.batch));
		
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
		_gui.update(delta);
		
		// update score animation and transfer points to gui
		for(int i = _scoreAnimation.size()-1; i >= 0; i--) {
			_scoreAnimation.get(i).update(delta);
			
			// transfer score to gui on finished animation
			if(_scoreAnimation.get(i).isFinished()) {
				_gui.addScore(_scoreAnimation.get(i).getScore());
				_scoreAnimation.remove(i);
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.pixlpommes.conn3bomb.screens.ScoreListener#scored(long, float, float)
	 */
	@Override
	public void scored(long score, float x, float y) {
		long scaledScore = (long) (score * _gui.getComboFactor());
		
		ScoreAnimation ani = new ScoreAnimation(_gui, scaledScore, x, y);
		_scoreAnimation.add(ani);
	}

	@Override
	public void comboed() {
		_gui.firedCombo();
	}
}
