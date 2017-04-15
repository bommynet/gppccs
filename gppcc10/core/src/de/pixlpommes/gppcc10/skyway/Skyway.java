package de.pixlpommes.gppcc10.skyway;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * The skyway.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Skyway {

	/** tiles count in width */
	public final static int COLS = 7;
	
	/** tiles count in height */
	public final static int ROWS = 14;
	
	/** tile size in pixel */
	public final static int TILESIZE = 64;
	
	/** lower left corner of the skyway */
	private float _offsetX, _offsetY;
	
	/** TODO: describe '_way' */
	private TileRow[] _way;
	
	/** TODO: describe '_tileTile' */
	public final static Texture TILE_NORMAL = new Texture(Gdx.files.internal("tile_normal.png"));
	
	/** TODO: describe '_tileInvers' */
	public final static Texture TILE_INVERS = new Texture(Gdx.files.internal("tile_normal.png"));
	
	/**
	 * Create skyway.
	 * 
	 * @param positionX
	 * @param positionY
	 */
	public Skyway(float positionX, float positionY) {
		_way = new TileRow[ROWS];
		
		_offsetX = positionX;
		_offsetY = positionY;
		
		for(int y=0; y<ROWS; y++) {
			_way[y] = new TileRow(TILE_NORMAL,
					_offsetX,
					_offsetY + y*TILESIZE);
		}
	}
	
	/**
	 * Draw skyway.
	 * 
	 * @param batch
	 */
	public void draw(Batch batch) {
		for(int y=0; y<ROWS; y++) {
			_way[y].draw(batch);
		}
	}
	

	
	/**
	 * Update skyway's tiles.
	 * 
	 * @param diffY
	 */
	public void updateScroll(float diffY) {
		for(int y=0; y<ROWS; y++) {
			int topIndex = (y-1 < 0) ? ROWS-1 : y-1;
			float top = _way[topIndex].getY();
			
			_way[y].updateScroll(diffY);
			/// TODO remove space every #ROWS tiles
			if(_way[y].getY() <= -TILESIZE) {
				_way[y].set((byte)15, top + TILESIZE);
			}
		}
	}

	/**
	 * @param index
	 * @return
	 */
	public float getXOfCol(int index) {
		return _way[0].getX(index);
	}
}
