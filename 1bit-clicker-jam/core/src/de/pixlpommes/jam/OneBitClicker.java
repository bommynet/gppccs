package de.pixlpommes.jam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import de.pixlpommes.jam.screens.BattleScreen;

/**
 * Create game.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 1.0
 */
public class OneBitClicker extends Game {
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create() {
		Gdx.app.log("Game", "create");
		setScreen(new BattleScreen());
	}
}
