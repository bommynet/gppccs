package de.pixlpommes.conn3bomb.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.pixlpommes.conn3bomb.GameApp;

/**
 * <p>
 * TODO: short class description.
 * </p>
 *
 * <p>
 * TODO: detailed class description.
 * </p>
 *
 * @author Thomas Borck
 */
public class DesktopLauncher {

    /**
     * @param arg
     */
    public static void main(String[] arg) {
        // create game configuration
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 800;
        config.height = 800;

        // start game
        new LwjglApplication(new GameApp(), config);
    }
}
