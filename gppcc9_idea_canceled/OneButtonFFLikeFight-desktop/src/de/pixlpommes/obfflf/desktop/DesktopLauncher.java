package de.pixlpommes.obfflf.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.pixlpommes.obfflf.Config;
import de.pixlpommes.obfflf.OneButtonFFFight;

/**
 * 
 * This class was created as part of my contribution to the #gppcc9.
 * @author Thomas Borck
 */
public class DesktopLauncher {
	
	/**
	 * @param arg
	 */
	public static void main (String[] arg) {
		// config desktop application
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = Config.WIDTH;
		config.height = Config.HEIGHT;
		config.resizable = false;
		
		// start game
		new LwjglApplication(new OneButtonFFFight(), config);
	}
}
