package de.pixlpommes.jam.units;

import de.pixlpommes.jam.units.base.Ability;
import de.pixlpommes.jam.units.base.Unit;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.0
 */
public class Slime extends Unit {

	/**
	 * 
	 */
	public Slime() {
		// create unit
		super(5, 50);
		
		// TODO add abilities
		_abilities.add(Ability.createAttack(2, 1));
		
		// TODO remove
		for(Ability a : _abilities) {
			System.out.println("adds ability: " +a);
		}
	}

}
