package de.pixlpommes.conn3bomb.screens;

/**
 * <p>
 * TODO: short class description.
 * </p>
 *
 * <p>
 * TODO: detailed class description.
 * </p>
 *
 * @author Thomas Borck - http://www.pixlpommes.de
 */
public interface ScoreListener {
	/**
	 * TODO: describe function
	 * @param score
	 * @param x
	 * @param y
	 */
	public void scored(long score, float x, float y);
	
	/**
	 * TODO: describe function
	 */
	public void comboed();
}
