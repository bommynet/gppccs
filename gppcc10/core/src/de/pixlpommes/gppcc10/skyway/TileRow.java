package de.pixlpommes.gppcc10.skyway;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class TileRow {

	/** TODO: describe '_tiles' */
	private Tile[] _tiles;

	/**
	 * @param x
	 * @param y
	 */
	public TileRow(Texture tex, float x, float y) {
		_tiles = new Tile[Skyway.COLS];
		for (int i = 0; i < _tiles.length; i++) {
			_tiles[i] = new Tile(tex, true, x + i * Skyway.TILESIZE, y);
		}
	}
	
	/**
	 * @param diffY
	 */
	public void updateScroll(float diffY) {
		for (int i = 0; i < _tiles.length; i++) {
			_tiles[i].updateScroll(diffY);
		}
	}
	
	/**
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
		Texture tex = (((config >> 7) & 1) == 1) ? Skyway.TILE_INVERS : Skyway.TILE_NORMAL;
		for (int i = 0; i < _tiles.length; i++) {
			boolean visible = ((config >> i) & 1) == 1;
			_tiles[i].set(tex, y, true, visible);
		}
	}
	
	/**
	 * @return
	 */
	public float getY() {
		return _tiles[0].getY();
	}
	
	/**
	 * @param index
	 * @return
	 */
	public float getX(int index) {
		return _tiles[index].getX();
	}
}
