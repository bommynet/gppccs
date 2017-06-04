package de.pixlpommes.conn3bomb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import de.pixlpommes.conn3bomb.GameApp;

/**
 * <p>
 * Standard screen implementation.
 * </p>
 *
 * <p>
 * Every screen should use the same configuration, so moved these standards out
 * of each screen to this class. The {@link #render()}-method can't be overwritten any
 * more, instead use {@link #draw()} and {@link #update(float)} for your code.
 * </p>
 * 
 * <p>
 * <b>Caution:</b> Each frame will call {@link #draw()} then {@link #update(float)}.
 * </p>
 *
 * @author Thomas Borck - http://www.pixlpommes.de
 */
public abstract class ScreenAdapter implements Screen {

	/** the game app */
	protected final GameApp _app;

	/**
	 * @param app
	 *            the game application
	 */
	public ScreenAdapter(final GameApp app) {
		_app = app;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		Gdx.app.log("Screen [" + this.getClass().getSimpleName() + "]", "show called");
	}

	/**
	 * Split render in draw() and update().
	 * 
	 * The draw-function will be called before update, so everything has to be
	 * setup before correctly.
	 * 
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public final void render(float delta) {
		draw();
		update(delta);
	}

	/**
	 * Draw screen content.
	 */
	public abstract void draw();

	/**
	 * Update screens game logic.
	 * 
	 * @param delta
	 */
	public abstract void update(float delta);

	/**
	 * Center content if window was resized.
	 * 
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		Gdx.app.log("Screen [" + this.getClass().getSimpleName() + "]", "resize called (" + width + "x" + height + ")");

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
		Gdx.app.log("Screen [" + this.getClass().getSimpleName() + "]", "pause called");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		Gdx.app.log("Screen [" + this.getClass().getSimpleName() + "]", "resume called");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		Gdx.app.log("Screen [" + this.getClass().getSimpleName() + "]", "hide called");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		Gdx.app.log("Screen [" + this.getClass().getSimpleName() + "]", "dispose called");
	}

}
