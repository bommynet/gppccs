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
		
		_textureFiles = new String[] {
			"enemy_slime_0.png", "enemy_slime_1.png", "enemy_slime_2.png",
			"enemy_slime_3.png", "enemy_slime_4.png"
		};
		
		// TODO add abilities
		_abilities.add(Ability.createAttack(2, 1));
		
		// TODO remove
		for(Ability a : _abilities) {
			System.out.println("adds ability: " +a);
		}
	}

}
