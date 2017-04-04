package de.pixlpommes.obfflf;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.pixlpommes.lib.Tuple;

/**
 * <p>Fighting screen.</p>
 * 
 * <p>Consists three main parts: i) the (input) lines, ii) the player(s) and
 * iii) the enemy.</p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public class Fight extends Stage {

	/** TODO: the _line */
	private LineManager _line;
	
	/** TODO: the _player */
	private Player[] _player;
	
	/** TODO: the _enemy */
	private Enemy _enemy;
	
	/**
	 * 
	 */
	public Fight() {
		// setup things
		_line = new LineManager();
		
		// create player(s)
		_player = new Player[2];
		_player[0] = new Player(false);
		_player[1] = new Player(true);
		
		// create enemy
		_enemy = new Enemy();
	}
	
	
	/**
	 * TODO:
	 */
	public void doActionPlayerOne() {
		// get result from line manager
		Tuple<Integer,Float> res = _line.hitButton();
		
		// if result is null, there's nothing to do
		if(res == null) {
			System.out.println("Action:player1:NULL");
			return;
		}
		
		// extract for better understanding
		int type = res.getA();
		float percent = res.getB();
		
		// TODO: do better and at other location in code
		int strength = 5;
		
		// calc weighted value
		int value = (int) Math.ceil(strength * percent);
		
		// do we do an attack or heal		
		if(type == 0) {
			_player[0].attack(_enemy, -value);
		} else {
			_player[0].heal(_player[0], value);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.scenes.scene2d.Stage#draw()
	 */
	@Override public void draw() {
		// ### update logic
		float deltaTime = Gdx.graphics.getDeltaTime();
		_line.update(deltaTime);
		_player[0].update(deltaTime);
		if(_player[1] != null) _player[1].update(deltaTime);
		_enemy.update(deltaTime);
		
		// ### draw view
		Batch batch = getBatch();
		
		// TODO: many things
		
		// draw player and enemy
		_player[0].draw(batch);
		if(_player[1] != null) _player[1].draw(batch);
		_enemy.draw(batch);
		
		// draw line manager
		_line.draw(batch);
		
		
		// ### process user input
		// TODO: 'one button' on SPACE, left mouse click and controller 'A'
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
			doActionPlayerOne();
	}
}
