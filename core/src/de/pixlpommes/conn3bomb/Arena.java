package de.pixlpommes.conn3bomb;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * <p>
 * The arena.
 * </p>
 * 
 * <p>
 * The arena manages all blocks and bombs in game, moves these objects down and
 * handles throw-ins by player and inserter.
 * </p>
 * 
 * @author Thomas Borck
 */
public class Arena extends ScreenObject {

	// GLOBALS
	/** TODO: describe COLS */
	public final static int COLS = 6;

	/** TODO: describe ROWS */
	public final static int ROWS = 12;

	// GAME
	/** <i>fixated</i> tiles */
	private int[] _tiles;

	/** each tile has it's position on the screen as (x,y) tuple */
	private float[][] _tilePos;

	/** <i>movable</i> tiles */
	private List<Movable> _tilesMove;

	/** speed for all movable tiles (pixels per second) */
	private float _tilesMoveSpeed;

	/**
	 * 
	 */
	public Arena(int offsetX, int offsetY) {
		this.setOffset(offsetX, offsetY);

		// initialize tiles
		_tiles = new int[COLS * ROWS];
		IntStream.range(0, _tiles.length).forEach(index -> _tiles[index] = -1);

		_tilesMove = new ArrayList<>();
		_tilesMoveSpeed = (float) Tiles.TILESIZE / 2f;;

		/// TODO remove! /////////////////
		this.add(0, 0, 0);
		this.add(1, ROWS - 1, 1);
		this.add(1, ROWS - 2, 0);
		this.add(COLS - 1, ROWS - 8, 2);
		this.add(COLS - 1, ROWS - 6, 2);
		//////////////////////////////////

		_tilePos = new float[COLS * ROWS][2];
		IntStream.range(0, _tilePos.length).forEach(index -> {
			int idx = index / ROWS;
			int idy = index % ROWS;

			_tilePos[index][0] = idx * Tiles.TILESIZE; // x
			_tilePos[index][1] = idy * Tiles.TILESIZE; // y
		});
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
		// TODO draw background

		// draw conveyor band
		IntStream.range(0, _tiles.length).forEach(index -> {
			Tiles.drawConvoyer(batch, // batch to draw on
					_offsetX + _tilePos[index][0], // screen x
					_offsetY + _tilePos[index][1] // screen y
			);
		});

		// draw fixated blocks/bombs
		IntStream.range(0, _tiles.length).forEach(index -> {
			Tiles.drawBlock(batch, _tiles[index], _offsetX + _tilePos[index][0],
					_offsetY + _tilePos[index][1]);
		});

		// draw movable blocks/bombs
		_tilesMove.forEach(
				tile -> Tiles.drawBlock(batch, tile.id, tile.x, tile.y));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.pixlpommes.conn3bomb.ScreenObject#update(float)
	 */
	@Override
	public void update(float delta) {
		// TODO update animation frames -> conveyor band

		// TODO update block/bomb positions
		float currentSpeed = _tilesMoveSpeed * delta * (-1);
		_tilesMove.forEach(tile -> tile.updateY(currentSpeed));

		// movables -> fixated when
		// hold removable items in separate list to avoid null-pointers
		List<Movable> remove = new ArrayList<>();

		// 1. tile reached bottom line
		_tilesMove.forEach(tile -> {
			if (tile.y <= _offsetY) {
				int idx = (int) ((tile.x - _offsetX) / Tiles.TILESIZE);
				_tiles[idx * ROWS] = tile.id;
				remove.add(tile);
			}
		});
		
		// remove items stored in 'remove'
		_tilesMove.removeAll(remove);

		// TODO check destroyables
	}

	/**
	 * Adds a new block to arena.
	 * 
	 * @param column
	 * @param row
	 * @param id
	 */
	public void add(int column, int row, int id) {
		Movable tile = new Movable(id, _offsetX + column * Tiles.TILESIZE,
				_offsetY + row * Tiles.TILESIZE);
		_tilesMove.add(tile);

		// TODO: differentiate between top and bottom throw in
	}

	/**
	 * Adds a block on top of the arena.
	 * 
	 * @param column
	 * @param block
	 */
	public void addTop(int column, int block) {
		// TODO: if top block != -1 -> lose
		this.add(column, ROWS, block);
		// int index = (column + 1) * ROWS - 1;
		// _tiles[index] = block;
	}

	/**
	 * Adds a block at he bottom of the arena.
	 * 
	 * @param column
	 * @param block
	 */
	public void addBottom(int column, int block) {
		// TODO: if bottom block != -1 -> move block up
		int index = column * ROWS;
		_tiles[index] = block;
	}

	/**
	 * <p>
	 * TODO: short class description.
	 * </p>
	 *
	 * <p>
	 * TODO: detailed class description.
	 * </p>
	 *
	 * @author Thomas Borck
	 */
	private class Movable {

		/** TODO: describe x */
		public float x;

		/** TODO: describe y */
		public float y;

		/** TODO: describe id */
		public int id;

		/**
		 * @param id
		 * @param x
		 * @param y
		 */
		public Movable(int id, float x, float y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}

		/**
		 * @param diffY
		 */
		public void updateY(float diffY) {
			this.y += diffY;
		}
	}
}
