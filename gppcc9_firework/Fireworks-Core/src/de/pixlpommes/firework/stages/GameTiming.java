package de.pixlpommes.firework.stages;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.pixlpommes.firework.Highscore;
import de.pixlpommes.firework.font.RetroFont;
import de.pixlpommes.firework.types.Firework;
import de.pixlpommes.firework.types.Timing;

/**
 * <p>Game: let fireworks explode as high as possible.</p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public class GameTiming extends Stage {
	
	/** global gravity for all objects */
	public final static Vector2 gravity = new Vector2(0, -0.2f);
	
	/** the fireworks */
	private List<Firework> _fireworks;
	
	/** font for UI */
	private BitmapFont _font;
	
	/** countdown before game starts */
	private float _countDown;
	
	/** possible game states */
	public static enum State {
		/** player is ready? */
		PREPARE,
		
		/** countdown before game starts */
		COUNTDOWN,
		
		/** game running */
		RUNNING,
		
		/** game over */
		GAMEOVER;
	}
	
	/** current game state */
	private State _state;
	
	/** retro bitmap as a font */
	private RetroFont _rfont;
	
	protected float _launchTimer;
	
	protected float _launchTimerDefault = 2f;
	
	/**
	 * Create stage.
	 */
	public GameTiming() {
		_fireworks = new ArrayList<Firework>();
		_font = new BitmapFont();
		_font.setFixedWidthGlyphs("0123456789,.:");
		_font.setColor(Color.WHITE);
		
		_rfont = new RetroFont();
		
		_countDown = 3f;
		_launchTimer = _launchTimerDefault;
		
		_state = State.PREPARE;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.scenes.scene2d.Stage#draw()
	 */
	public void draw() {
		State next = null;
		
		// add rockets in an interval
		if(_launchTimer > 0) {
			_launchTimer -= Gdx.graphics.getDeltaTime();
		} else {
			addFirework();
			_launchTimer = _launchTimerDefault;
		}
		
		switch(_state) {
			case PREPARE:
				drawPrepare(); 
				break;
				
			case COUNTDOWN:
				_countDown -= Gdx.graphics.getDeltaTime();
				if(_countDown < 0) {
					_countDown = 0;
					next = State.RUNNING;
				}
				drawCountdown();
				break;
				
			case RUNNING:
				// draw game state
				drawGame();
				drawGameUI();
				break;
				
			case GAMEOVER:
				drawGame();
				drawGameOver();
				break;
				
			default:
				// TODO: should never be reached...remove it?
				break;
		}
		
		// set new state
		if(next != null) {
			_state = next;
			drawFlash();
		}
	}
	
	/**
	 * draw routine for player preparing
	 */
	public void drawPrepare() {
		Batch batch = getBatch();
		batch.begin();
		_rfont.draw(batch, "READY?",
				(int)(Gdx.graphics.getWidth()/2f),
				(int)(Gdx.graphics.getHeight() / 2f),
				true,
				true);
		batch.end();
	}
	
	/**
	 * draw routine for count down before start
	 */
	public void drawCountdown() {
		int h = (int)Math.floor(Gdx.graphics.getHeight() * 0.15f);
		
		// get 'character' by count down value
		int c = (int)Math.ceil(_countDown);
		
		Batch batch = getBatch();
		batch.begin();
		_rfont.draw(batch, ""+c,
				(int)(Gdx.graphics.getWidth()/2f),
				(int)(Gdx.graphics.getHeight() / 2f),
				h, h,
				true,
				true);
		batch.end();
	}
	
	/**
	 * draw routine for game play
	 */
	public void drawGame() {
		ShapeRenderer sr = new ShapeRenderer();
		sr.begin(ShapeType.Filled);
		
		// update and remove fireworks
		for(int i = 0; i < _fireworks.size(); i++) {
			_fireworks.get(i).update();
			_fireworks.get(i).draw(sr);
			
			if(_fireworks.get(i).isDone()) {
				_fireworks.remove(i);
			}
		}
		
		sr.end();
		sr.dispose();
	}
	
	/**
	 * draws information on UI
	 */
	public void drawGameUI() {
		// prepare strings
		
		// prepare font size
		int fontWidth = 15;
		int fontHeight = 30;
		
		// draw ui
		SpriteBatch sb = new SpriteBatch();
		sb.begin();
//		_rfont.draw(sb,
//				uitext, // text to draw
//				Gdx.graphics.getWidth()/2, // x position
//				(int)(Gdx.graphics.getHeight()-fontHeight*1.2), // y position
//				fontWidth, fontHeight, // font size in width, height
//				true, false); // center font on x-axis, but not on y-axis
		sb.end();
		sb.dispose();
	}
	
	/**
	 * draw routine for game stats after game ends
	 */
	public void drawGameOver() {
		// format floating number to two decimals
		String fps = String.format("%.5g%n", 0);
		fps.trim();
		fps = fps.substring(0, fps.indexOf(',')+3);
		fps = fps.replace(',', '.');
		
		// position of the first line
		int posY = 350;
		int posX = 60;
		
		// next line at
		int diffY = 50;
		
		Batch batch = getBatch();
		batch.begin();
		_rfont.draw(batch, "Fireworks", posX, posY);
		posY -= diffY;
		_rfont.draw(batch, "   total: "+ 0, posX, posY);
		posY -= diffY;
		_rfont.draw(batch, " at once: "+ 0, posX, posY);
		posY -= diffY;
		_rfont.draw(batch, "     fps: "+fps, posX, posY);
		batch.end();
		
		// count down 'til user input is accepted again
		_countDown -= Gdx.graphics.getDeltaTime();
		if(_countDown < 0) _countDown = 0;
	}
	
	/**
	 * TODO: describe behavior
	 */
	private void drawFlash() {
		ShapeRenderer sr = new ShapeRenderer();
		sr.begin(ShapeType.Filled);
		
		sr.setColor(Color.WHITE);
		sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		sr.end();
		sr.dispose();
	}
	
	/**
	 * handle user input
	 * @return true if game ended
	 */
	public boolean pushedOneButton() {
		switch(_state) {
			case PREPARE: 
				// start countdown
				_state = State.COUNTDOWN;
				break;
				
			case RUNNING: 
				if(_fireworks.size() > 0) 
					((Timing)_fireworks.get(0)).doExplosion();
				break;
				
			case GAMEOVER:
				// let stage handler know, the game is over
				return _countDown == 0;
				
			default:
				// do nothing on user input at state
				//  - COUNTDOWN
				break;
		}
		
		// every time return false, if game's not over
		return false;
	}
	
	/**
	 * Adds new and random fireworks.
	 */
	public void addFirework() {
		_fireworks.add(new Timing());
	}
	
	/**
	 * TODO: describe behavior
	 * @return
	 */
	public Highscore.Entry getEntry() {
		// create highscore after the game ends 
		if(_state != State.GAMEOVER) return null;
		
		Highscore.Entry entry = new Highscore.Entry();
		return entry;
	}
}
