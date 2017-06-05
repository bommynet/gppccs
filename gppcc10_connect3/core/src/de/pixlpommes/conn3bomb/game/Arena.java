package de.pixlpommes.conn3bomb.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.pixlpommes.conn3bomb.GameApp;
import de.pixlpommes.conn3bomb.ScreenObject;
import de.pixlpommes.conn3bomb.screens.ScoreListener;

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
 * @author Thomas Borck - http://www.pixlpommes.de
 */
public class Arena extends ScreenObject {

	// GLOBALS
	/** TODO: describe COLS */
	public final static int COLS = 6;

	/** TODO: describe ROWS */
	public final static int ROWS = 12;

	/** TODO: describe 'TILESIZE' */
	public final static int TILESIZE = 48;

	/** TODO: describe 'COLORS_COUNT' */
	public final static int COLORS_COUNT = 3;

	// LIBGDX
	/** the game app */
	private final GameApp _app;

	// GAME
	/** <i>fixed</i> tiles */
	private List<Tile> _tilesFixed;

	/** downwards <i>moving</i> tiles */
	private List<Tile> _tilesDown;

	/** upwards <i>moving</i> tiles */
	private List<Tile> _tilesUp;

	/** speed for all tiles on conveyor band (pixels per second) */
	private float _moveSpeedConveyor;

	/** speed for all tiles fired by player (pixels per second) */
	private float _moveSpeedPlayerTiles;

	/** show and animate explosions */
	private Explosion _explosion;

	/** TODO: describe '_scoreListeners' */
	private List<ScoreListener> _scoreListeners;

	/**
	 * 
	 */
	public Arena(GameApp app, int offsetX, int offsetY) {
		_app = app;
		this.setOffset(offsetX, offsetY);

		// initialize tiles: fixed - predefined with position and id = -1
		_tilesFixed = new ArrayList<>(ROWS * COLS);
		IntStream.range(0, ROWS * COLS).forEach(index -> {
			int idx = index / ROWS;
			int idy = index % ROWS;

			float posX = idx * TILESIZE; // x
			float posY = idy * TILESIZE; // y
			int id = -1;

			Tile tile = new Tile(posX, posY, id);
			_tilesFixed.add(index, tile);
		});

		// initialize tiles: movable - empty lists
		_tilesDown = new ArrayList<>();
		_tilesUp = new ArrayList<>();

		// initialize explosion handler
		_explosion = new Explosion();

		// listeners
		_scoreListeners = new ArrayList<>();

		// setup speed
		_moveSpeedConveyor = (float) TILESIZE / 2f;
		_moveSpeedPlayerTiles = (float) TILESIZE * 5;

		/// TODO remove! /////////////////
		this.add(0, ROWS - 8, 2, false);
		this.add(0, ROWS - 6, 2, false);
		//////////////////////////////////
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

		// draw fixated blocks/bombs
		_tilesFixed.stream().forEach(tile -> drawBlock(tile.id, _offsetX + tile.x, _offsetY + tile.y));

		// draw movable blocks/bombs
		_tilesDown.forEach(tile -> drawBlock(tile.id, _offsetX + tile.x, _offsetY + tile.y));
		_tilesUp.forEach(tile -> drawBlock(tile.id, _offsetX + tile.x, _offsetY + tile.y));

		_explosion.draw(batch, _offsetX, _offsetY);
	}

