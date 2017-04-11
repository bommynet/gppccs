package de.pixlpommes.jam.screens.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import de.pixlpommes.jam.arena.Arena;
import de.pixlpommes.jam.units.base.Unit;

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
	
	/**
	 * @param unit
	 * @param x
	 * @param y
	 */
	public void setObserver(Unit unit, int x, int y) {
		int index = y*Arena.COLUMNS + x;
		
		// TODO positioning by col & row
		float posX = x * 80;
		float posY = y * 80;
		UnitPanel pan = new UnitPanel(posX, posY);
		_units[index] = pan;
		
		unit.deleteObservers();
		unit.addObserver(pan);
	}
}
