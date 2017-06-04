package de.pixlpommes.conn3bomb.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.pixlpommes.conn3bomb.ScreenObject;
import de.pixlpommes.conn3bomb.Tiles;

/**
 * <p>
 * The player.
 * </p>
 *
 * <p>
 * This entity is controlled by the user itself. It could move on the lower
 * border of the arena and is able to throw in blocks and bombs to change the
 * arenas state.
 * </p>
 *
 * @author Thomas Borck
 */
public class Player extends ScreenObject {

	// GAME
	/** current column the player stands at */
	private int _pos;

	/** TODO: describe _blocks */
	private int[] _tile;

	/** TODO: describe '_texture' */
	private Texture _texture;

	/**
	 * 
	 */
	public Player(Texture texture) {
		this.setOffset(0, 0);

		_pos = Arena.COLS / 2;

		_tile = new int[] { (int) (Math.random() * Arena.COLORS_COUNT), (int) (Math.random() * Arena.COLORS_COUNT),
				(int) (Math.random() * Tiles.COLORS_COUNT) };

		_texture = texture;

		//Gdx.input.setInputProcessor(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.pixlpommes.conn3bomb.ScreenObject#draw(com.badlogic.gdx.graphics.g2d.
	 * Batch)
	 */
	@Override
	public void draw(Batch batch) {
		float halfsize = Arena.TILESIZE / 2;

		// draw blocks
		drawBlock(batch, _tile[0], _offsetX + _pos * Tiles.TILESIZE, _offsetY, Arena.TILESIZE, Arena.TILESIZE);
		drawBlock(batch, _tile[1], _offsetX + (_pos + 1) * Arena.TILESIZE, _offsetY - 0.25f * Arena.TILESIZE, halfsize,
				halfsize);
		drawBlock(batch, _tile[2], _offsetX + (_pos + 1.5f) * Arena.TILESIZE, _offsetY - 0.25f * Arena.TILESIZE,
				halfsize, halfsize);

		// draw player
		Tiles.drawPlayer(batch, _offsetX + _pos * Arena.TILESIZE, _offsetY);
	}

	/**
	 * TODO: describe function
	 * 
	 * @param tileId
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	private void drawBlock(Batch batch, int tileId, float x, float y, float w, float h) {
		// tile id == -1 -> invisible, so don't draw it
		if (tileId == -1)
			return;

		int tilePosX = (tileId > 9 ? (tileId - 10 + 1) : (tileId + 1)) * Arena.TILESIZE;
		int tilePosY = (tileId > 9 ? 2 : 1) * Arena.TILESIZE;

		batch.draw(_texture, // tile set file
				x, y, // position on screen
				w, h, // size on screen
				tilePosX, tilePosY, // tile position in file
				Arena.TILESIZE, Arena.TILESIZE, // tile size
				false, false // don't flip
		);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.pixlpommes.conn3bomb.ScreenObject#update(float)
	 */
	@Override
	public void update(float delta) {
	}

	/**
	 * TODO: describe function
	 * 
	 * @param stepX
	 */
	public void moveBy(int stepX) {
		_pos += stepX;
		
		if(_pos < 0)
			_pos = 0;
		else if(_pos >= Arena.COLS)
			_pos = Arena.COLS-1;
	}
	
	/**
	 * TODO: describe function
	 * @param positionX
	 */
	public void moveTo(int positionX) {
		if(positionX < 0)
			_pos = 0;
		else if(positionX >= Arena.COLS)
			_pos = Arena.COLS-1;
		else
			_pos = positionX;
	}
	
	/**
	 * TODO: describe function
	 * @return
	 */
	public int fireTileId() {
		int fire = _tile[0];
		
		_tile[0] = _tile[1];
		_tile[1] = _tile[2];
		_tile[2] = (int) (Math.random() * Tiles.COLORS_COUNT);
		
		return fire;
	}
	
	/**
	 * TODO: describe function
	 * @return
	 */
	public int getColumn() {
		return _pos;
	}
}
