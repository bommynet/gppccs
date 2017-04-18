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
 * @version 0.1
 */
public class GameScreen implements Screen, InputProcessor {

	/** batch to render everything */
	private SpriteBatch _batch;

	/** standard 2D camera */
	private OrthographicCamera _cam;

	// LAYER 0
	/** the skyway */
	private Skyway _skyway;

	/** moving speed in pixel per second */
	private float _skywaySpeed;
	
	/** moves or stops skyway */
	private boolean _skywayIsMoving;

	/** the player */
	private Player _player;
	
	// LAYER 1
	/** clouds as texture */
	private Texture _clouds;
	
	/** TODO: describe '_cloudsY' */
	private float _cloudsY;
	
	// LAYER 2
	/** world as texture */
	private Texture _world;
	
	/** TODO: describe '_worldY' */
	private float _worldY;

	/**
	 * Create and init game screen.
	 */
	public GameScreen() {
		// setup graphics
		_cam = new OrthographicCamera();
		_batch = new SpriteBatch();
		
		_player = new Player(0, 0);

		// center skyway horizontally
		float pos = (Gppcc10.WIDTH - Skyway.COLS * Skyway.TILESIZE) / 2;
		_skyway = new Skyway(pos, 0, _player);

		_skywaySpeed = -170;
		_skywayIsMoving = true;
		
		
		_clouds = new Texture(Gdx.files.internal("clouds_Wolken.png"));
		_cloudsY = 0;

		_world = new Texture(Gdx.files.internal("clouds_Welt.png"));
		_worldY = 0;
		
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
		
		// check collisions
		Collide collide = _skyway.collide();
		if(collide != Collide.TILE && !_player.isSwitching()) {
			// something special happened
			_skywayIsMoving = false;
		}
		
		// update positions
		if(_skywayIsMoving) {
			float deltaSpeed = _skywaySpeed * delta;
			
			_skyway.updateScroll(deltaSpeed);
			
			_cloudsY += deltaSpeed * 0.5f;
			if(_cloudsY < -_clouds.getHeight()) _cloudsY = 0;
			
			_worldY += deltaSpeed * 0.2f;
			if(_worldY < -_world.getHeight()) _worldY = 0;
		}
		_player.update(delta);

		// draw skyway
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
		// no move -> no player
		if(!_skywayIsMoving) return false;
		
		if (keycode == Keys.A) {
			_skyway.movePlayer(-1);
		} else if (keycode == Keys.D) {
			_skyway.movePlayer(1);
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
