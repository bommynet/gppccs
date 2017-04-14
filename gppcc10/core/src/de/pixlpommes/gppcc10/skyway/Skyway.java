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
	public final static int ROWS = 12;
	
	/** tile size in pixel */
	public final static int TILESIZE = 64;
	
	/** lower left corner of the skyway */
	private float _offsetX, _offsetY;
	
	/** TODO: describe '_way' */
	private Tile[][] _way;
	
	/** TODO: describe '_tileTile' */
	private final Texture _tileNormal = new Texture(Gdx.files.internal("tile_normal.png"));
	
	/**
	 * Create skyway.
	 * 
	 * @param positionX
	 * @param positionY
	 */
	public Skyway(float positionX, float positionY) {
		_way = new Tile[COLS][ROWS];
		
		_offsetX = positionX;
		_offsetY = positionY;
		
		for(int x=0; x<COLS; x++) {
			for(int y=0; y<ROWS; y++) {
				_way[x][y] = new Tile(_tileNormal,
						true,
						_offsetX + x*TILESIZE,
						_offsetY + y*TILESIZE);
			}
		}
	}
	
	/**
	 * Draw skyway.
	 * 
	 * @param batch
	 */
	public void draw(Batch batch) {
		for(int x=0; x<COLS; x++) {
			for(int y=0; y<ROWS; y++) {
				_way[x][y].draw(batch);
			}
		}
	}
}
