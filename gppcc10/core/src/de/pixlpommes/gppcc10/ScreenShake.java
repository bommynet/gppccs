package de.pixlpommes.gppcc10;

import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Screen shake algorithm.
 * 
 * Based on code from 'smilne' - http://www.netprogs.com/libgdx-screen-shaking/
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 1.0
 */
public class ScreenShake {

	protected float _shakeElapsed;

	protected float _shakeDuration;

	protected float _shakeBaseX;

	protected float _shakeBaseY;

	protected float _shakeIntensity;

	protected Random _shakeRandom;
	
	protected OrthographicCamera _cam;

	/**
	 * 
	 */
	public ScreenShake(OrthographicCamera cam) {
		_cam = cam;
		
		_shakeBaseX = _cam.position.x;
		_shakeBaseY = _cam.position.y;
		
		_shakeRandom = new Random();

		_shakeDuration = _shakeElapsed = 1;
		_shakeIntensity = 10;
	}

	/**
	 * @param batch
	 * @param delta
	 */
	protected void shakeUpdate(Batch batch, float delta) {
		_cam.position.x = _shakeBaseX;
		_cam.position.y = _shakeBaseY;
		
		// only shake when required
		if (_shakeElapsed < _shakeDuration) {
			// Calculate the amount of shake based on how long it has been
			// shaking already
			float currentPower = _shakeIntensity * _cam.zoom * ((_shakeDuration - _shakeElapsed) / _shakeDuration);
			float x = (_shakeRandom.nextFloat() - 0.5f) * currentPower;
			float y = (_shakeRandom.nextFloat() - 0.5f) * currentPower;
			_cam.translate(-x, -y);

			// Increase the elapsed time by the delta provided.
			_shakeElapsed += delta;
		}
		
		batch.setProjectionMatrix(_cam.combined);
		_cam.update();
	}
	
	/**
	 * 
	 */
	protected void shake() {
		_shakeElapsed = 0;
	}
}