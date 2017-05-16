package de.pixlpommes.conn3bomb;

import com.badlogic.gdx.Game;

/**
 * @author Thomas Borck
 */
public class Connect3BombTrack extends Game {
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create () {
		this.setScreen(new GameScreen());
	}
}
