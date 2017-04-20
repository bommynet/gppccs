package de.pixlpommes.gppcc10.skyway;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.5
 */
public class TileRow {

	/** tiles of this row */
	private Tile[] _tiles;

	/**
	 * Create new row.
	 * 
	 * @param x
	 * @param y
	 */
	public TileRow(float x, float y) {
		_tiles = new Tile[Skyway.COLS];
		for (int i = 0; i < _tiles.length; i++) {
			_tiles[i] = new Tile(true, x + i * Skyway.TILESIZE, y);
		}
	}
	
	/**
	 * Update position.
	 * 
	 * @param diffY
	 */
	public void updateScroll(float diffY) {
		for (int i = 0; i < _tiles.length; i++) {
			_tiles[i].updateScroll(diffY);
		}
	}
	
	/**
	 * Draw tile.
	 * 
	 * @param batch
	 */
	public void draw(Batch batch) {
		for (int i = 0; i < _tiles.length; i++) {
			_tiles[i].draw(batch);
		}
	}
	
	/**
	 * 
	 * config<br/>
	 * -----------<br/>
	 * 2^7: 0=normal texture, 1=inverse texture<br/>
	 * 2^0 to 2^6: set tile in row (0 to 6) 0=invisible, 1=visible<br/>
	 * @param config
	 * @param y
	 */
	public void set(byte config, float y) {
		/// TODO set first bit as tile color
		///Texture tex = (((config >> 7) & 1) == 1) ? Skyway.TILE_INVERS : Skyway.TILE_NORMAL;
		for (int i = 0; i < _tiles.length; i++) {
			boolean visible = ((config >> i) & 1) == 1;
			_tiles[i].set(y, true, visible);
		}
	}
	
	/**
	 * @return y position of this row
	 */
	public float getY() {
		return _tiles[0].getY();
	}
	
	/**
	 * @param index
	 * @return x position of selected tile in this row
	 */
	public float getX(int index) {
		return _tiles[index].getX();
	}
	
	/**
	 * Get reference of selected tile.
	 * 
	 * @param index
	 * @return
	 */
	public Tile get(int index) {
		return _tiles[index];
	}
	
	/**
	 * @return count tiles of this row (both, visible and invisible)
	 */
	public int size() {
		return _tiles.length;
	}
}
