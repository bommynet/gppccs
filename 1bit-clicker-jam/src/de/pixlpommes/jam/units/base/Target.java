package de.pixlpommes.jam.units.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Target area as list of positions.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Target {

	/** TODO: describe '_positions' */
	protected List<Position> _positions;
	
	/**
	 * 
	 */
	public Target() {
		_positions = new ArrayList<>();
	}
	
	public List<Position> get() { return _positions; }
}
