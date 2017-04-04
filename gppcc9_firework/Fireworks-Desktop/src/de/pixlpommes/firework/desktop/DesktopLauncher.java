package de.pixlpommes.firework.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.pixlpommes.firework.Config;
import de.pixlpommes.firework.GoFirework;

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
		
		config.width     = Config.WIDTH;
		config.height    = Config.HEIGHT;
		config.resizable = Config.RESIZEABLE;
		config.title     = Config.TITLE;
		
		// start game
		new LwjglApplication(new GoFirework(), config);
	}
}
