package de.pixlpommes.jam.actions;

import java.util.ArrayList;
import java.util.List;

import de.pixlpommes.jam.arena.Arena;
import de.pixlpommes.jam.units.base.Ability;
import de.pixlpommes.jam.units.base.Position;
import de.pixlpommes.jam.units.base.Target;
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
	 * Creates a new action and adds it to the manager.
	 * 
	 * @param actor
	 * @param ability
	 * @param targetX
	 * @param targetY
	 * @return
	 */
	public Action create(Unit actor, Ability ability, int targetX, int targetY) {
		/// TODO: calculate target area by (targetX,targetY) and ability.target
		Target target = new Target();
		target.add(targetX, targetY);
		
		// create action
		Action action = new Action(actor, target, ability);
		
		// add reference to actor
		boolean check = actor.setActiveAction(action);
		
		// if action can't be set as units active action, cancel create()
		if(!check)
			return null;
		
		// add new action to manager
		add(action);
		return action;
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
				System.out.println("Fire action: " + act.getAbility());
				
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
				act.getActor().resetActiveAction();
				_actions.remove(i);
			}
		}
	}
}
