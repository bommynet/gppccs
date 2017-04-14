package de.pixlpommes.jam.screens.ui;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.pixlpommes.jam.arena.Arena;
import de.pixlpommes.jam.units.Player;
import de.pixlpommes.jam.units.base.Ability;
import de.pixlpommes.jam.units.base.Unit;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class ArenaUI {

	public final static int[] UNITROW = new int[]{ 130, 30, -70 };
	public final static int[] UNITCOL = new int[]{ -300, -160, -60, 100, 200 };
	
	public final static int   BUTTONS = 6;
	public final static int[] BUTTONROW = new int[]{ -160, -220 };
	public final static int[] BUTTONCOL = new int[]{ -300, -240, -180 };
	
	/** TODO: describe '_unit' */
	private UnitPanel[] _units;
	
	/** TODO: describe '_buttons' */
	private PlayerButton[] _buttons;
	
	
	
	/**
	 * 
	 */
	public ArenaUI() {
		// setup unit panels
		_units = new UnitPanel[Arena.COLUMNS * Arena.ROWS];
		for(int x=0; x<Arena.COLUMNS; x++) {
			for(int y=0; y<Arena.ROWS; y++) {
				// skip panel above and below player
				if(x == 0 && (y == 0 || y == 2)) continue;
				
				// create unit panels
				int index = y*Arena.COLUMNS + x;
				float posX = UNITCOL[x];
				float posY = UNITROW[y];
				
				UnitPanel pan = new UnitPanel(posX, posY);
				_units[index] = pan;
			}
		}
		
		// setup player buttons
		_buttons = new PlayerButton[BUTTONS];
		for(int x=0; x<BUTTONCOL.length; x++) {
			for(int y=0; y<BUTTONROW.length; y++) {
				// create inactive buttons (placeholder)
				int index = y*BUTTONCOL.length + x;
				int posX = BUTTONCOL[x];
				int posY = BUTTONROW[y];
				
				PlayerButton btn = new PlayerButton(posX, posY);
				_buttons[index] = btn;
			}
		}
	}
	
	/**
	 * @param batch
	 */
	public void draw(Batch batch) {
		
		// draw unit panels
		for(UnitPanel unit : _units) {
			if(unit != null)
				unit.draw(batch);
		}
		
		// draw player buttons
		for(PlayerButton button : _buttons) {
			if(button != null)
				button.draw(batch);
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
	 * @param unit
	 */
	public void setPlayer(Unit unit) {
		List<Ability> abilities = unit.getAbilities();
		int count = abilities.size();
		count = (count > BUTTONS) ? BUTTONS : count;
		
		for(int i=0; i<BUTTONS; i++) {
			String[] files;
			
			if(i<count) {
				files = new String[]{
						"button_" + abilities.get(i).getName().toLowerCase() + "_0.png",
						"button_" + abilities.get(i).getName().toLowerCase() + "_1.png"
				};
			} else {
				files = new String[]{
						"button_inactive_0.png",
						"button_inactive_0.png"
				};
			}
			
			_buttons[i].set(
					files[0],
					files[1],
					(i < count) ? abilities.get(i) : null);
		}
		
		// add unit to panels
		setUnit(unit, 0, 1);
	}
	
	/**
	 * @param index
	 */
	public void removeUnit(int index) {
		_units[index].reset();
	}
}
