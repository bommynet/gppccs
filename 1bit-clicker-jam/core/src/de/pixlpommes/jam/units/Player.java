package de.pixlpommes.jam.units;

import de.pixlpommes.jam.units.base.Ability;
import de.pixlpommes.jam.units.base.Unit;

/**
 * The player unit.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Player extends Unit {

	/**
	 * Create the player.
	 */
	public Player() {
		// create unit
		super(50, 50);
		
		// TODO add abilities
		_abilities.add(Ability.createHeal(2, 1));
		
		// TODO make controllable by player
		for(Ability a : _abilities) {
			System.out.println("create button for: " +a);
		}
	}

}
