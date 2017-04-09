package de.pixlpommes.jam.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.pixlpommes.jam.OneBitClicker;

/**
 * Desktop game launcher.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 1.0
 */
public class DesktopLauncher {
	
	public static void main (String[] arg) {
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		// setup  basic game
		config.width = 640;
		config.height = 480;
		config.resizable = false;
		
		// start game
		new LwjglApplication(new OneBitClicker(), config);
	}
}
