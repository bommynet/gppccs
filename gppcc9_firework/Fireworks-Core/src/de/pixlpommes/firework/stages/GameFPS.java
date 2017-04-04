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
import de.pixlpommes.firework.types.Cracker;
import de.pixlpommes.firework.types.DualColor;
import de.pixlpommes.firework.types.Firework;
import de.pixlpommes.firework.types.Simple;
import de.pixlpommes.firework.types.Standard;

/**
 * <p>Game: launch as many fireworks as you can.</p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public class GameFPS extends Stage {
	
	/** global gravity for all objects */
	public final static Vector2 gravity = new Vector2(0, -0.2f);
	
	/** the fireworks */
	private List<Firework> _fireworks;
	
	/** font for UI */
	private BitmapFont _font;
	
	/** start value for countdown */
	private final int COUNTDOWN = 15; 
	
	/** current countdown */
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
	
	/** current overall created fireworks */
	private int _resFireworks;
	
	/** maximum fireworks on screen at the same time */
	private int _resFireworksAtATime;
	
	/** fireworks per second */
	private float _resFireworksPerSecond;
	
	/**
	 * Create stage.
	 */
	public GameFPS() {
		_fireworks = new ArrayList<Firework>();
		_font = new BitmapFont();
		_font.setFixedWidthGlyphs("0123456789,.:");
		_font.setColor(Color.WHITE);
		
		_rfont = new RetroFont();
		
		_countDown = 3;
		_resFireworks = 0;
		_resFireworksAtATime = 0;
		_resFireworksPerSecond = 0;
		
		_state = State.PREPARE;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.scenes.scene2d.Stage#draw()
	 */
	public void draw() {
		State next = null;
		
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
				// update counter
				_countDown -= Gdx.graphics.getDeltaTime();
				if(_countDown < 0) {
					_countDown = 2; // avoid player skips stats accidently
					next = State.GAMEOVER;
					// now we can calc fps
					_resFireworksPerSecond = (float)_resFireworks / (float)COUNTDOWN;
				}
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
			if(next == State.RUNNING) {
				_countDown = COUNTDOWN;
			}
			drawFlash();
		}
	}
	
	/**
	 * draw routine for player preparing
	 */
	public void drawPrepare() {
		//this.drawBanner();
		
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
		// spawn random cracker
		if(_state == State.RUNNING && Math.random() < 0.01) {
			_fireworks.add(new Cracker());
		}
		
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
		// prepare 'countdown' string
		String cd = ""+(int)_countDown;
		if(cd.length() < 2) cd = "0" + cd;
		
		// prepare 'sum' string
		String sum = ""+_resFireworks;
		while(sum.length() < 3) sum = "0" + sum;
		
		// prepare 'at once' string
		String max = ""+_resFireworksAtATime;
		while(max.length() < 2) max = "0" + max;
		
		// concat strings to ui text
		String uitext = "§" + cd + "  $" + sum + "  %" + max;
		
		int fontWidth = 15;
		int fontHeight = 30;
		
		SpriteBatch sb = new SpriteBatch();
		sb.begin();
		_rfont.draw(sb,
				uitext, // text to draw
				Gdx.graphics.getWidth()/2, // x position
				(int)(Gdx.graphics.getHeight()-fontHeight*1.2), // y position
				fontWidth, fontHeight, // font size in width, height
				true, false); // center font on x-axis, but not on y-axis
		sb.end();
		sb.dispose();
	}
	
	/**
	 * draw routine for game stats after game ends
	 */
	public void drawGameOver() {
		// format floating number to two decimals
		String fps = String.format("%.5g%n", _resFireworksPerSecond);
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
		_rfont.draw(batch, "   total: "+_resFireworks, posX, posY);
		posY -= diffY;
		_rfont.draw(batch, " at once: "+_resFireworksAtATime, posX, posY);
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
				this.addRandomFireworks();
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
	public void addRandomFireworks() {
		Standard newFw = null;
		double switchFw = Math.random() * 3.0;
		
		if(switchFw < 1.0) {
			newFw = new Standard();
		} else if (switchFw < 2.0) {
			newFw = new DualColor();
		} else {
			newFw = new Simple();
		}
		
		_fireworks.add(newFw);
		
		// recalc results
		_resFireworks++;
		_resFireworksAtATime = Math.max(_resFireworksAtATime, _fireworks.size());
	}
	
	/**
	 * TODO: describe behavior
	 * @return
	 */
	public Highscore.Entry getEntry() {
		// create highscore after the game ends 
		if(_state != State.GAMEOVER) return null;
		
		Highscore.Entry entry = new Highscore.Entry();
		entry.setName("Dummy");
		entry.setOverall(_resFireworks);
		entry.setAtonce(_resFireworksAtATime);
		entry.setFps(_resFireworksPerSecond);
		return entry;
	}
}
