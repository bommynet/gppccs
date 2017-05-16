package de.pixlpommes.conn3bomb;

import java.util.stream.IntStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
    
    // GRAPHICS
    /** TODO: describe TILESIZE */
    public final static int TILESIZE = 48;
    
    /** TODO: describe TILES */
    public final static Texture TILES = new Texture(Gdx.files.internal("tiles.png"));

    // GAME
    /** TODO: describe _tiles */
    private int[] _tiles;

    /**
     * 
     */
    public Arena() {
	this.setOffset(0, 0);

	// setup game data
	_tiles = new int[COLS * ROWS];
	IntStream.range(0, _tiles.length).forEach(index -> _tiles[index] = -1);
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
	// TODO draw blocks/bombs
	IntStream.range(0, _tiles.length).forEach(index -> {
	    if(_tiles[index] != -1) {
		
	    }
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
	// TODO check destroyables
    }

}
