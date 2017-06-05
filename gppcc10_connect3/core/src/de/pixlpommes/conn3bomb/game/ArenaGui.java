package de.pixlpommes.conn3bomb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

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
	
	/** maximal time for combos */
	private static final float COMBO_TIMER_MAX = 5f;

	/** increase combo factor by this value */
	private static final float COMBO_FACTOR_INC = 0.5f;

	/** TODO: describe '_score' */
	private long _score;
	
	/** TODO: describe '_comboTimer' */
	private float _comboTimer;
	
	/** TODO: describe '_comboFactor' */
	private float _comboFactor;
	
	/** TODO: describe '_fntScore' */
	private BitmapFont _fntScore;

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
		
		_fntScore = app.assets.get("fonts/score.fnt", BitmapFont.class);
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
		// score
		_fntScore.draw(batch, "SCORE", _offsetX, _offsetY + 70);
		_fntScore.draw(batch, ""+_score, _offsetX, _offsetY);
		
		// combo
		_fntScore.draw(batch, "COMBO", _offsetX, _offsetY + 340);
		_fntScore.draw(batch, ""+_comboTimer, _offsetX, _offsetY + 270);
		_fntScore.draw(batch, "x"+_comboFactor, _offsetX, _offsetY + 200);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.pixlpommes.conn3bomb.ScreenObject#update(float)
	 */
	@Override
	public void update(float delta) {
		_comboTimer -= delta;
		
		if(_comboTimer < 0) {
			_comboFactor = 1.0f;
			_comboTimer = 0;
		}
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

	/**
	 * TODO: describe function
	 */
	public void firedCombo() {
		// TODO flag -> flash, to show timer was refreshed
		
		_comboTimer = COMBO_TIMER_MAX;
		_comboFactor += COMBO_FACTOR_INC;
	}
}
