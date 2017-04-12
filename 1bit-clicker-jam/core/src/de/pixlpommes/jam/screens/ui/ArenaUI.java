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

	public final static int[] ROW = new int[]{ 100, 0, -100 };
	public final static int[] COL = new int[]{ -300, -160, -60, 100, 200 };
	
	/** TODO: describe '_unit' */
	private UnitPanel[] _units;
	
	
	
	/**
	 * 
	 */
	public ArenaUI() {
		_units = new UnitPanel[Arena.COLUMNS * Arena.ROWS];
		for(int x=0; x<Arena.COLUMNS; x++) {
			for(int y=0; y<Arena.ROWS; y++) {
				// skip panel above and below player
				if(x == 0 && (y == 0 || y == 2)) continue;
				
				// create unit panels
				int index = y*Arena.COLUMNS + x;
				float posX = COL[x];
				float posY = ROW[y];
				UnitPanel pan = new UnitPanel(posX, posY);
				_units[index] = pan;
			}
		}
	}
	
	/**
	 * @param batch
	 */
	public void draw(Batch batch) {
		
		// draw all unit panels
		for(UnitPanel unit : _units) {
			if(unit != null)
				unit.draw(batch);
		}
		
	}
	
	/**
	 * @param unit
	 * @param x
	 * @param y
	 */
	public void setUnit(Unit unit, int x, int y) {
		int index = y*Arena.COLUMNS + x;
		_units[index].set(unit);
	}
	
	/**
	 * @param index
	 */
	public void removeUnit(int index) {
		_units[index].reset();
	}
}
