package de.pixlpommes.conn3bomb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

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
public class Tiles {

    /** TODO: describe TILESIZE */
    public final static int TILESIZE = 48;

    /** TODO: describe COLORS_COUNT */
    public final static int COLORS_COUNT = 3;

    /** tile set as single file */
    public final static Texture TILES = new Texture(
            Gdx.files.internal("tiles.png"));

    /**
     * @param batch
     * @param x
     * @param y
     */
    public static void drawConvoyer(Batch batch, float x, float y) {
        batch.draw(TILES, // tile set file
                x, y, // position on screen
                0, 2 * Tiles.TILESIZE, // tile position in file
                Tiles.TILESIZE, Tiles.TILESIZE // tile size
        );
    }

    /**
     * @param batch
     * @param id
     * @param x
     * @param y
     */
    public static void drawBlock(Batch batch, int id, float x, float y) {
        // tile id == -1 -> invisible, so don't draw it
        if (id == -1)
            return;

        int tilePosX = (id > 9 ? (id - 10 + 1) : (id + 1)) * Tiles.TILESIZE;
        int tilePosY = (id > 9 ? 2 : 1) * Tiles.TILESIZE;

        batch.draw(TILES, // tile set file
                x, y, // position on screen
                tilePosX, tilePosY, // tile position in file
                Tiles.TILESIZE, Tiles.TILESIZE// tile size
        );
    }

    /**
     * @param batch
     * @param id
     * @param x
     * @param y
     */
    public static void drawBlockThumbnail(Batch batch, int id, float x,
            float y) {
        // tile id == -1 -> invisible, so don't draw it
        if (id == -1)
            return;

        int tilePosX = (id + 1) * Tiles.TILESIZE;

        batch.draw(TILES, // tile set file
                x, y, // position on screen
                TILESIZE / 2, TILESIZE / 2, // drawing tile sized
                tilePosX, Tiles.TILESIZE, // tile position in file
                Tiles.TILESIZE, Tiles.TILESIZE, // tile size
                false, false // mirroring
        );
    }

    /**
     * @param batch
     * @param x
     * @param y
     */
    public static void drawPlayer(Batch batch, float x, float y) {
        batch.draw(TILES, // tile set file
                x, y, // position on screen
                3 * Tiles.TILESIZE, 0 * Tiles.TILESIZE, // tile position in file
                TILESIZE, TILESIZE// tile size
        );
    }
}
