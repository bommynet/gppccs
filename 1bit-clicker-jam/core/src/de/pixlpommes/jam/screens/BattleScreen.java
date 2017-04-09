package de.pixlpommes.jam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import de.pixlpommes.jam.actions.ActionManager;
import de.pixlpommes.jam.arena.Arena;
import de.pixlpommes.jam.units.Player;
import de.pixlpommes.jam.units.Slime;
import de.pixlpommes.jam.units.base.Unit;

/**
 * The main game screen.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class BattleScreen implements Screen {

	/** area to place all units */
	private Arena _arena;
	
	/** create, execute, remove all unit actions */
	private ActionManager _actionManager;
	
	/** flags if fight is currently running (true) or paused/not started (false) */
	private boolean _isFightRunning;
	
	/**
	 * Create a battle screen.
	 */
	public BattleScreen() {
		// init
		_arena = new Arena();
		_actionManager = new ActionManager(_arena);
		_isFightRunning = false;
		
		// create basic units
		_arena.setPlayer(new Player()); // player
		_arena.setUnit(new Slime(), 2, 1); // party member
		_arena.setUnit(new Slime(), 3, 1); // enemy
	}
	
	/**
	 * Update logic.
	 * 
	 * @param delta
	 */
	public void update(float delta) {
		// check for inactive party members
		for(Unit party : _arena.getPartyMembers()) {
			if(party != null && !party.hasActiveAction()) {
				// get first enemy and attack it
				for(int id : Arena.IDS_ENEMY) {
					if(_arena.getUnit(id) != null) {
						// attack this unit
						int x = id % Arena.COLUMNS;
						int y = (int) Math.floor(id / Arena.COLUMNS);
						System.out.println(
								_actionManager.create(party, party.getAbility(0), x, y));
					}
				}
			}
		}
		
		// update action manager
		_actionManager.doTick(delta);
		
		
		// remove killed units
		for(int id=0; id<Arena.COLUMNS * Arena.ROWS; id++) {
			Unit unit = _arena.getUnit(id);
			
			if(unit != null && !unit.isAlive()) {
				// TODO: kill unit
				System.out.println("kill " + unit);
				_arena.setUnit(null, id);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		/// Gdx.app.log("BattleScreen", "render called");
		
		// update logic if fight is running
		if(_isFightRunning)
			this.update(delta);
		/// TODO remove!!!
		else {
			Gdx.app.log("BattleScreen", "start fight");
			_isFightRunning = true;
		}
		
		// draw screen always
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		Gdx.app.log("BattleScreen", "show called");
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		Gdx.app.log("BattleScreen", "resize called");
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
		Gdx.app.log("BattleScreen", "pause called");
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		Gdx.app.log("BattleScreen", "resume called");
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		Gdx.app.log("BattleScreen", "hide called");
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		Gdx.app.log("BattleScreen", "dispose called");
	}

}
