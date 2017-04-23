package de.pixlpommes.gppcc10.iceway;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Update and show a row of the ice way.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class IcewayRow {
	
	/** TODO: describe '_tiles' */
	private Tile[] _tiles;
	
	/** TODO: describe '_y' */
	private float _x, _y;
	
	
	/**
	 * @param x
	 * @param y
	 */
	public IcewayRow(float x, float y) {
		_x = x;
		_y = y;
		
		_tiles = new Tile[Iceway.COLS];
		for(int i=0; i<_tiles.length; i++)
			_tiles[i] = Tile.NORMAL;
	}
	
	/**
	 * @param x
	 * @param y
	 */
	public IcewayRow(float x, float y, Tile[] config) {
		_x = x;
		_y = y;
		
		_tiles = config;
	}
	
	
	/**
	 * TODO: describe function
	 * @param force direction on y axis (already delta-scaled before)
	 */
	public void update(float force) {
		_y += force;
	}
	
	/**
	 * TODO: describe function
	 * @param batch
	 */
	public void draw(Batch batch) {
		for(int i=0; i<_tiles.length; i++) {
			// holes can't be drawn
			if(_tiles[i] == Tile.NONE) continue;
			
			// draw tile
			batch.draw(Iceway.TILESET,
					_x + i * Iceway.TILESIZE,
					_y,
					0, 0, // tile position in tile set
					64, 64); // tile size
		}
	}
	
	/**
	 * @return y position of this row
	 */
	public float getY() {
		return _y;
	}
	
	
	/**
	 * Possible types of tiles.
	 * 
	 * @author Thomas Borck - http://www.pixlpommes.de
	 * @version 1.0
	 */
	public enum Tile {
		/** normal ice block */
		NORMAL,
		
		/** show no tile */
		NONE;
	}
}