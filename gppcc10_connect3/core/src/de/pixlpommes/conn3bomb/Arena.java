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

	/** speed for all tiles on conveyor band (pixels per second) */
	private float _moveSpeedConveyor;

	/** speed for all tiles fired by player (pixels per second) */
	private float _moveSpeedPlayerTiles;

	/**
	 * 
	 */
	public Arena(int offsetX, int offsetY) {
		this.setOffset(offsetX, offsetY);

		// initialize tiles
		_tiles = new int[COLS * ROWS];
		IntStream.range(0, _tiles.length).forEach(index -> _tiles[index] = -1);

		_tilesMove = new ArrayList<>();
		_moveSpeedConveyor = (float) Tiles.TILESIZE / 2f;
		_moveSpeedPlayerTiles = (float) Tiles.TILESIZE * 5;

		/// TODO remove! /////////////////
		this.add(0, ROWS - 8, 2, false);
		this.add(0, ROWS - 6, 2, false);
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
			Tiles.drawBlock(batch, _tiles[index], _offsetX + _tilePos[index][0], _offsetY + _tilePos[index][1]);
		});

		// draw movable blocks/bombs
		_tilesMove.forEach(tile -> Tiles.drawBlock(batch, tile.id, tile.x, tile.y));
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
		float conveyorSpeed = _moveSpeedConveyor * delta * (-1);
		float playerSpeed = _moveSpeedPlayerTiles * delta;

		_tilesMove.forEach(tile -> tile.updateY(tile.moveUp ? playerSpeed : conveyorSpeed));

		// hold removable items in separate list to avoid null-pointers
		List<Movable> remove = new ArrayList<>();

		// movables -> fixated when (only the ones moving from top to bottom)
		_tilesMove.stream().filter(tile -> !tile.moveUp).forEach(tile -> {
			int idx = (int) ((tile.x - _offsetX) / Tiles.TILESIZE);

			// 1. tile reached bottom line
			if (tile.y <= _offsetY) {
				_tiles[idx * ROWS] = tile.id;
				remove.add(tile);
			}
			// 2. tile reached fixed block
			else {
				int idy = (int) ((tile.y - _offsetY) / Tiles.TILESIZE);
//				if (idy < 0)
//					return; // bottom line already checked
//
//				if (idy >= ROWS)
//					return; // TODO: handle full columns / game over

				int index = this.getIndexByPosition(idx, idy);
				int indexAbove = this.getIndexByPosition(idx, idy + 1);
				
				// something went wrong if one index is invalid
				if(index == -1 || indexAbove == -1) {
					// TODO: handle error
					return;
				}

				if (_tilePos[index][1] + Tiles.TILESIZE >= tile.y - _offsetY && _tiles[index] != -1) {
					_tiles[indexAbove] = tile.id;
					remove.add(tile);
				}
			}
		});

		// movables -> fixated when (only the ones moving from bottom to top)
		_tilesMove.stream().filter(tile -> tile.moveUp).forEach(tile -> {
			int idx = (int) ((tile.x - _offsetX) / Tiles.TILESIZE);
			int idy = (int) ((tile.y - _offsetY) / Tiles.TILESIZE);
			int id = idx * ROWS + idy;

			// fixate tile if tile is over an empty tile and
			if (_tiles[id] == -1) {
				// tile doesn't overlap any other down moving
				// movable tile
				long count = _tilesMove.stream().filter(move -> !move.moveUp).filter(other -> {
					return tile.x >= other.x && tile.x <= other.x + Tiles.TILESIZE && tile.y >= other.y
							&& tile.y <= other.y + Tiles.TILESIZE;
				}).count();

				// no overlapping means -> fixate tile
				if (count == 0) {
					_tiles[id] = tile.id;
					remove.add(tile);
				}
			}
		});

		// remove items stored in 'remove'
		_tilesMove.removeAll(remove);

		// TODO check destroyables
		// 0. create list to hold all destroyables
		List<Integer> indexesDestroy = new ArrayList<>();

		// 1. search for bombs
		// 2. check all to the bomb connected blocks, if they have the same
		// color and if there are minimal three of it in a row/col
		for (int x = 0; x < COLS; x++) {
			for (int y = 0; y < ROWS; y++) {
				int index = this.getIndexByPosition(x, y);

				// check for valid index
				// invalid index -> nothing to do
				if (index == -1)
					continue;

				// no bomb -> nothing to do
				if (_tiles[index] < 10)
					continue;

				// index already marked as destroyed -> nothing to do
				if (indexesDestroy.contains(index))
					continue;

				// check all vertical neighbors
				List<Integer> indexesVertical = new ArrayList<>();

				int curX = x;
				int curIndex = this.getIndexByPosition(curX, y);

				// 1. columns from x to 0
				while (!(curIndex == -1)
						&& (_tiles[curIndex] == _tiles[index] || _tiles[curIndex] == _tiles[index] - 10)) {
					indexesVertical.add(curIndex);

					curX--;
					curIndex = this.getIndexByPosition(curX, y);
				}

				curX = x + 1;
				curIndex = this.getIndexByPosition(curX, y);

				// 2. columns from x + 1 to COLS - 1
				while (!(curIndex == -1)
						&& (_tiles[curIndex] == _tiles[index] || _tiles[curIndex] == _tiles[index] - 10)) {
					indexesVertical.add(curIndex);

					curX++;
					curIndex = this.getIndexByPosition(curX, y);
				}

				// add vertical indexes if there are minimum three in a line
				if (indexesVertical.size() > 2)
					indexesDestroy.addAll(indexesVertical);

				// check all horizontal neighbors
				List<Integer> indexesHorizontal = new ArrayList<>();

				int curY = y;
				curIndex = this.getIndexByPosition(x, curY);
				// 1. rows from y to 0
				while (!(curIndex == -1)
						&& (_tiles[curIndex] == _tiles[index] || _tiles[curIndex] == _tiles[index] - 10)) {
					indexesHorizontal.add(curIndex);

					curY--;
					curIndex = this.getIndexByPosition(x, curY);
				}

				curY = y + 1;
				curIndex = this.getIndexByPosition(x, curY);
				// 2. rows from y + 1 to ROWS - 1
				while (!(curIndex == -1)
						&& (_tiles[curIndex] == _tiles[index] || _tiles[curIndex] == _tiles[index] - 10)) {
					indexesHorizontal.add(curIndex);

					curY++;
					curIndex = this.getIndexByPosition(x, curY);
				}

				// add horizontal indexes if there are minimum three in a line
				if (indexesHorizontal.size() > 2)
					indexesDestroy.addAll(indexesHorizontal);
			}
		}

		// destroy destroyables
		indexesDestroy.forEach(index -> {
			// TODO: animate destruction
			_tiles[index] = -1;
		});

		// if there are destroyed blocks, everything else should fall down
		if (indexesDestroy.size() > 0) {
			for (int index = 0; index < _tiles.length; index++) {
				Movable m = new Movable(_tiles[index], _offsetX + _tilePos[index][0], _offsetY + _tilePos[index][1],
						false);
				_tiles[index] = -1;

				_tilesMove.add(m);
			}
		}
	}

	/**
	 * TODO: describe function
	 * 
	 * @param x
	 * @param y
	 * @return index defined by x and y, or -1
	 */
	private int getIndexByPosition(int x, int y) {
		if (x < 0 || x >= COLS || y < 0 || y >= ROWS)
			return -1;

		return x * ROWS + y;
	}

	/**
	 * Adds a new block to arena.
	 * 
	 * @param column
	 * @param row
	 * @param id
	 */
	public void add(int column, int row, int id, boolean moveUp) {
		Movable tile = new Movable(id, _offsetX + column * Tiles.TILESIZE, _offsetY + row * Tiles.TILESIZE, moveUp);
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
		this.add(column, ROWS, block, false);
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
		this.add(column, 0, block, true);
		// int index = column * ROWS;
		// _tiles[index] = block;
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

		/** TODO: describe moveUp */
		public boolean moveUp;

		/**
		 * @param id
		 * @param x
		 * @param y
		 */
		public Movable(int id, float x, float y, boolean moveUp) {
			this.id = id;
			this.x = x;
			this.y = y;
			this.moveUp = moveUp;
		}

		/**
		 * @param diffY
		 */
		public void updateY(float diffY) {
			this.y += diffY;
		}
	}
}
