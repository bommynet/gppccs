package de.pixlpommes.jam.actions;

import java.util.ArrayList;
import java.util.List;

/**
 * Manage actions for all units.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class ActionManager {

	/** TODO: describe '_actions' */
	private List<Action> _actions;
	
	
	/**
	 * 
	 */
	public ActionManager() {
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
			// remove done actions
			if(_actions.get(i).isDone())
				_actions.remove(i);
			
			// update action
			_actions.get(i).doTick(delta);
		}
	}
}
