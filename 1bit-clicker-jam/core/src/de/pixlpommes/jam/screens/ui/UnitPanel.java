package de.pixlpommes.jam.screens.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import de.pixlpommes.jam.units.base.Unit;

/**
 * Draw a unit and all it's information.<br/>
 * This component shows the unit sprite, it's current/max health
 * and can be clicked.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class UnitPanel {

	private int _offsetX, _offsetY;
	
	private int _width, _height;
	
	/** TODO: describe '_health' */
	private ProgressBar _health;
	
	/** TODO: describe '_timer' */
	private ProgressBar _timer;
	
	/** unit to show up */
	private Unit _unit;
	
	/** TODO: describe '_textures' */
	private Texture[] _textures;
	
	/**
	 * 
	 */
	public UnitPanel(float offsetX, float offsetY) {
		_offsetX = (int)offsetX;
		_offsetY = (int)offsetY;
		_width   = 80;
		_height  = 80;
		
		_health = new ProgressBar(
				ProgressBar.FILE_HEALTHBAR,
				0f, 0f,
				_offsetX, _offsetY - 9);
		
		_timer = new ProgressBar(
				ProgressBar.FILE_TIMERBAR,
				0f, 0f,
				_offsetX, _offsetY + _height);
		
		_unit = null;
		_textures = null;
	}
	
	/**
	 * Reset data to 0.
	 */
	public void reset() {
		_health.setValues(0f, 0f, 0f);
		_timer.setValues(0f, 0f, 0f);
		_unit = null;
	}
	
	public void set(Unit unit) {
		_unit = unit;
		_health.setValues(0f, _unit.getHpMax(), _unit.getHpCurrent());
		
		// check if there are texture files
		String[] tex = _unit.getTextureFiles();
		if(tex == null) return;
		
		// delete existing textures
		if(_textures != null) {
			for(Texture t : _textures)
				t.dispose();
			_textures = null;
		}
		
		// load new textures
		_textures = new Texture[tex.length];
		for(int i=0; i<tex.length; i++) {
			_textures[i] = new Texture(Gdx.files.internal(tex[i]));
		}
	}
	
	/**
	 * @param batch
	 */
	public void draw(Batch batch) {
		
		/// TODO remove border
		ShapeRenderer sr = new ShapeRenderer();
		sr.setProjectionMatrix(batch.getProjectionMatrix());
		sr.begin(ShapeType.Line);
		sr.setColor(1f, 0f, 0f, 1f);
		sr.rect(_offsetX, _offsetY, _width, _height);
		sr.end();
		
		// don't show panel if no unit is referenced
		if(_unit == null) return;
		
		// show health bar if unit has health only
		_health.setValue(_unit.getHpCurrent());
		_health.draw(batch);
		
		// draw 'idle' sprite
		/// TODO draw 'attack' sprite
		if(_textures != null) {
			batch.begin();
			batch.draw(_textures[0], _offsetX, _offsetY);
			batch.end();
		}
		
		// show timer bar if unit 'casts' something
		if(_unit.getActiveAction() != null) {
			_timer.setValues(0,
					_unit.getActiveAction().getAbility().getUseTime(),
					_unit.getActiveAction().getTimerInvers());
			_timer.draw(batch);
		}
	}
}
