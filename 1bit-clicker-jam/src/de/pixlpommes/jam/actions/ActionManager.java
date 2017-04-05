package de.pixlpommes.jam.actions;

import java.util.ArrayList;
import java.util.List;

import de.pixlpommes.jam.arena.Arena;
import de.pixlpommes.jam.units.base.Position;
import de.pixlpommes.jam.units.base.Unit;

/**
 * Manage actions for all units.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class ActionManager {
	
	/** TODO: describe '_arena' */
	private Arena _arena;

	/** TODO: describe '_actions' */
	private List<Action> _actions;
	
	
	/**
	 * 
	 */
	public ActionManager(Arena arena) {
		_arena = arena;
		_actions = new ArrayList<>();
	}
	
	
	/**
	 * Add a new action to this manager.
	 * 
	 * @param action
	 * @return
	 */
	public boolean add(Action action) {
		if(action == null)
			return false;
		
		if(_actions.contains(action))
			return false;
		
		return _actions.add(action);
	}
	
	
	/**
	 * @param delta
	 */
	public void doTick(float delta) {
		// do ticks for all actions available
		// run reverse through list to kill done entries
		for(int i=_actions.size()-1; i>=0; i--) {
			// set reference for better readability
			Action act = _actions.get(i);
			
			// update action
			act.doTick(delta);
			
			// timer is done -> execute ability
			if(act.isDone()) {
				/// reduce unit mp
				act.getActor().updateMpCurrent(-act.getAbility().getMpCosts());
				/// for each target
				for(Position p : act.getTarget().get()) {
					/// -> get unit
					Unit u = _arena.getUnit(p.getX(), p.getY());
					if(u == null) continue;
					
					/// -> sub ability.damage from target.unit.hp
					switch(act.getAbility().getType()) {
						// TODO check for valid range
						// TODO check for damage or debuff abilities
						case RANGE:
						case MELEE:
						case MAGIC:
							u.updateHpCurrent(-act.getAbility().getDamage());
					}
				}
				
				// after all remove action
				_actions.remove(i);
			}
		}
	}
}
