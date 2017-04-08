package de.pixlpommes.jam;

import de.pixlpommes.jam.actions.ActionManager;
import de.pixlpommes.jam.arena.Arena;
import de.pixlpommes.jam.units.Player;
import de.pixlpommes.jam.units.Slime;
import de.pixlpommes.jam.units.base.Unit;

/**
 * Game entry point.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Main {

	/**
	 * @param args
	 */
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
		
		
		long timer = System.currentTimeMillis();
		
		while(true) {
			// delta timer
			float delta = 0.016f;
			///System.out.println(delta);
			
			// check for inactive party memebrs
			for(Unit party : a.getPartyMembers()) {
				if(party != null && !party.hasActiveAction()) {
					// get first enemy and attack it
					for(int id : Arena.IDS_ENEMY) {
						if(a.getUnit(id) != null) {
							// attack this unit
							int x = id % Arena.COLUMNS;
							int y = (int) Math.floor(id / Arena.COLUMNS);
							System.out.println(am.create(party, party.getAbility(0), x, y));
						}
					}
				}
			}
			
			// update action manager
			am.doTick(delta); 
			
			
			// update delta
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
