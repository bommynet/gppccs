package de.pixlpommes.jam.screens.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.pixlpommes.jam.units.base.Ability;

/**
 * A ability button for the player.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class PlayerButton {
	
	/** TODO: describe '_texInactive' */
	private Texture _texActive, _texInactive;
	
	/** TODO: describe '_y' */
	private int _x, _y;
	
	/** TODO: describe '_ability' */
	private Ability _ability;
	
	/** TODO: describe '_isActive' */
	private boolean _isActive;
	
	
	/**
	 * @param x
	 * @param y
	 */
	public PlayerButton(int x, int y) {
		_x = x;
		_y = y;
		
		_texActive = new Texture(Gdx.files.internal("button_inactive_0.png"));
		_texInactive = new Texture(Gdx.files.internal("button_inactive_0.png"));
		
		_ability = null;
		
		_isActive = false;
		
	}
	
	/**
	 * @param batch
	 */
	public void draw(Batch batch) {
		batch.begin();
		if(_isActive)
			batch.draw(_texActive, _x, _y);
		else
			batch.draw(_texInactive, _x, _y);
		batch.end();
	}
	
	/**
	 * @param activeTextureFile
	 * @param inactiveTextureFile
	 * @param ability
	 */
	public void set(String activeTextureFile, String inactiveTextureFile, Ability ability) {
		_texActive.dispose();
		_texInactive.dispose();
		
		_texActive = new Texture(Gdx.files.internal(activeTextureFile));
		_texInactive = new Texture(Gdx.files.internal(inactiveTextureFile));
		_ability = ability;
	}
	
	/**
	 * @return
	 */
	public Ability getAbility() {
		return _ability;
	}
	
	/**
	 * @return
	 */
	public boolean isActive() {
		return _isActive;
	}
	
	/**
	 * @param active
	 */
	public void setActive(boolean active) {
		_isActive = active;
	}
	
	/**
	 * @param active
	 */
	public void setActive() {
		setActive(true);
	}
}
