package de.pixlpommes.gppcc10.iceway;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.pixlpommes.gppcc10.Gppcc10;

/**
 * A level progress bar.
 * 
 * Shows where the player actually is (relative to complete level length) and
 * where the blow-flyers are.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class ProgressBar {
	
	private final int END_TILE_WIDTH = 14;
	private final int END_TILE_HEIGHT = 9;
	private final int BAR_TILE_WIDTH = 12;
	private final int BAR_TILE_HEIGHT = 42;
	
	/** TODO: describe '_y' */
	private float _x, _y;
	
	/** TODO: describe '_flash' */
	private boolean _flash;
	
	/** TODO: describe '_flashTime' */
	private float _flashTime, _flashMaxTime;
	
	
	/**
	 * @param posX
	 * @param posY
	 */
	public ProgressBar(float posX, float posY) {
		_x = posX;
		_y = posY;
		
		_flash = false;
		_flashTime = 0;
		_flashMaxTime = 2;
	}
	
	/**
	 * TODO: describe function
	 * @param delta
	 */
	public void update(float delta) {
		if(_flash && _flashTime > 0) {
			_flashTime -= delta;
		} else if(_flash) {
			_flash = false;
		}
	}
	
	/**
	 * TODO: describe function
	 * @param batch
	 */
	public void draw(Batch batch) {
		// current y
		float curY = _y;
		
		// select bar tile color by variable 'flash'
		int tileX = (_flash) ? 80 : 64;
		
		// lower end tile
		batch.draw(Gppcc10.TILESET,
			_x, curY,
			282, 192, // tile position in tile set
			END_TILE_WIDTH, END_TILE_HEIGHT); // tile size
		// add tile size to y
		curY += END_TILE_HEIGHT;
		
		// all bar tiles
		for(int i=0; i<6; i++) {
			batch.draw(Gppcc10.TILESET,
				_x+1, // add 1 to position bar in center
				curY,
				tileX, 256, // tile position in tile set
				BAR_TILE_WIDTH, BAR_TILE_HEIGHT); // tile size
			curY += BAR_TILE_HEIGHT;
		}
		
		// top end tile
		batch.draw(Gppcc10.TILESET,
				_x, curY,
				282, 247, // tile position in tile set
				END_TILE_WIDTH, END_TILE_HEIGHT); // tile size
		
		// blue texture @(64, 256)x(12,42)
		// red texture @(80, 256)x(12,42)
		// bars top end tile @(282, 247)x(14,9)
		// bars lower end tile @(282, 192)x(14,9)
	}
	
	/**
	 * TODO: describe function
	 */
	public void flash() {
		if(_flash) return;
		
		_flash = true;
		_flashTime = _flashMaxTime;
	}
}
