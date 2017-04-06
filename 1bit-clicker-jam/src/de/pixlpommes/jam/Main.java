package de.pixlpommes.jam;

import de.pixlpommes.jam.actions.ActionManager;
import de.pixlpommes.jam.arena.Arena;
import de.pixlpommes.jam.units.Player;

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
		
		a.setPlayer(new Player());
		System.out.println(a);
	}

}
