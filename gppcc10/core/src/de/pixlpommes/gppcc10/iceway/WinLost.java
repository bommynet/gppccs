package de.pixlpommes.gppcc10.iceway;

/**
 * Handle win/lost situation outside the iceway it self.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 1.0
 */
public interface WinLost {

	/**
	 * Player won the game.
	 */
	public void gameWon();
	
	/**
	 * Player lost the game.
	 */
	public void gameLost();
}
