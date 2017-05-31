package de.pixlpommes.conn3bomb;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pixlpommes.conn3bomb.screens.LoadingScreen;

/**
 * @author Thomas Borck
 */
public class GameApp extends Game {
	
	/** one global camera */
	public OrthographicCamera camera;
	
	/** one global sprite batch */
	public SpriteBatch batch;
	
	/** one global manager for graphics and sounds */
	public AssetManager assets;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create() {
		// create instances
		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		assets = new AssetManager();
		
//		this.setScreen(new GameScreen(this));
		this.setScreen(new LoadingScreen(this));
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Game#dispose()
	 */
	public void dispose() {
		batch.dispose();
		assets.dispose();
		this.getScreen().dispose();
	}
}
