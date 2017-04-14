package de.pixlpommes.gppcc10;

import com.badlogic.gdx.Game;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Gppcc10 extends Game {
	
	/** game screen width */
	public final static int WIDTH = 960;
	
	/** game screen height */
	public final static int HEIGHT = 768;
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create () {
		setScreen(new GameScreen());
	}
}
