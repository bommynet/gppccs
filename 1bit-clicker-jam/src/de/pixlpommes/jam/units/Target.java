package de.pixlpommes.jam.units;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.0 - 0
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
