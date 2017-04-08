package de.pixlpommes.jam;

import de.pixlpommes.jam.actions.ActionManager;
import de.pixlpommes.jam.arena.Arena;
import de.pixlpommes.jam.units.Player;
import de.pixlpommes.jam.units.Slime;

/**
 * Game entry point.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Main {

	public static void main(String[] args) {
		Arena a = new Arena();
		ActionManager am = new ActionManager(a);
		
		// setup player
		a.setPlayer(new Player());
		
		// setup simple party member
		a.setUnit(new Slime(), 2, 1);
		
		// setup simple enemy
		a.setUnit(new Slime(), 3, 1);
		
		
		System.out.println(a);
	}

}
