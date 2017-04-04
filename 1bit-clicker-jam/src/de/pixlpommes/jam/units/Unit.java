package de.pixlpommes.jam.units;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.0 - 0
 */
public abstract class Unit {

	/** TODO: describe '_hpCurrent' */
	protected float _hpCurrent, _hpMax;
	
	/** TODO: describe '_mpCurrent' */
	protected float _mpCurrent, _mpMax;
	
	/** TODO: describe '_abilities' */
	protected List<Ability> _abilities;
	
	/** TODO: describe '_debuffs' */
	protected Debuff[] _debuffs;
	
	/** TODO: describe '_position' */
	protected Position _position;
	
	/**
	 * @param hp
	 * @param mp
	 * @param position
	 */
	protected Unit(int hp, int mp, Position position) {
		_hpCurrent = _hpMax = hp;
		_mpCurrent = _mpMax = mp;
		_position = position;
		
		_abilities = new ArrayList<>();
		_debuffs = new Debuff[] {
				new Debuff(Debuff.Type.POISON),
				new Debuff(Debuff.Type.SILENCE),
				new Debuff(Debuff.Type.PARALYZE)
		};
	}
}
