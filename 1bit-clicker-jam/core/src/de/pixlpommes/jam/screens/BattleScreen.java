package de.pixlpommes.jam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pixlpommes.jam.actions.ActionManager;
import de.pixlpommes.jam.arena.Arena;
import de.pixlpommes.jam.screens.ui.ArenaUI;
import de.pixlpommes.jam.screens.ui.ProgressBar;
import de.pixlpommes.jam.screens.ui.UnitPanel;
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

	/** batch to render everything */
	private SpriteBatch _batch;
	
	/** standard 2D camera */
	private OrthographicCamera _cam;
	
	/** area to place all units */
	private Arena _arena;
	
	/** graphical representation of the arena */
	private ArenaUI _arenaUI;
	
	/** create, execute, remove all unit actions */
	private ActionManager _actionManager;
	
	/** flags if fight is currently running (true) or paused/not started (false) */
	private boolean _isFightRunning;
	
	
	/**
	 * Create a battle screen.
	 */
	public BattleScreen() {
		// setup graphics
		_cam = new OrthographicCamera();
		_batch = new SpriteBatch();
		
		// init
		_arena = new Arena();
		_arenaUI = new ArenaUI();
		_actionManager = new ActionManager(_arena);
		_isFightRunning = true; /// TODO start by user input!
		
		// create basic units
		_arena.setPlayer(new Player()); // player
		
		// party member
		Unit partyMember = new Slime();
		_arena.setUnit(partyMember, 2, 1); // party member
		_arenaUI.setObserver(partyMember, 2, 1);
		
		// enemy
		Unit enemy = new Slime();
		_arena.setUnit(enemy, 3, 1); // enemy
		_arenaUI.setObserver(enemy, 3, 1);
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
				unit.deleteObservers();
				_arena.setUnit(null, id);
				_arenaUI.removeUnit(id);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		/// Gdx.app.log("BattleScreen", "render called");
		
		// clear screen
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// update logic if fight is running
		if(_isFightRunning)
			this.update(delta);
		
		
		// draw screen always
		//_batch.begin();
		
		_arenaUI.draw(_batch);
		
		//_batch.end();
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
		
		// update camera
		_cam.viewportWidth = width;
		_cam.viewportHeight = height;
		_cam.update();
		
		// update renderer
		_batch.setProjectionMatrix(_cam.combined);
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
