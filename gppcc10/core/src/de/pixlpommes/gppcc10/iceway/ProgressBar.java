package de.pixlpommes.gppcc10.iceway;

import com.badlogic.gdx.graphics.g2d.Batch;

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
	
	/** TODO: describe '_y' */
	private float _x, _y;
	
	
	/**
	 * @param posX
	 * @param posY
	 */
	public ProgressBar(float posX, float posY) {
		_x = posX;
		_y = posY;
		// blue texture @(48, 256)x(8,64)
		// red texture @(16, 256)x(8,64)
		// bars top end tile @(282, 247)x(14,9)
		// bars lower end tile @(282, 192)x(14,9)
	}
	
	/**
	 * TODO: describe function
	 * @param batch
	 */
	public void draw(Batch batch) {
		// current y
		float curY = _y;
		
		// lower end tile
		batch.draw(Iceway.TILESET,
			_x, curY,
			282, 192, // tile position in tile set
			14, 9); // tile size
		// add tile size to y
		curY += 9;
		
		for(int i=0; i<6; i++) {
			batch.draw(Iceway.TILESET,
				_x+3, // add 3 to position bar in center
				curY,
				48, 256, // tile position in tile set
				8, 64); // tile size
			curY += 64;
		}
		
		// top end tile
		batch.draw(Iceway.TILESET,
				_x, curY,
				282, 247, // tile position in tile set
				14, 9); // tile size
		
		// blue texture @(48, 256)x(8,64)
		// red texture @(16, 256)x(8,64)
		// bars top end tile @(282, 247)x(14,9)
		// bars lower end tile @(282, 192)x(14,9)
	}
}
