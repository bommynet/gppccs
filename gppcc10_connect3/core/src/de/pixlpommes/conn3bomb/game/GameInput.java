package de.pixlpommes.conn3bomb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;

/**
 * <p>
 * Game input handler.
 * </p>
 *
 * @author Thomas Borck - http://www.pixlpommes.de
 */
public class GameInput implements InputProcessor {

	/** TODO: describe '_player' */
	private final Player _player;

	/** TODO: describe '_arena' */
	private final Arena _arena;

	/**
	 * @param player
	 */
	public GameInput(final Arena arena, final Player player) {
		_arena = arena;
		_player = player;

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.A) {
			_player.moveBy(-1);
		} else if (keycode == Keys.D) {
			_player.moveBy(1);
		} else if (keycode == Keys.SPACE || keycode == Keys.CONTROL_LEFT) {
			int tile = keycode == Keys.SPACE ? _player.fireTileId() : _player.fireTileId() + 10;
			int col = _player.getColumn();
			_arena.addBottom(col, tile);
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT || button == Buttons.RIGHT) {
			int tile = button == Buttons.LEFT ? _player.fireTileId() : _player.fireTileId() + 10;
			int col = _player.getColumn();
			_arena.addBottom(col, tile);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// get current column
		float curX = screenX - (Gdx.graphics.getWidth() / 2) - _arena.getOffsetX();
		int pos = (int) ((float) curX / (float) Arena.TILESIZE);

		// move player
		_player.moveTo(pos);

		return false;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
