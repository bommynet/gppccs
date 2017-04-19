package de.pixlpommes.gppcc10.skyway;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.pixlpommes.gppcc10.Player;
import de.pixlpommes.gppcc10.Player.Collide;

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
	public final static int TILESIZE = 66;
	
	/** TODO: describe '_tileTile' */
	public final static Texture TILE_NORMAL = new Texture(Gdx.files.internal("tile_normal.png"));
	
	/** TODO: describe '_tileInvers' */
	public final static Texture TILE_INVERS = new Texture(Gdx.files.internal("tile_inverse.png"));
	
	/** lower left corner of the skyway */
	private float _offsetX, _offsetY;
	
	/** TODO: describe '_way' */
	private TileRow[] _way;
	
	/** reference to player */
	private Player _player;
	
	/** column the player 'stands' on */
	private int _playerCol;
	
	/** row the player 'stands' on */
	private int _playerRow;
	
	/**
	 * Create skyway.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param player
	 */
	public Skyway(float positionX, float positionY, Player player) {
		_way = new TileRow[ROWS];
		
		_offsetX = positionX;
		_offsetY = positionY;
		
		for(int y=0; y<ROWS; y++) {
			_way[y] = new TileRow(TILE_NORMAL,
					_offsetX,
					_offsetY + y*TILESIZE);
		}
		
		_player = player;
//		_playerCol = COLS / 2;
//		_player.setPosition(_offsetX + _playerCol * TILESIZE,
//				_offsetY + TILESIZE);
//		
//		_playerRow = 1;
		resetPlayer();
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
				// remove row from bottom and move it to top
				/// TODO load config from file
				byte config = (byte)0x1C;
				_way[y].set(config, top + TILESIZE);
				
				// update players row reference
				_playerRow++;
				if(_playerRow >= ROWS) _playerRow = 0;
			}
		}
	}

	/**
	 * What kind of tile the player collides with.
	 * 
	 * @return
	 */
	public Collide collide() {
		// left and right of the skyway the player steps on holes
		if(_playerCol < 0 || _playerCol >= _way[0].size())
			return Collide.HOLE;
		// if player steps on invisible tiles, he steps on a hole
		else if(!_way[_playerRow].get(_playerCol).isVisible())
			return Collide.HOLE;
		// if player steps on visible tiles, he steps on... 
		else {
			// ...a normal tile
			return Collide.TILE;
			
			// TODO ...a powerup
			// TODO ...a blocked tile
		}
	}

	/**
	 * Move player based on tiles.
	 * 
	 * @param byColumns
	 */
	public void movePlayer(int byColumns) {
		_playerCol += byColumns;
		float x = _offsetX + _playerCol * TILESIZE;
		_player.switchPos(x);
	}
	
	/**
	 * Reset player to center column.
	 */
	public void resetPlayer() {
		_playerCol = COLS / 2;
		_player.setPosition(_offsetX + _playerCol * TILESIZE,
				_offsetY + TILESIZE);
		
		_playerRow = 1;
	}
}
