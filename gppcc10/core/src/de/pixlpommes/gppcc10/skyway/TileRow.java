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
	
	/** TODO: describe '_config' */
	private byte _config;

	/**
	 * @param x
	 * @param y
	 */
	public TileRow(Texture tex, float x, float y) {
		_tiles = new Tile[Skyway.COLS];
		for (int i = 0; i < _tiles.length; i++) {
			_tiles[i] = new Tile(tex, true, x + i * Skyway.TILESIZE, y);
		}
		
		_config = 127;
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
	 * @param config
	 * @param y
	 */
	public void set(byte config, float y) {
		_config = config;
		for (int i = 0; i < _tiles.length; i++) {
			boolean visible = (((byte)Math.pow(2,i)) & config) == ((byte)Math.pow(2,i));
			_tiles[i].set(Skyway.TILE_NORMAL, y, true, visible);
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
