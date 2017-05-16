package de.pixlpommes.conn3bomb;

import com.badlogic.gdx.graphics.g2d.Batch;

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

    /** TODO: describe _state */
    private State _state;

    /** TODO: describe _timerDelay */
    private float _timer, _timerDelay;

    /**
     * 
     */
    public Inserter() {
	this.setOffset(0, 0);

	_timer = _timerDelay = 2f;
	_state = State.COUNTDOWN;
    }

    @Override
    public void draw(Batch batch) {
	// TODO Auto-generated method stub

    }

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
	    // TODO: start throw-in-animation
	    _state = State.ANIMATE;
	    break;

	case ANIMATE:
	    // TODO: animate block throw-in
	    // TODO: if animation ends, reset timer for next round
	    break;

	case RESET:
	    // TODO: transfer block to arena
	    // reset timer
	    _timer = _timerDelay;
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
