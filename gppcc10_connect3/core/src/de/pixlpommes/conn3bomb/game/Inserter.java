package de.pixlpommes.conn3bomb.game;

import java.util.stream.IntStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.pixlpommes.conn3bomb.ScreenObject;
import de.pixlpommes.conn3bomb.Tiles;

/**
 * <p>
 * The inserter.
 * </p>
 *
 * <p>
 * This object is located above the arena and throws in new blocks and bombs in
 * given intervals.
 * </p>
 *
 * @author Thomas Borck
 */
public class Inserter extends ScreenObject {

	/** reference to game arena */
	private Arena _arena;

	/** TODO: describe _state */
	private State _state;

	/** TODO: describe _timerDelay */
	private float _timer, _timerDelay;

	private float _timerY, _timerYStep;

	/** TODO: describe _blocks */
	private int[] _tiles;

	/** TODO: describe '_texture' */
	private Texture _texture, _band;

	/**
	 * 
	 */
	public Inserter(Arena arena, Texture texture, Texture band) {
		this.setOffset(0, 0);

		_arena = arena;

		_texture = texture;
		_band = band;

		_timer = _timerDelay = 2f;
		_state = State.COUNTDOWN;

		_tiles = new int[Arena.COLS];
		IntStream.range(0, _tiles.length).forEach(i -> _tiles[i] = -1);
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
		// draw landing area
		IntStream.range(0, _tiles.length).forEach(index -> {
			drawConvoyerBand(batch, 0, _offsetX + index * Tiles.TILESIZE, _offsetY, 1f);
		});

		// draw blocks
		if (_state == State.ANIMATE) {
			float factor = _timer / _timerDelay;

			IntStream.range(0, _tiles.length).forEach(index -> {
				if (_tiles[index] == -1)
					return;

				// draw shadow
				float shadow = Arena.TILESIZE / 2f - Arena.TILESIZE / 2f * (1f - factor);

				drawConvoyerBand(batch, 5, shadow + _offsetX + index * Arena.TILESIZE, shadow + _offsetY, 1f - factor);

				// draw falling block
				drawBlock(batch, // batch to draw in
						_tiles[index], // block id
						_offsetX + index * Tiles.TILESIZE, _timerY, // position
																	// on screen
						1f + factor * 0.75f // scaling
				);
			});
		} else {
			IntStream.range(0, _tiles.length).forEach(index -> {
				drawBlock(batch, _tiles[index], _offsetX + index * Tiles.TILESIZE, _offsetY, 1f);
			});
		}
	}

	/**
	 * TODO: describe function
	 * 
	 * @param frame
	 * @param x
	 * @param y
	 */
	private void drawConvoyerBand(Batch batch, int frame, float x, float y, float sizeFactor) {
		float size = Arena.TILESIZE * sizeFactor;

		batch.draw(_band, // tile set file
				x, y, // position on screen
				size, size, // drawing tile sized
				frame * Arena.TILESIZE, 0, // tile position in file
				Arena.TILESIZE, Arena.TILESIZE, // tile size
				false, false // mirroring
		);
	}

	/**
	 * TODO: describe function
	 * 
	 * @param batch
	 * @param id
	 * @param x
	 * @param y
	 * @param sizeFactor
	 */
	public void drawBlock(Batch batch, int id, float x, float y, float sizeFactor) {
		// tile id == -1 -> invisible, so don't draw it
		if (id == -1)
			return;

		int tilePosX = (id > 9 ? (id - 10 + 1) : (id + 1)) * Arena.TILESIZE;
		float size = Arena.TILESIZE * sizeFactor;

		batch.draw(_texture, // tile set file
				x, y, // position on screen
				size, size, // drawing tile sized
				tilePosX, Arena.TILESIZE, // tile position in file
				Arena.TILESIZE, Arena.TILESIZE, // tile size
				false, false // mirroring
		);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.pixlpommes.conn3bomb.ScreenObject#update(float)
	 */
	@Override
	public void update(float delta) {
		// update FSM
		switch (_state) {
		case COUNTDOWN:
			if (_timer < 0) {
				_state = State.CREATE;
			} else {
				_timer -= delta;
			}
			break;

		case CREATE:
			// TODO: create random block/bomb
			int rndIndex = (int) (Math.random() * _tiles.length);
			int rndBlock = (int) (Math.random() * 3);
			_tiles[rndIndex] = rndBlock;

			// TODO: start throw-in-animation
			_timer = _timerDelay; /// remove
			_timerY = Gdx.graphics.getHeight() / 2 + Arena.TILESIZE; // spawn
																		// above
																		// screen
			_timerYStep = (_timerY - _offsetY) / _timerDelay; // falling in in
																// delay-seconds
			_state = State.ANIMATE;
			break;

		case ANIMATE:
			// TODO: animate block throw-in
			// TODO: if animation ends, reset timer for next round
			if (_timer < 0) {
				_state = State.RESET;
			} else {
				_timerY -= _timerYStep * delta;
				_timer -= delta;
			}
			break;

		case RESET:
			// TODO: transfer block to arena
			IntStream.range(0, _tiles.length).forEach(i -> {
				if (_tiles[i] >= 0)
					_arena.addTop(i, _tiles[i]);
				_tiles[i] = -1;
			});

			// reset timer
			/// _timer = _timerDelay;
			// reset timer to random time [1, _timerDelay)
			_timer = (float) (Math.random() * (1 - _timerDelay) + 1);

			// restart timer
			_state = State.COUNTDOWN;
			break;
		}

	}

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
	private static enum State {
		COUNTDOWN,

		CREATE,

		ANIMATE,

		RESET;
	}
}
