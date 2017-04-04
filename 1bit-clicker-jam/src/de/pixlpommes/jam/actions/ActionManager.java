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
	 * @param action
	 */
	public void add(Action action) {
		_actions.add(action);
	}
	
	
	/**
	 * @param delta
	 */
	public void doTick(float delta) {
		// do ticks for all actions available
		// run reverse through list to kill done entries
		for(int i=_actions.size()-1; i>=0; i--) {
			_actions.get(i).doTick(delta);
		}
	}
}
