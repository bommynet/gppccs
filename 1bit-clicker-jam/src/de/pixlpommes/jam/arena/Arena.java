package de.pixlpommes.jam.arena;

import de.pixlpommes.jam.units.base.Unit;

/**
 * Fighting arena.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Arena {

	/*
	 *  # GG EE     # = not available
	 *  P GG EE     P = Player
	 *  # GG EE     G = Party, E = Enemy
	 */
	
	/** TODO: describe 'COLUMNS' */
	public static int COLUMNS = 5;
	
	/** TODO: describe 'ROWS' */
	public static int ROWS = 3;
	
	/** TODO: describe '_position' */
	private Unit[] _position;
	
	
	
	/**
	 * 
	 */
	public Arena() {
		_position = new Unit[COLUMNS * ROWS];
		for(int i=0; i<_position.length; i++) {
			_position = null;
		}
	}
	
	
	
	/**
	 * @param unit
	 * @param x
	 * @param y
	 */
	public void setUnit(Unit unit, int x, int y) {
		// TODO: check x and y
		_position[y*COLUMNS + x] = unit;
	}
	
	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public Unit getUnit(int x, int y) {
		// TODO: check x and y
		return _position[y*COLUMNS + x];
	}
	
	/**
	 * @param player
	 */
	public void setPlayer(Unit player) {
		setUnit(player, 0, 1);
	}
}
