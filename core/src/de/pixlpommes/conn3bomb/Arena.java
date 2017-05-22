package de.pixlpommes.conn3bomb;

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
	/** the tiles as raw data */
	private int[] _tiles;

	/** each tile has it's position on the screen as (x,y) tuple */
	private float[][] _tilePos;

	/** all tiles moving the same speed, so use one var for all */
	private float _timedOffsetY;

	/** current elapsed time for move timer */
	private float _moveTimer;

	/** maximum time needed to move blocks one row down */
	private float _moveDelay;

	/**
	 * 
	 */
	public Arena() {
		this.setOffset(0, 0);

		// setup game data
		_tiles = new int[COLS * ROWS];
		IntStream.range(0, _tiles.length).forEach(index -> _tiles[index] = -1);

		/// TODO remove! /////////////////
		_tiles[0] = 0;
		_tiles[(ROWS - 1) * 2] = 1;
		_tiles[(ROWS - 1) * 2 + 1] = 0;
		_tiles[COLS * ROWS - 8] = 2;
		_tiles[COLS * ROWS - 6] = 2;
		//////////////////////////////////

		_tilePos = new float[COLS * ROWS][2];
		IntStream.range(0, _tilePos.length).forEach(index -> {
			int idx = index / ROWS;
			int idy = index % ROWS;

			_tilePos[index][0] = idx * Tiles.TILESIZE; // x
			_tilePos[index][1] = idy * Tiles.TILESIZE; // y
		});

		_timedOffsetY = 0;
		_moveTimer = _moveDelay = 2f;
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

		// TODO draw conveyor band
		IntStream.range(0, _tiles.length).forEach(index -> {
			Tiles.drawConvoyer(batch, // batch to draw on
					_offsetX + _tilePos[index][0], // screen x
					_offsetY + _tilePos[index][1] // screen y
			);
		});

		// TODO draw blocks/bombs
		IntStream.range(0, _tiles.length).forEach(index -> {
			Tiles.drawBlock(batch, _tiles[index], _offsetX + _tilePos[index][0],
					_offsetY - _timedOffsetY + _tilePos[index][1]);
		});
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
		_moveTimer -= delta;
		if (_moveTimer < 0) {
			// move down blocks
			IntStream.range(0, _tiles.length).forEach(index -> {
				int idy = index % ROWS;

				if (idy > 0 && _tiles[index - 1] == -1) {
					_tiles[index - 1] = _tiles[index];
					_tiles[index] = -1;
				}
			});

			// reset timer
			_moveTimer = _moveDelay;
		}
		/// TODO reactivate
		/// _timedOffsetY = (1.0f - _moveTimer / _moveDelay) * TILESIZE;

		// TODO check destroyables
	}

	/**
	 * Adds a block on top of the arena.
	 * 
	 * @param column
	 * @param block
	 */
	public void addTop(int column, int block) {
		// TODO: if top block != -1 -> lose
		int index = (column + 1) * ROWS - 1;
		_tiles[index] = block;
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
}
