package de.pixlpommes.conn3bomb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import de.pixlpommes.conn3bomb.GameApp;

/**
 * <p>
 * Loading screen.
 * </p>
 *
 * @author Thomas Borck - http://www.pixlpommes.de
 */
public class LoadingScreen extends ScreenAdapter {

	// THE SCREEN
	/** loading progress in % */
	private float _progress;

	/** loaded assets */
	private int _countCurrent;

	/** count queued assets */
	private int _countQueued;

	/**
	 * Create loading screen.
	 * 
	 * @param app
	 *            the game application
	 */
	public LoadingScreen(final GameApp app) {
		super(app);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.pixlpommes.conn3bomb.screens.ScreenAdapter#show()
	 */
	public void show() {
		// load assets in queue
		_app.assets.load("tiles.png", Texture.class);
		_app.assets.load("graphics/band.png", Texture.class);

		// init progress
		_progress = 0.0f;
		_countCurrent = 0;
		_countQueued = _app.assets.getQueuedAssets();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.pixlpommes.conn3bomb.screens.ScreenAdapter#draw()
	 */
	@Override
	public void draw() {
		// clear screen
		Gdx.gl.glClearColor(0.553f, 0.651f, 0.711f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.pixlpommes.conn3bomb.screens.ScreenAdapter#update(float)
	 */
	@Override
	public void update(float delta) {
		// get progress values
		boolean loadingFinished = _app.assets.update();
		// _progress = _app.assets.getProgress();
		_progress = MathUtils.lerp(_progress, _app.assets.getProgress(), 0.1f); // smoothen
																				// progress
																				// for
																				// nicer
																				// progress
																				// bar
		_countCurrent = _app.assets.getLoadedAssets();

		// console log
		Gdx.app.log("Loading %:", "" + (_progress * 100));
		Gdx.app.log("Loading #:", _countCurrent + "/" + _countQueued);

		// TODO: draw fancy progress stuff

		// switch screen if finished
		if (loadingFinished && _progress >= 0.999f) { // >= 0.999 'cause
														// function will not
														// reach 1.0
			_app.setScreen(new GameScreen(_app));
		}
	}
}
