package de.pixlpommes.conn3bomb;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
        _tilesMoveSpeed = (float) Tiles.TILESIZE / 2f;
        ;

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
        float currentSpeed = _tilesMoveSpeed * delta;
        _tilesMove.forEach(tile -> tile.updateY(currentSpeed));

        // hold removable items in separate list to avoid null-pointers
        List<Movable> remove = new ArrayList<>();

        // movables -> fixated when (only the ones moving from top to bottom)
        _tilesMove.stream()
                .filter(tile -> !tile.moveUp)
                .forEach(tile -> {
                    int idx = (int) ((tile.x - _offsetX) / Tiles.TILESIZE);

                    // 1. tile reached bottom line
                    if (tile.y <= _offsetY) {
                        _tiles[idx * ROWS] = tile.id;
                        remove.add(tile);
                    }
                    // 2. tile reached fixed block
                    else {
                        int idy = (int) ((tile.y - _offsetY) / Tiles.TILESIZE);
                        if (idy < 0)
                            return; // bottom line already checked

                        int index = idx * ROWS + idy;

                        if (_tilePos[index][1] + Tiles.TILESIZE >= tile.y
                                - _offsetY
                                && _tiles[index] != -1) {
                            _tiles[index + 1] = tile.id;
                            remove.add(tile);
                        }
                    }
                });

        // movables -> fixated when (only the ones moving from bottom to top)
        _tilesMove.stream()
                .filter(tile -> tile.moveUp)
                .forEach(tile -> {
                    int idx = (int) ((tile.x - _offsetX) / Tiles.TILESIZE);
                    int idy = (int) ((tile.y - _offsetY) / Tiles.TILESIZE);
                    int id = idx * ROWS + idy;

                    // fixate tile if tile is over an empty tile and
                    if (_tiles[id] == -1) {
                        // tile doesn't overlap any other down moving
                        // movable tile
                        Stream<Movable> stream = _tilesMove.stream()
                                .filter(move -> !move.moveUp)
                                .filter(tile2 -> {
                                    return tile.x >= tile2.x
                                            && tile.x <= tile2.x
                                                    + Tiles.TILESIZE
                                            && tile.y >= tile2.y
                                            && tile.y <= tile2.y
                                                    + Tiles.TILESIZE;
                                });

                        if (stream.count() == 0) {
                            _tiles[id] = tile.id;
                            remove.add(tile);
                        }
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
    public void add(int column, int row, int id, boolean moveUp) {
        Movable tile = new Movable(id, _offsetX + column * Tiles.TILESIZE,
                _offsetY + row * Tiles.TILESIZE, moveUp);
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
            this.y += (moveUp ? 5 * diffY : -diffY);
        }
    }
}
