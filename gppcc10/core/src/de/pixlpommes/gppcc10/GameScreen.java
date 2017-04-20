package de.pixlpommes.gppcc10;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pixlpommes.gppcc10.Player.Collide;
import de.pixlpommes.gppcc10.skyway.Skyway;

/**
 * Basic game screen.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.3
 */
public class GameScreen implements Screen, InputProcessor {

	/** batch to render everything */
	private SpriteBatch _batch;

	/** standard 2D camera */
	private OrthographicCamera _cam;

	//// ** manages a screen shake */
	/// private ScreenShake _shake;

	/** moving speed in pixel per second */
	private float _skywaySpeed;

	/** moving speed factor for cloud-layer */
	private final float _speedFactorLayer_Clouds = 0.4f;

	/** moving speed factor for world-layer */
	private final float _speedFactorLayer_World = 0.2f;

	// LAYER 0
	/** the skyway */
	private Skyway _skyway;

	/** moves or stops skyway */
	private boolean _skywayIsMoving;

	/** the player */
	private Player _player;

	// LAYER 1
	/** clouds as texture */
	private Texture _clouds;

	/** position of first cloud texture */
	private float _cloudsY;

	// LAYER 2
	/** world as texture */
	private Texture _world;

	/** position of first world texture */
	private float _worldY;

	/**
	 * Create and init game screen.
	 */
	public GameScreen() {
		// setup graphics
		_cam = new OrthographicCamera();
		_batch = new SpriteBatch();

		// _shake = new ScreenShake(_cam);

		// setup player
		_player = new Player();

		// setup skyway (horizontally centered)
		float pos = (Gppcc10.WIDTH - Skyway.COLS * Skyway.TILESIZE) / 2;
		_skyway = new Skyway(pos, 0, _player);

		_skywaySpeed = -170;
		_skywayIsMoving = true;

		// setup cloud layer
		_clouds = new Texture(Gdx.files.internal("clouds_Wolken.png"));
		_cloudsY = 0;

		// setup world layer
		_world = new Texture(Gdx.files.internal("clouds_Welt.png"));
		_worldY = 0;

		// add input handling
		Gdx.input.setInputProcessor(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@SuppressWarnings("incomplete-switch") // not all states can be reached
	@Override
	public void render(float delta) {
		// clear screen
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// check collisions
		Collide collide = _skyway.collide();
		if (collide != Collide.TILE && !_player.isSwitching()) {
			switch (collide) {
			case HOLE:
				// player falls -> reset to center
				/// TODO animate falling before reset
				_skywayIsMoving = false;
				_skyway.resetPlayer();
				_player.changeState(Player.State.FALL);
				/// _shake.shake(); /// TODO remove!
				_skywayIsMoving = true;
				break;

			case BLOCK:
				// player is blocked -> reset to center
				/// TODO animate blocking before reset
				_skywayIsMoving = false;
				_skyway.resetPlayer();
				_player.changeState(Player.State.BLOCK);
				/// _shake.shake(); /// TODO remove!
				_skywayIsMoving = true;
				break;
			}

		}

		// update positions
		if (_skywayIsMoving) {
			float deltaSpeed = _skywaySpeed * delta;

			_skyway.updateScroll(deltaSpeed);

			_cloudsY += deltaSpeed * _speedFactorLayer_Clouds;
			if (_cloudsY < -_clouds.getHeight())
				_cloudsY = 0;

			_worldY += deltaSpeed * _speedFactorLayer_World;
			if (_worldY < -_world.getHeight())
				_worldY = 0;
		}
		_player.update(delta);

		// do a screen shake
		/// _shake.shakeUpdate(_batch, delta);

		// draw game
		_batch.begin();
		// 1. world (layer 'down')
		_batch.draw(_world, 0, _worldY);
		_batch.draw(_world, 0, _worldY + Gppcc10.HEIGHT);
		// 2. clouds (layer 'middle')
		_batch.draw(_clouds, 0, _cloudsY);
		_batch.draw(_clouds, 0, _cloudsY + Gppcc10.HEIGHT);
		// 3. game (layer 'top')
		_skyway.draw(_batch);
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
		Gdx.app.log("BattleScreen", "resize called (" + width + "x" + height + ")");

		// update camera
		_cam.viewportWidth = width;
		_cam.viewportHeight = height;
		_cam.update();
		_cam.translate(width / 2, height / 2); // origin at (0,0) = down left

		// update shaker
		/// _shake = new ScreenShake(_cam);

		// update renderer
		_batch.setProjectionMatrix(_cam.combined);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.InputProcessor#keyDown(int)
	 */
	@Override
	public boolean keyDown(int keycode) {
		// no move -> no player
		if (!_skywayIsMoving)
			return false;

		if (keycode == Keys.A) {
			_skyway.movePlayer(-1);
		} else if (keycode == Keys.D) {
			_skyway.movePlayer(1);
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#keyUp(int)
	 */
	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#keyTyped(char)
	 */
	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#touchDown(int, int, int, int)
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#touchUp(int, int, int, int)
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#touchDragged(int, int, int)
	 */
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#mouseMoved(int, int)
	 */
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#scrolled(int)
	 */
	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
