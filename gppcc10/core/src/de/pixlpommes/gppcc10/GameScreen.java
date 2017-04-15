package de.pixlpommes.gppcc10;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.pixlpommes.gppcc10.skyway.Skyway;

/**
 * Basic game screen.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class GameScreen implements Screen, InputProcessor {

	/** batch to render everything */
	private SpriteBatch _batch;

	/** standard 2D camera */
	private OrthographicCamera _cam;

	/** the skyway */
	private Skyway _skyway;

	/** moving speed in pixel per second */
	private float _skywaySpeed;

	/** the player */
	private Player _player;

	/**
	 * Create and init game screen.
	 */
	public GameScreen() {
		// setup graphics
		_cam = new OrthographicCamera();
		_batch = new SpriteBatch();

		// center skyway horizontally
		float pos = (Gppcc10.WIDTH - Skyway.COLS * Skyway.TILESIZE) / 2;
		_skyway = new Skyway(pos, 0);

		_player = new Player(_skyway.getXOfCol((int) Math.floor(Skyway.COLS / 2)), 0);

		_skywaySpeed = -170;
		Gdx.input.setInputProcessor(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		// clear screen
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// update positions
		_skyway.updateScroll(_skywaySpeed * delta);
		_player.update(delta);

		// draw skyway
		_batch.begin();
		// 1. world (layer 'down')
		// 2. clouds (layer 'middle')
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
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.InputProcessor#keyDown(int)
	 */
	@Override
	public boolean keyDown(int keycode) {
		float newPos = 0;

		if (keycode == Keys.A) {
			newPos = _player.getX() - Skyway.TILESIZE;
		} else if (keycode == Keys.D) {
			newPos = _player.getX() + Skyway.TILESIZE;
		}

		if (newPos != 0) {
			_player.switchPos(newPos);
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
