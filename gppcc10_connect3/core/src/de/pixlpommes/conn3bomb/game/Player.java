package de.pixlpommes.conn3bomb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.pixlpommes.conn3bomb.ScreenObject;
import de.pixlpommes.conn3bomb.Tiles;

/**
 * <p>
 * The player.
 * </p>
 *
 * <p>
 * This entity is controlled by the user itself. It could move on the lower
 * border of the arena and is able to throw in blocks and bombs to change the
 * arenas state.
 * </p>
 *
 * @author Thomas Borck
 */
public class Player extends ScreenObject implements InputProcessor {

    /** reference to game arena */
    private Arena _arena;

    /** current column the player stands at */
    private int _pos;

    /** TODO: describe _blocks */
    private int[] _tile;

    /**
     * 
     */
    public Player(Arena arena) {
        this.setOffset(0, 0);

        _arena = arena;

        _pos = Arena.COLS / 2;

        _tile = new int[] {
                (int) (Math.random() * Tiles.COLORS_COUNT),
                (int) (Math.random() * Tiles.COLORS_COUNT),
                (int) (Math.random() * Tiles.COLORS_COUNT)
        };

        Gdx.input.setInputProcessor(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.pixlpommes.conn3bomb.ScreenObject#draw(com.badlogic.gdx.graphics.g2d.
     * Batch)
     */
    @Override
    public void draw(Batch batch) {
        // draw blocks
        Tiles.drawBlock(batch, _tile[0], _offsetX + _pos * Tiles.TILESIZE,
                _offsetY);
        Tiles.drawBlockThumbnail(batch, _tile[1],
                _offsetX + (_pos + 1) * Tiles.TILESIZE,
                _offsetY - 0.25f * Tiles.TILESIZE);
        Tiles.drawBlockThumbnail(batch, _tile[2],
                _offsetX + (_pos + 1.5f) * Tiles.TILESIZE,
                _offsetY - 0.25f * Tiles.TILESIZE);

        // draw player
        Tiles.drawPlayer(batch, _offsetX + _pos * Tiles.TILESIZE, _offsetY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.pixlpommes.conn3bomb.ScreenObject#update(float)
     */
    @Override
    public void update(float delta) {
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.badlogic.gdx.InputProcessor#keyDown(int)
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.A) {
            _pos--;
            if (_pos < 0)
                _pos = 0;
        }
        else if (keycode == Keys.D) {
            _pos++;
            if (_pos >= Arena.COLS)
                _pos = Arena.COLS - 1;
        }
        else if (keycode == Keys.SPACE) {
            _arena.addBottom(_pos, _tile[0]);
            _tile[0] = _tile[1];
            _tile[1] = _tile[2];
            _tile[2] = (int) (Math.random() * Tiles.COLORS_COUNT);
            
            // 20% chance to add bomb instead of block
            if(Math.random() > 0.8)
                _tile[2] += 10;
        }
        else if (keycode == Keys.CONTROL_LEFT) {
//            _arena.addBottom(_pos, _tile[0] + 10);
//            _tile[0] = _tile[1];
//            _tile[1] = _tile[2];
//            _tile[2] = (int) (Math.random() * Tiles.COLORS_COUNT);
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.badlogic.gdx.InputProcessor#keyUp(int)
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.badlogic.gdx.InputProcessor#keyTyped(char)
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.badlogic.gdx.InputProcessor#touchDown(int, int, int, int)
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer,
            int button) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.badlogic.gdx.InputProcessor#touchUp(int, int, int, int)
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.badlogic.gdx.InputProcessor#touchDragged(int, int, int)
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.badlogic.gdx.InputProcessor#mouseMoved(int, int)
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.badlogic.gdx.InputProcessor#scrolled(int)
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
