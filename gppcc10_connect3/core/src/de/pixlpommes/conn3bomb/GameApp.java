package de.pixlpommes.conn3bomb;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pixlpommes.conn3bomb.screens.LoadingScreen;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 */
public class GameApp extends Game {
	
	// GLOBALS
	public final static int WIDTH = 800;
	public final static int HEIGHT = 800;
	public final static int HALF_WIDTH = WIDTH / 2;
	public final static int HALF_HEIGHT = HEIGHT / 2;
	
	// LIBGDX
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
