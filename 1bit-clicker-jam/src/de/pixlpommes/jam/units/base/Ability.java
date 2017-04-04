package de.pixlpommes.jam.units.base;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.0 - 0
 */
public class Ability {
	
	/** TODO: describe '_useTime' */
	protected float _useTime;
	
	/** TODO: describe '_type' */
	protected Type _type;
	
	/** TODO: describe '_targetType' */
	protected TargetType _targetType;
	
	
	
	

	
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
}
