package de.pixlpommes.jam.screens.ui;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.pixlpommes.jam.arena.Arena;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class ArenaUI {

	/** TODO: describe '_unit' */
	private UnitPanel[] _units;
	
	
	
	/**
	 * 
	 */
	public ArenaUI() {
		_units = new UnitPanel[Arena.COLUMNS * Arena.ROWS];
		for(int i=0; i<_units.length; i++) {
			_units[i] = null;
		}
	}
	
	/**
	 * @param batch
	 */
	public void draw(Batch batch) {
		
		// draw all unit panels
		for(UnitPanel unit : _units) {
			if(unit != null) unit.draw(batch);
		}
		
	}
}
