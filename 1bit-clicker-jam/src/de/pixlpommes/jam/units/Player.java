package de.pixlpommes.jam.units;

import de.pixlpommes.jam.units.base.Position;
import de.pixlpommes.jam.units.base.Unit;

/**
 * The player unit.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Player extends Unit {

	/**
	 * 
	 */
	protected Player() {
		// create unit
		super(50, 50, new Position(0, 3));
		
		// TODO add abilities
		
		// TODO make controllable by player
	}

}
