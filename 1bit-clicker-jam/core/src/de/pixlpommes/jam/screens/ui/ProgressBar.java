package de.pixlpommes.jam.screens.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * A progress bar build by two custom graphics.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class ProgressBar {
	
	public final static String[] FILE_HEALTHBAR =
			new String[]{"health_bar_0.png", "health_bar_1.png"};

	/** image that shows the empty progress bar */
	private Texture _emptyBar;
	
	/** image that shows the filled progress bar */
	private Texture _filledBar;
	
	/** value range for progress */
	private float _min, _max, _current;
	
	/** position of the bar on the screen */
	private float _x, _y;
	
	/**
	 * Create a new progress bar.
	 * 
	 * @param filename name of file for empty and filled bar
	 * @param min
	 * @param max
	 * @param x
	 * @param y
	 */
	public ProgressBar(String[] filename, float min, float max, int x, int y) {
		_emptyBar = new Texture(Gdx.files.internal(filename[0]));
		_filledBar = new Texture(Gdx.files.internal(filename[1]));
		
		_min = min;
		_max = max;
		_current = min;
		
		_x = x;
		_y = y;
	}

	/**
	 * Draw progress bar.
	 * @param batch
	 */
	public void draw(Batch batch) {
		batch.begin();
		
		// draw empty bar as base
		batch.draw(_emptyBar, _x, _y);
		
		// draw filled bar partial
		float normMax = _max - _min;
		float normCur = _current - _min;
		float partial = normCur / normMax;
		batch.draw(_filledBar,
				_x, _y,
				_filledBar.getWidth() * partial,
				(float)_filledBar.getHeight(), 
				0, 0,
				(int)(_filledBar.getWidth() * partial),
				_filledBar.getHeight(),
				false, false);
		
		batch.end();
	}

	/**
	 * Add number to current value.
	 * @param add
	 */
	public void updateValue(float add) {
		_current += add;
		
		_current = _current > _max ? _max : _current;
		_current = _current < _min ? _min : _current;
	}
	
	/**
	 * @param value
	 */
	public void setValue(float value) {
		_current = value;
		
		_current = _current > _max ? _max : _current;
		_current = _current < _min ? _min : _current;
	}
	
	/**
	 * @return
	 */
	public float getValue() {
		return _current;
	}
	
	/**
	 * @param min
	 * @param max
	 * @param current
	 */
	public void setValues(float min, float max, float current) {
		_min = min;
		_max = max;
		_current = current;
	}
	
	/**
	 * @return
	 */
	public float getValueMax() {
		return _max;
	}
}