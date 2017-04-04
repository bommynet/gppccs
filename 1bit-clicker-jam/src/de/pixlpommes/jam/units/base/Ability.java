package de.pixlpommes.jam.units.base;

/**
 * Basic structure for abilities.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Ability {

	/** TODO: describe '_name' */
	protected String _name;

	/// ** TODO: describe '_icon' */
	// protected Icon _icon;

	/** TODO: describe '_useTime' */
	protected float _useTime;

	/** TODO: describe '_type' */
	protected Type _type;

	/** TODO: describe '_targetType' */
	protected TargetType _targetType;

	
	
	public Ability(String name, Type type, TargetType targetType, float useTime, float dmg) {
		_name = name;
		_type = type;
		_targetType = targetType;
		_useTime = useTime;
		/// TODO setup dmg or heal for ability
	}
	
	
	
	public String getName() { return _name; }
	public float getUseTime() { return _useTime; }
	public Type getType() { return _type; }
	public TargetType getTargetType() { return _targetType; }
	
	public void setName(String name) { _name = name; }
	public void setUseTime(float time) { _useTime = time; }
	public void setType(Type type) { _type = type; }
	public void setTargetType(TargetType type) { _targetType = type; }

	
	
	/**  */
	public static enum Type {
		MELEE, RANGE, MAGIC, HEAL;
	}

	/**  */
	public static enum TargetType {
		/** one field */
		POINT,

		/** one complete column */
		LINE_I,

		/** one complete row (enemy or party, not all) */
		LINE,

		/** all enemies OR all party-members */
		ALL,

		/** all units (enemies, party and player) */
		GLOBAL;
	}

	
	
	/**
	 * Create standard attack.
	 * 
	 * This attack is a single target melee attack with given
	 * values for using time and damage.
	 * 
	 * @param useTime
	 * @param damage
	 * @return
	 */
	public static Ability createAttack(float useTime, float damage) {
		return new Ability(
				"Attack",
				Ability.Type.MELEE,
				Ability.TargetType.POINT,
				useTime,
				damage
				);
	}
	
	/**
	 * Create standard attack.
	 * 
	 * This attack is a single target range attack with given
	 * values for using time and damage.
	 * 
	 * @param useTime
	 * @param damage
	 * @return
	 */
	public static Ability createRangeAttack(float useTime, float damage) {
		Ability tmp = Ability.createAttack(useTime, damage);
		tmp.setType(Ability.Type.RANGE);
		return tmp;
	}
}
