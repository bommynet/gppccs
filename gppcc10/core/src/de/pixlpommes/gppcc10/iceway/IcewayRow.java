package de.pixlpommes.gppcc10.iceway;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Update and show a row of the ice way.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.2
 */
public class IcewayRow {
	
	/** TODO: describe '_tiles' */
	private Tile[] _tiles;
	
	/** TODO: describe '_y' */
	private float _x, _y;
	
	/** TODO: describe '_isGoalRow' */
	private boolean _isGoalRow;
	
	/** TODO: describe '_isMolten' */
	private boolean _isMolten;
	
	/** TODO: describe '_meltTimer' */
	private float[] _meltTimer;
	
	/** TODO: describe 'MELT_TIME' */
	public final static float MELT_TIME = 2f;
	
	/** scale factor at which the player should fall */
	public final static float MELT_FACTOR_FALLING = 0.75f;
	
	
	/**
	 * @param x
	 * @param y
	 */
	public IcewayRow(float x, float y) {
		_x = x;
		_y = y;
		
		_isMolten = false;
		_meltTimer = new float[Iceway.COLS];
		for(int i=0; i<_meltTimer.length; i++)
			_meltTimer[i] = IcewayRow.MELT_TIME;
		
		_isGoalRow = false;
		
		_tiles = new Tile[Iceway.COLS];
		for(int i=0; i<_tiles.length; i++)
			_tiles[i] = Tile.NORMAL;
	}
	
	/**
	 * @param x
	 * @param y
	 * @param config
	 */
	public IcewayRow(float x, float y, Tile[] config) {
		this(x, y, config, false);
	}
	
	/**
	 * @param x
	 * @param y
	 * @param config
	 * @param goal
	 */
	public IcewayRow(float x, float y, Tile[] config, boolean goal) {
		_x = x;
		_y = y;
		
		_isMolten = false;
		_meltTimer = new float[Iceway.COLS];
		for(int i=0; i<_meltTimer.length; i++)
			_meltTimer[i] = IcewayRow.MELT_TIME;
		
		_isGoalRow = goal;
		
		_tiles = new Tile[config.length];
		for(int i=0; i<_tiles.length; i++)
			_tiles[i] = config[i];
	}
	
	
	/**
	 * TODO: describe function
	 * @param delta
	 * @param force direction on y axis (already delta-scaled before)
	 */
	public void update(float delta, float force) {
		_y += force;
		
		boolean somethingStillMelting = false;
		
		for(int i=0; i<_tiles.length; i++) {
			if(_tiles[i] == Tile.MELTING || _tiles[i] == Tile.MOLTEN) {
				_meltTimer[i] -= delta;
				
				// tile is gone
				if(_meltTimer[i] <= 0) {
					_meltTimer[i] = 0f;
					_tiles[i] = Tile.NONE;
				} else {
					somethingStillMelting = true;
				}
			} else {
				somethingStillMelting = true;
			}
		}
		
		if(!somethingStillMelting)
			_isMolten = true;
	}
	
	/**
	 * TODO: describe function
	 * @param batch
	 */
	public void draw(Batch batch) {
		for(int i=0; i<_tiles.length; i++) {
			// holes can't be drawn
			if(_tiles[i] == Tile.NONE) continue;
			
			// no scaling needed for non-melting tiles
			if(_tiles[i] == Tile.NORMAL) {
				// draw tile
				batch.draw(Iceway.TILESET,
						_x + i * Iceway.TILESIZE,
						_y,
						(_isGoalRow ? 64 : 0), 0, // tile position in tile set
						Iceway.TILESIZE-2, Iceway.TILESIZE-2); // tile size
			} else {
				float factor = _meltTimer[i] / IcewayRow.MELT_TIME;
				
				// at defined melting point, the player can't stand on this block anymore
				if(_tiles[i] == Tile.MELTING && factor <= IcewayRow.MELT_FACTOR_FALLING)
					_tiles[i] = Tile.MOLTEN;
				
				// weighted position (factX) and size (factW) of sprite
				float factW = (Iceway.TILESIZE-2)*factor;
				float factX = (Iceway.TILESIZE-2) - factW;
				
				// draw tile
				batch.draw(Iceway.TILESET,
						_x + i * Iceway.TILESIZE + factX,
						_y + factX,
						factW, factW, // tile scaled size
						0, 0, // tile position in tile set
						Iceway.TILESIZE-2, Iceway.TILESIZE-2, // tile size
						false, false); // don't flip sprite
			}
		}
	}
	
	/**
	 * @return y position of this row
	 */
	public float getY() {
		return _y;
	}
	
	/**
	 * TODO: describe function
	 * @param index
	 * @return
	 */
	public float getX(int index) {
		return _x + index * Iceway.TILESIZE;
	}
	
	/**
	 * TODO: describe function
	 * @return
	 */
	public Tile getTile(int index) {
		return _tiles[index];
	}
	
	/**
	 * TODO: describe function
	 * @return
	 */
	public boolean isMolten() {
		return _isMolten;
	}
	
	/**
	 * TODO: describe function
	 */
	public void setMolten() {
		// melt tiles
		for(int i=0; i<_tiles.length; i++) {
			if(_tiles[i] != Tile.NORMAL) continue;
			
			_tiles[i] = Tile.MELTING;
		}
	}
	
	
	/**
	 * Possible types of tiles.
	 * 
	 * @author Thomas Borck - http://www.pixlpommes.de
	 * @version 1.1
	 */
	public enum Tile {
		/** normal ice block */
		NORMAL,
		
		/** ice block melts now */
		MELTING,
		
		/** ice block is molten */
		MOLTEN,
		
		/** show no tile */
		NONE;
	}
}
