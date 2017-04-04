package de.pixlpommes.jam.actions;

import de.pixlpommes.jam.units.base.Ability;
import de.pixlpommes.jam.units.base.Target;
import de.pixlpommes.jam.units.base.Unit;

/**
 * An action started by one unit.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Action {

	/** TODO: describe '_actor' */
	protected Unit _actor;
	
	/** TODO: describe '_target' */
	protected Target _target;
	
	/** TODO: describe '_ability' */
	protected Ability _ability;
	
	/** TODO: describe '_timer' */
	protected float _timer;
	
	/** TODO: describe '_isDone' */
	protected boolean _isDone;

	
	public Action(Unit actor, Target target, Ability ability) {
		setActor(actor);
		setTarget(target);
		setAbility(ability);
		setTimer(ability.getUseTime());
		_isDone = false;
	}
	
	
	
	public void doTick(float delta) {
		_timer -= delta;
		
		// do not execute abilities to early
		if(_timer > 0) return;
		
		// execute ability
		/// TODO execute algorithm
		/// for each target 
		///    -> get unit
		///    -> sub ability.damage from target.unit.hp
		
		// mark action as done and removable
		_isDone = true;
	}
	
	
	
	
	public Unit getActor() {
		return _actor;
	}

	public void setActor(Unit actor) {
		this._actor = actor;
	}

	public Target getTarget() {
		return _target;
	}

	public void setTarget(Target target) {
		this._target = target;
	}

	public Ability getAbility() {
		return _ability;
	}

	public void setAbility(Ability ability) {
		this._ability = ability;
	}

	public float getTimer() {
		return _timer;
	}

	public void setTimer(float timer) {
		this._timer = timer;
	}
	
	public boolean isDone() {
		return _isDone;
	}
	
}
