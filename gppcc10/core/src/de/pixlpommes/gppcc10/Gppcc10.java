package de.pixlpommes.gppcc10;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 1.0
 */
public class Gppcc10 extends Game {
	
	/** game screen width */
	public final static int WIDTH = 960;
	
	/** game screen height */
	public final static int HEIGHT = 768;
	
	/** half game screen width */
	public final static int HALF_WIDHT = WIDTH/2;
	
	/** half game screen width */
	public final static int HALF_HEIGHT = HEIGHT/2;

	/** texture including all tiles for ice way */
	public final static Texture TILESET = new Texture(
			Gdx.files.internal("iceway.png"));
	
	/** background texture: clouds */
	public final static Texture TEXTURE_CLOUDS = new Texture(
			Gdx.files.internal("clouds.png"));
	
	/** background texture: woods */
	public final static Texture FILE_IMAGE_WOODS = new Texture(
			Gdx.files.internal("woods.png"));
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create () {
		setScreen(new IcewayScreen());
	}
}
