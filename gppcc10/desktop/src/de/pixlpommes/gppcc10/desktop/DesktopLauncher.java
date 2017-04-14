package de.pixlpommes.gppcc10.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.pixlpommes.gppcc10.Gppcc10;

/**
 * LibGDX desktop launcher for #gppcc10 project.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 1.0
 */
public class DesktopLauncher {
	
	public static void main (String[] arg) {
		// config game
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Gppcc10.WIDTH;
		config.height = Gppcc10.HEIGHT;
		config.resizable = false;
		
		// create game window
		new LwjglApplication(new Gppcc10(), config);
	}
}
