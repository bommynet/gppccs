package de.pixlpommes.jam.units.base;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.0 - 0
 */
public class Debuff {
	
	/** TODO: describe '_type' */
	protected Type _type;
	
	/** TODO: describe '_isActive' */
	protected boolean _isActive;
	
	/** TODO: describe '_duration' */
	protected float _duration;
	
	/** TODO: describe '_damagePerTick' */
	protected float _damagePerTick;
	
	/** Standard constructor not allowed for others! */
	private Debuff() {
		_type = Type.POISON;
		_isActive = false;
		_duration = 0.0f;
		_damagePerTick = 0.0f;
	}
	
	/**
	 * Create debuff of given type.
	 * @param type
	 */
	public Debuff(Type type) {
		this();
		_type = type;
	}

	
	
	
	/** TODO: describe debuffids */
	public static enum Type { 
		POISON, SILENCE, PARALYZE;
	}
}
