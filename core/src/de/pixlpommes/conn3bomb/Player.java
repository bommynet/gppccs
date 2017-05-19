package de.pixlpommes.conn3bomb;

import java.util.stream.IntStream;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;

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

	/** TODO: describe _pos */
	private int _pos;

	/** TODO: describe _blocks */
	private int _tile;

	/**
	 * 
	 */
	public Player(Arena arena) {
		this.setOffset(0, 0);

		_arena = arena;

		_pos = Arena.COLS / 2;

		_tile = -1;

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
		// TODO: draw player
		batch.draw(Arena.TILES, _offsetX + _pos * Arena.TILESIZE, _offsetY,
				3 * Arena.TILESIZE, 2 * Arena.TILESIZE, Arena.TILESIZE,
				Arena.TILESIZE);

		// draw block
		if (_tile != -1) {
			batch.draw(Arena.TILES, _offsetX + _pos * Arena.TILESIZE, _offsetY,
					_tile * Arena.TILESIZE, 0, Arena.TILESIZE, Arena.TILESIZE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.pixlpommes.conn3bomb.ScreenObject#update(float)
	 */
	@Override
	public void update(float delta) {
		if (_tile < 0)
			_tile = (int) (Math.random() * 3);
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
		} else if (keycode == Keys.D) {
			_pos++;
			if (_pos >= Arena.COLS)
				_pos = Arena.COLS - 1;
		} else if (keycode == Keys.SPACE) {
			_arena.addBottom(_pos, _tile);
			_tile = -1;
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
