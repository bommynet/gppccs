package de.pixlpommes.gppcc10;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pixlpommes.gppcc10.iceway.Iceway;
import de.pixlpommes.gppcc10.ui.IcewayUI;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class IcewayScreen implements Screen, InputProcessor {
	
	// general
	/** batch to render everything */
	private SpriteBatch _batch;

	/** standard 2D camera */
	private OrthographicCamera _cam;

	// LAYER 0
	/** the iceway */
	private Iceway _iceway;

	// LAYER 1
	/** clouds as texture */
	private Texture _clouds;

	/** position of first cloud texture */
	private float _cloudsY;

	/** moving speed factor for cloud-layer */
	private final float _speedFactorLayer_Clouds = 0.4f;

	// LAYER 2
	/** world as texture */
	private Texture _world;

	/** position of first world texture */
	private float _worldY;

	/** moving speed factor for world-layer */
	private final float _speedFactorLayer_World = 0.2f;
	
	
	private IcewayUI ui = new IcewayUI();
	

	/**
	 * 
	 */
	public IcewayScreen() {
		// setup graphics
		_cam = new OrthographicCamera();
		_batch = new SpriteBatch();

		// _shake = new ScreenShake(_cam);

		// setup iceway (horizontally centered, aligned to bottom)
		int posX = -(Iceway.COLS * Iceway.TILESIZE) / 2;
		int posY = -(Gppcc10.HEIGHT / 2);
		_iceway = new Iceway(posX, posY);

		// setup cloud layer
		_clouds = Iceway.TEXTURE_CLOUDS;
		_cloudsY = -Gppcc10.HALF_HEIGHT;

		// setup world layer
		_world = Iceway.TEXTURE_WOODS;
		_worldY = -Gppcc10.HALF_HEIGHT;

		// add input handling
		Gdx.input.setInputProcessor(this);
		
		// Observers
		_iceway.addObserver(ui);
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
		
		// keyboard input
		//checkInput();

		// update positions
		_iceway.update(delta);

		// update background layers (already negative!)
		float deltaSpeed = _iceway.getSpeed() * delta;

		_cloudsY -= deltaSpeed * _speedFactorLayer_Clouds;
		if (_cloudsY < -Gppcc10.HALF_HEIGHT - Gppcc10.HEIGHT)
			_cloudsY = -Gppcc10.HALF_HEIGHT;

		_worldY -= deltaSpeed * _speedFactorLayer_World;
		if (_worldY < -Gppcc10.HALF_HEIGHT - _world.getHeight())
			_worldY = -Gppcc10.HALF_HEIGHT;
		
		
		// update ui animations
		ui.update(delta);

		// TODO: do a screen shake if needed
		/// _shake.shakeUpdate(_batch, delta);

		// draw game
		_batch.begin();
		// 1. world (layer 'down') -- height 200, so need 5 copies
		_batch.draw(_world, -Gppcc10.HALF_WIDHT, _worldY);
		_batch.draw(_world, -Gppcc10.HALF_WIDHT, _worldY + 1 * _world.getHeight());
		_batch.draw(_world, -Gppcc10.HALF_WIDHT, _worldY + 2 * _world.getHeight());
		_batch.draw(_world, -Gppcc10.HALF_WIDHT, _worldY + 3 * _world.getHeight());
		_batch.draw(_world, -Gppcc10.HALF_WIDHT, _worldY + 4 * _world.getHeight());
		// 2. clouds (layer 'middle')
		_batch.draw(_clouds, -Gppcc10.HALF_WIDHT, _cloudsY);
		_batch.draw(_clouds, -Gppcc10.HALF_WIDHT, _cloudsY + Gppcc10.HEIGHT);
		// 3. game (layer 'top')
		_iceway.draw(_batch);
		
		// GUI
		ui.draw(_batch);
		_batch.end();
	}
	

	@Override
	public void resize(int width, int height) {
		Gdx.app.log("BattleScreen", "resize called (" + width + "x" + height + ")");

		// update camera
		_cam.viewportWidth = width;
		_cam.viewportHeight = height;
		_cam.update();

		// update shaker
		/// _shake = new ScreenShake(_cam);

		// update renderer
		_batch.setProjectionMatrix(_cam.combined);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	
	public void checkInput() {
		// moving -> no more player move
		if (!_iceway.isMoving())
			return;
		
		// old input algorithm
		if (Gdx.input.isKeyPressed(Keys.A)) {
			_iceway.movePlayerBy(-Iceway.TILESIZE);
		} else if (Gdx.input.isKeyPressed(Keys.D)) {
			_iceway.movePlayerBy(Iceway.TILESIZE);
		}
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#keyDown(int)
	 */
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.A:
				_iceway.movePlayerLeft();
				break;
				
			case Keys.D:
				_iceway.movePlayerRight();
				break;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#keyUp(int)
	 */
	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
			case Keys.A:
				_iceway.movePlayerNot();
				break;
				
			case Keys.D:
				_iceway.movePlayerNot();
				break;
		}
		
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
