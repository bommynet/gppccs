package de.pixlpommes.gppcc10;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * The player.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Player {

	private Texture _tex;
	
	private float _x, _y;
	
	public Player(float x, float y) {
		_x = x;
		_y = y;
		
		_tex = new Texture(Gdx.files.internal("player.png"));
	}
	
	public void draw(Batch batch) {
		batch.draw(_tex, _x, _y);
	}
}
