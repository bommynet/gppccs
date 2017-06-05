package de.pixlpommes.conn3bomb.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.pixlpommes.conn3bomb.GameApp;

/**
 * <p>
 * Start desktop/pc version.
 * </p>
 *
 * @author Thomas Borck - http://www.pixlpommes.de
 */
public class DesktopLauncher {

    /**
     * @param arg
     */
    public static void main(String[] arg) {
        // create game configuration
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = GameApp.WIDTH;
        config.height = GameApp.HEIGHT;
        config.resizable = false;

        // start game
        new LwjglApplication(new GameApp(), config);
    }
}
