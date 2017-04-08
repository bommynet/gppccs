package de.pixlpommes.jam.arena;

import java.util.ArrayList;
import java.util.List;

import de.pixlpommes.jam.units.Player;
import de.pixlpommes.jam.units.base.Unit;

/**
 * Fighting arena.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Arena {

	/*
	 *  # GG EE     # = not available
	 *  P GG EE     P = Player
	 *  # GG EE     G = Party, E = Enemy
	 */
	
	/** TODO: describe 'COLUMNS' */
	public static int COLUMNS = 5;
	
	/** TODO: describe 'ROWS' */
	public static int ROWS = 3;
	
	/**
	 * position ids for party members
	 * 
	 * x  1  2 x x
	 * x  6  7 x x
	 * x 11 12 x x
	 */
	public static int[] IDS_PARTY = new int[]{ 1, 2, 6, 7, 11, 12};
	
	/**
	 * position ids for enemies
	 * 
	 * x x x  3  4
	 * x x x  8  9
	 * x x x 13 14
	 */
	public static int[] IDS_ENEMY = new int[]{ 3, 4, 8, 9, 13, 14};
	
	/** TODO: describe '_position' */
	private Unit[] _position;
	
	
	
	/**
	 * 
	 */
	public Arena() {
		_position = new Unit[COLUMNS * ROWS];
		for(int i=0; i<_position.length; i++) {
			_position[i] = null;
		}
	}
	
	
	
	/**
	 * @param unit
	 * @param x
	 * @param y
	 */
	public void setUnit(Unit unit, int x, int y) {
		// TODO: check x and y
		_position[y*COLUMNS + x] = unit;
	}
	
	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public Unit getUnit(int x, int y) {
		// TODO: check x and y
		return _position[y*COLUMNS + x];
	}
	
	/**
	 * @return
	 */
	public List<Unit> getPartyMembers() {
		List<Unit> party = new ArrayList<>();
		
		for(int id : IDS_PARTY)
			party.add(_position[id]);
		
		return party;
	}
	
	/**
	 * @return
	 */
	public List<Unit> getEnemies() {
		List<Unit> enemies = new ArrayList<>();
		
		for(int id : IDS_ENEMY)
			enemies.add(_position[id]);
		
		return enemies;
	}
	
	/**
	 * @param player
	 */
	public void setPlayer(Unit player) {
		setUnit(player, 0, 1);
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "Arena\n-------\n|";
		for(int i=0; i<_position.length; i++) {
			if((i != 0) && (i % COLUMNS == 0))
				str +="|\n|";
			
			if(_position[i] == null)
				str += "#";
			else if(_position[i] instanceof Player)
				str += "P";
			else if(i % COLUMNS > 2)
				str += "E";
			else
				str += "G";
		}
		
		str += "|\n-------";
		return str;
	}
}
