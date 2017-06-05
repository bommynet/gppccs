package de.pixlpommes.conn3bomb.screens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

import de.pixlpommes.conn3bomb.ScreenObject;
import de.pixlpommes.conn3bomb.game.ArenaGui;

/**
 * <p>
 * TODO: short class description.
 * </p>
 *
 * <p>
 * TODO: detailed class description.
 * </p>
 *
 * @author Thomas Borck - http://www.pixlpommes.de
 */
public class ScoreAnimation extends ScreenObject {

	// STATICS
	/** TODO: describe 'TIME_MOVE_UP' */
	private final static float TIME_MOVE_UP = 0.5f;

	/** TODO: describe 'TIME_MOVE_TO_GUI' */
	private final static float TIME_MOVE_TO_GUI = 0.5f;

	/** TODO: describe 'MOVE_UP_BY' */
	private final static float MOVE_UP_BY = 100;

	// REFERENCES
	/** TODO: describe '_gui' */
	private ArenaGui _gui;

	// ANIMATION
	/** TODO: describe '_timer' */
	private float _timer;

	/** TODO: describe '_score' */
	private long _score;

	/** TODO: describe '_state' */
	private State _state;

	/** TODO: describe '_targetY' */
	private float _targetX, _targetY;

	/**
	 * @param score
	 * @param x
	 * @param y
	 */
	public ScoreAnimation(ArenaGui gui, long score, float x, float y) {
		_gui = gui;
		_timer = TIME_MOVE_UP;
		_score = score;
		_state = State.ANIMATE_UP;
		_targetY = y + MOVE_UP_BY;

		setOffset(x, y);
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
		if(_state == State.FINISHED) return;
		
		ShapeRenderer sr = new ShapeRenderer();
		sr.setProjectionMatrix(batch.getProjectionMatrix());
		
		batch.end();
		sr.begin(ShapeType.Filled);
		
		sr.setColor(1, 0, 0, 1);
		sr.rect(_offsetX, _offsetY, 10, 3);
		
		sr.end();
		batch.begin();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.pixlpommes.conn3bomb.ScreenObject#update(float)
	 */
	@Override
	public void update(float delta) {
		switch (_state) {
		case ANIMATE_UP:
			// state condition
			if (_timer < 0) {
				_timer = TIME_MOVE_TO_GUI;
				_targetX = _gui.getOffsetX();
				_targetY = _gui.getOffsetY();
				_state = State.ANIMATE_TO_GUI;
			}

			// update position
			_offsetY = MathUtils.lerp(_offsetY, _targetY, 0.05f);

			// update timer
			_timer -= delta;
			break;

		case ANIMATE_TO_GUI:
			// state condition
			if (_timer < 0) {
				_timer = 0;
				_state = State.FINISHED;
			}

			// update position
			_offsetX = MathUtils.lerp(_offsetX, _targetX, 0.1f);
			_offsetY = MathUtils.lerp(_offsetY, _targetY, 0.1f);

			// update timer
			_timer -= delta;
			break;

		case FINISHED:
			break;
		}
	}

	/**
	 * TODO: describe function
	 * 
	 * @return
	 */
	public long getScore() {
		return _score;
	}

	/**
	 * TODO: describe function
	 * 
	 * @return
	 */
	public boolean isFinished() {
		return _state == State.FINISHED;
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
	 * @author Thomas Borck - http://www.pixlpommes.de
	 */
	private enum State {
		ANIMATE_UP,

		ANIMATE_TO_GUI,

		FINISHED;
	}
}