	/**
	 * TODO: describe function
	 * 
	 * @param tileId
	 * @param x
	 * @param y
	 */
	private void drawBlock(int tileId, float x, float y) {
		// tile id == -1 -> invisible, so don't draw it
		if (tileId == -1)
			return;

		int tilePosX = (tileId > 9 ? (tileId - 10 + 1) : (tileId + 1)) * TILESIZE;
		int tilePosY = (tileId > 9 ? 2 : 1) * TILESIZE;

		// Texture tiles = _app.assets.get("tiles.png", Texture.class);
		Texture tiles = _app.assets.get("tiles.png");

		_app.batch.draw(tiles, // tile set file
				x, y, // position on screen
				tilePosX, tilePosY, // tile position in file
				TILESIZE, TILESIZE// tile size
		);
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

		_tilesDown.forEach(tile -> tile.y += conveyorSpeed);
		_tilesUp.forEach(tile -> tile.y += playerSpeed);

		// hold removable items in separate list to avoid null-pointers
		List<Tile> remove = new ArrayList<>();

		// movables (top-down) -> fixated, if
		_tilesDown.stream().forEach(tile -> {
			int idx = (int) (tile.x / TILESIZE);

			// 1. tile reached bottom line
			if (tile.y <= 0) {
				_tilesFixed.get(idx * ROWS).id = tile.id;
				remove.add(tile);
			}
			// 2. tile reached fixed block
			else {
				int idy = (int) (tile.y / TILESIZE);
				// if (idy < 0)
				// return; // bottom line already checked
				//
				// if (idy >= ROWS)
				// return; // TODO: handle full columns / game over

				int index = this.getIndexByPosition(idx, idy);
				int indexAbove = this.getIndexByPosition(idx, idy + 1);

				// something went wrong if one index is invalid
				if (index == -1 || indexAbove == -1) {
					// TODO: handle error
					return;
				}

				if (_tilesFixed.get(index).y + TILESIZE >= tile.y && _tilesFixed.get(index).id != -1) {
					// lose if top tile is not empty!
					if (_tilesFixed.get(indexAbove).id != -1) {
						Gdx.app.log("Column full", "" + idx); /// TODO remove
																/// log
					}

					_tilesFixed.get(indexAbove).id = tile.id;
					remove.add(tile);
				}
			}
		});

		// movables (bottom-up) -> fixated, if
		_tilesUp.stream().forEach(tile -> {
			int idx = (int) (tile.x / TILESIZE);
			int idy = (int) (tile.y / TILESIZE);

			int id = this.getIndexByPosition(idx, idy);

			if (id == -1) {
				// TODO: something went wrong
				return;
			}

			// 1. tile is over an empty tile and
			if (_tilesFixed.get(id).id == -1) {
				// tile doesn't overlap any other down moving movable tile
				long count = _tilesDown.stream().filter(down -> {
					// overlapping when over the same cell id
					int otherIdx = (int) (down.x / TILESIZE);
					int otherIdy = (int) (down.y / TILESIZE);
					return idx == otherIdx && idy == otherIdy;
				}).count();

				// no overlapping means -> fixate tile
				if (count == 0) {
					_tilesFixed.get(id).id = tile.id;
					remove.add(tile);
				}
			}
		});

		// remove items stored in 'remove'
		_tilesDown.removeAll(remove);
		_tilesUp.removeAll(remove);

		// update explosions
		_explosion.update(delta);

		// check for destroyables
		List<Integer> indexesDestroy = new ArrayList<>();
		// indexesDestroy.addAll(this.findDestroyable_RowsAndCols());
		indexesDestroy.addAll(this.findDestroyable_Connected());

		// destroy destroyables
		indexesDestroy.forEach(index -> {
			// add explosion
			float x = _tilesFixed.get(index).x;
			float y = _tilesFixed.get(index).y;
			int color = _tilesFixed.get(index).id;
			color = color > 9 ? color - 10 : color;
			_explosion.add(x, y, color);

			// remove block
			_tilesFixed.get(index).id = -1;
		});

		// if there are destroyed blocks, everything else should fall down
		if (indexesDestroy.size() > 0) {
			// score!
			long score = indexesDestroy.size() * 10;
			// first destroyable is a bomb! everytime!
			float x = _tilesFixed.get(indexesDestroy.get(0)).x + _offsetX; 
			float y = _tilesFixed.get(indexesDestroy.get(0)).y + _offsetY;
			_scoreListeners.stream().forEach(listener -> listener.scored(score, x, y));

			// let fall blocks down
			for (int index = 0; index < _tilesFixed.size(); index++) {
				if (_tilesFixed.get(index).id == -1)
					continue;

				Tile tile = new Tile(_tilesFixed.get(index).x, _tilesFixed.get(index).y, _tilesFixed.get(index).id);
				_tilesDown.add(tile);

				_tilesFixed.get(index).id = -1;
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
		Tile tile = new Tile(column * TILESIZE, row * TILESIZE, id);

		if (moveUp) {
			_tilesUp.add(tile);
		} else if (!moveUp) {
			_tilesDown.add(tile);
		}
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
	}

	/**
	 * TODO: describe function
	 * 
	 * @param listener
	 */
	public void addScoreListener(ScoreListener listener) {
		_scoreListeners.add(listener);
	}

	/**
	 * Find destroyable blocks.
	 * 
	 * If there is a bomb connected to one or more blocks of the same color,
	 * then destroy all these blocks. Blocks of the same color that are
	 * connected to connected blocks, will be destroyed to.
	 * 
	 * @return a list of indexes of destroybale blocks
	 */
	public List<Integer> findDestroyable_Connected() {
		// store destroyables
		List<Integer> indexesDestroy = new ArrayList<>();

		for (int x = 0; x < COLS; x++) {
			for (int y = 0; y < ROWS; y++) {
				int index = this.getIndexByPosition(x, y);

				if (_tilesFixed.get(index).id > 9 && !indexesDestroy.contains(index)) {
					this.findConnectedTo(indexesDestroy, x, y, -1);
				}
			}
		}

		return indexesDestroy;
	}

	/**
	 * TODO: describe function
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private void findConnectedTo(List<Integer> list, int x, int y, int type) {
		int locType = type;
		int index = this.getIndexByPosition(x, y);

		// index invalid
		if (index == -1)
			return;

		// no type selected currently
		if (locType == -1)
			locType = _tilesFixed.get(index).id;
		// current block is in the list already
		else if (list.contains(index))
			return;
		// types are equal -> add it to the list
		else if (locType == _tilesFixed.get(index).id || locType - 10 == _tilesFixed.get(index).id)
			list.add(index);
		// nothing to do anymore
		else
			return;

		this.findConnectedTo(list, x - 1, y, locType); // left neighbor
		this.findConnectedTo(list, x + 1, y, locType); // right neighbor
		this.findConnectedTo(list, x, y - 1, locType); // bottom neighbor
		this.findConnectedTo(list, x, y + 1, locType); // to neighbor
	}

	/**
	 * Find destroyable blocks.
	 * 
	 * If there are three or more blocks in a row or column (including a bomb),
	 * then these lines will be marked as destroyable.
	 * 
	 * @return a list of indexes of destroybale blocks
	 */
	public List<Integer> findDestroyable_RowsAndCols() {
		// store destroyables
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
				if (_tilesFixed.get(index).id < 10)
					continue;

				// index already marked as destroyed -> nothing to do
				if (indexesDestroy.contains(index))
					continue;

				// check all vertical neighbors
				List<Integer> indexesVertical = new ArrayList<>();

				int curX = x;
				int curIndex = this.getIndexByPosition(curX, y);

				// 1. columns from x to 0
				while (!(curIndex == -1) && (_tilesFixed.get(curIndex).id == _tilesFixed.get(index).id
						|| _tilesFixed.get(curIndex).id == _tilesFixed.get(index).id - 10)) {
					indexesVertical.add(curIndex);

					curX--;
					curIndex = this.getIndexByPosition(curX, y);
				}

				curX = x + 1;
				curIndex = this.getIndexByPosition(curX, y);

				// 2. columns from x + 1 to COLS - 1
				while (!(curIndex == -1) && (_tilesFixed.get(curIndex).id == _tilesFixed.get(index).id
						|| _tilesFixed.get(curIndex).id == _tilesFixed.get(index).id - 10)) {
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
				while (!(curIndex == -1) && (_tilesFixed.get(curIndex).id == _tilesFixed.get(index).id
						|| _tilesFixed.get(curIndex).id == _tilesFixed.get(index).id - 10)) {
					indexesHorizontal.add(curIndex);

					curY--;
					curIndex = this.getIndexByPosition(x, curY);
				}

				curY = y + 1;
				curIndex = this.getIndexByPosition(x, curY);
				// 2. rows from y + 1 to ROWS - 1
				while (!(curIndex == -1) && (_tilesFixed.get(curIndex).id == _tilesFixed.get(index).id
						|| _tilesFixed.get(curIndex).id == _tilesFixed.get(index).id - 10)) {
					indexesHorizontal.add(curIndex);

					curY++;
					curIndex = this.getIndexByPosition(x, curY);
				}

				// add horizontal indexes if there are minimum three in a line
				if (indexesHorizontal.size() > 2)
					indexesDestroy.addAll(indexesHorizontal);
			}
		}

		return indexesDestroy;
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
	 * @author Thomas Borck - http://www.pixlpommes.de
	 */
	private class Tile {
		/** position x on screen */
		public float x;

		/** position y on screen */
		public float y;

		/** tile id (color & block/bomb) */
		public int id;

		/**
		 * @param id
		 * @param x
		 * @param y
		 */
		public Tile(float x, float y, int id) {
			this.id = id;
			this.x = x;
			this.y = y;
		}
	}
}
