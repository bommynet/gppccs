package de.pixlpommes.conn3bomb;

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
	public final static int COLS = 6;
	public final static int ROWS = 12;

	/**
	 * 
	 */
	public Arena() {
		this.setOffset(0, 0);
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
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.pixlpommes.conn3bomb.ScreenObject#update(float)
	 */
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub

	}

}
