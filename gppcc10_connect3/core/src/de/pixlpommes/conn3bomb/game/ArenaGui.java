package de.pixlpommes.conn3bomb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.pixlpommes.conn3bomb.GameApp;
import de.pixlpommes.conn3bomb.ScreenObject;

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
public class ArenaGui extends ScreenObject {
	
	/** TODO: describe '_score' */
	private long _score;
	
	/** TODO: describe '_comboTimer' */
	private float _comboTimer;
	
	/** TODO: describe '_comboFactor' */
	private float _comboFactor;

	/**
	 * @param app
	 *            the game app
	 */
	public ArenaGui(final GameApp app) {
		this.setOffset(0, 0);
		
		// score
		_score = 0;
		_comboTimer = 0f;
		_comboFactor = 1f;
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
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.pixlpommes.conn3bomb.ScreenObject#update(float)
	 */
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO: describe function
	 * @return
	 */
	public float getComboFactor() {
		return _comboFactor;
	}

	/**
	 * TODO: describe function
	 * @param score
	 */
	public void addScore(long score) {
		// TODO animate score adding?
		
		// add score to current score
		_score += score;
		Gdx.app.log("Score", ""+_score);
	}
}
