package de.pixlpommes.gppcc10;

import com.badlogic.gdx.Game;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 1.0
 */
public class Gppcc10 extends Game {
	
	/** game screen width */
	public final static int WIDTH = 960;
	
	/** game screen height */
	public final static int HEIGHT = 768;
	
	/** half game screen width */
	public final static int HALF_WIDHT = WIDTH/2;
	
	/** half game screen width */
	public final static int HALF_HEIGHT = HEIGHT/2;
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create () {
		setScreen(new IcewayScreen());
	}
}
