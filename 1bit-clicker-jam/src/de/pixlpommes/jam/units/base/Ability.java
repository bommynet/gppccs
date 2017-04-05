package de.pixlpommes.jam.units.base;

/**
 * Basic structure for abilities.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.2
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
	
	/** TODO: describe '_damage' */
	protected float _damage;
	
	/** TODO: describe '_mpCosts' */
	protected float _mpCosts;

	
	
	public Ability(String name, Type type, TargetType targetType, float useTime, float dmg, float mpCosts) {
		setName(name);
		setType(type);
		setTargetType(targetType);
		setUseTime(useTime);
		setDamage(dmg);
		setMpCosts(mpCosts);
	}
	
	
	
	public String getName() { return _name; }
	public float getUseTime() { return _useTime; }
	public Type getType() { return _type; }
	public TargetType getTargetType() { return _targetType; }
	public float getDamage() { return _damage; }
	public float getMpCosts() { return _mpCosts; }
	
	/**
	 * Set a name for the ability.
	 * 
	 * @param name
	 */
	public void setName(String name) { 
		if(name == null)
			_name = "N/A";
		else
			_name = name;
	}
	
	/**
	 * Set using time for the ability.
	 * 
	 * @param time values between [0,inf) are accepted, negative values will be translated to positive ones
	 */
	public void setUseTime(float time) {
		_useTime = Math.abs(time);
	}
	
	/**
	 * Sets type of the ability.
	 * 
	 * @param type type to set, if null then type = {@link Ability.Type.MELEE}
	 */
	public void setType(Type type) {
		_type = type == null ? Type.MELEE : type;
	}
	
	/**
	 * Sets target type of the ability.
	 * 
	 * @param type target type to set, if null then type = {@link Ability.TargetType.POINT}
	 */
	public void setTargetType(TargetType type) {
		_targetType = type == null ? Ability.TargetType.POINT : type;
	}
	
	/**
	 * Sets damage of the ability.
	 * 
	 * @param dmg >0 does damage, <0 does heal, =0 does nothing
	 */
	public void setDamage(float dmg) {
		_damage = dmg;
	}
	
	/**
	 * Sets mp costs of the ability.
	 * 
	 * @param mpCosts
	 */
	public void setMpCosts(float mpCosts) {
		_mpCosts = mpCosts;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Ability['"+ _name + "', "
				+ _type + ", "
				+ _damage + ", "
				+ _useTime + "sec, "
				+ _targetType + "]";
	}
	
	
	/**  */
	public static enum Type {
		/** melee attacks can reach the first column of enemies only */
		MELEE,
		
		/** range attacks can reach both columns directly, except the healers position */
		RANGE,
		
		/** magic attacks can reach every position */
		MAGIC;
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
		float checkedDamage = Math.abs(damage);
		
		return new Ability(
				"Attack",
				Ability.Type.MELEE,
				Ability.TargetType.POINT,
				useTime,
				checkedDamage,
				0.0f);
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
		tmp.setName("Range");
		tmp.setType(Ability.Type.RANGE);
		return tmp;
	}
	
	/**
	 * @param useTime
	 * @param heal
	 * @return
	 */
	public static Ability createHeal(float useTime, float heal) {
		// healing has to be negative
		float checkedHeal = -Math.abs(heal);
		
		return new Ability(
				"Heal",
				Ability.Type.MAGIC,
				Ability.TargetType.POINT,
				useTime,
				checkedHeal,
				1.0f);
	}
}
