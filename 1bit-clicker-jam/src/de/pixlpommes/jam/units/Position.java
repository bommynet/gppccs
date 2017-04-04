package de.pixlpommes.jam.units;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.0 - 0
 */
public class Position {
	
	/** TODO: describe '_x' */
	protected int _x, _y;
	
	/**
	 * 
	 */
	public Position() {
		this(0, 0);
	}
	
	/**
	 * @param x
	 * @param y
	 */
	public Position(int x, int y) {
		_x = x;
		_y = y;
	}
	
	/**
	 * @return
	 */
	public int getX() { return _x; }
	
	/**
	 * @return
	 */
	public int getY() { return _y; }
	
	/**
	 * @return
	 */
	public int[] get() { return new int[]{_x, _y}; }
}
