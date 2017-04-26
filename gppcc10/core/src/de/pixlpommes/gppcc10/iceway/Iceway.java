package de.pixlpommes.gppcc10.iceway;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.pixlpommes.gppcc10.Gppcc10;
import de.pixlpommes.gppcc10.iceway.IcewayRow.Tile;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class Iceway {

	/** TODO: describe 'CONFIG_3' */
	public final static Tile[] CONFIG_3 = new Tile[] {
			Tile.NONE, Tile.NONE,
			Tile.NORMAL, Tile.NORMAL, Tile.NORMAL,
			Tile.NONE, Tile.NONE
	};

	/** TODO: describe 'CONFIG_313' */
	public final static Tile[] CONFIG_313 = new Tile[] {
			Tile.NORMAL, Tile.NORMAL, Tile.NORMAL,
			Tile.NONE,
			Tile.NORMAL, Tile.NORMAL, Tile.NORMAL
	};
	
	/** tiles count in width */
	public final static int COLS = 7;

	/** tiles count in height */
	public final static int ROWS = 15;

	/** tile size in pixel */
	public final static int TILESIZE = 66;

	/** texture including all tiles for ice way */
	public final static Texture TILESET = new Texture(Gdx.files.internal("iceway.png"));

	// general
	/** TODO: describe '_offsetX' */
	private int _offsetX, _offsetY;

	// iceway
	/** TODO: describe '_iceway' */
	private List<IcewayRow> _iceway;
	
	/** TODO: describe '_maxSpeed' */
	private float _speed, _maxSpeed;
	
	/** acceleration per second */
	private float _accSpeed;
	
	// items and more
	/** powerups and -downs */
	private Items _items;

	
	/**
	 * @param posX
	 * @param posY
	 */
	public Iceway(int posX, int posY) {
		// setup position
		_offsetX = posX;
		_offsetY = posY;
		
		// setup speed values
		_speed = 0;
		_maxSpeed = 170;
		_accSpeed = 75;

		// init iceway
		_iceway = new ArrayList<IcewayRow>();
		for (int i = 0; i < ROWS; i++) {
			_iceway.add(new IcewayRow(_offsetX, _offsetY + i * TILESIZE));
		}
		
		// setup item and object manager
		_items = new Items();
	}
	
	/**
	 * TODO: describe function
	 * @param delta
	 */
	public void update(float delta) {
		// speed up to max speed
		if(_speed < _maxSpeed)
			_speed += _accSpeed * delta;
		else if(_speed > _maxSpeed)
			_speed = _maxSpeed;
		
		// speed for all rows at current frame (negative for moving down)
		float deltaSpeed = _speed * delta * (-1);
		
		// update positions
		for(int i=_iceway.size()-1; i>=0; i--) {
			IcewayRow row = _iceway.get(i);
			row.update(deltaSpeed);
			
			// remove out-of-screen rows and add new ones
			if(row.getY() < -Gppcc10.HALF_HEIGHT - 2*TILESIZE) {
				// remove row
				_iceway.remove(row);
				
				// add row on top
				float topY = _iceway.get(_iceway.size()-1).getY() + TILESIZE;
				IcewayRow newRow = new IcewayRow(_offsetX, topY, CONFIG_3);
				_iceway.add(newRow);
				
				// TODO: select a random visible tile or something like config
				if(Math.random() < 0.2) {
					_items.add(_iceway.get(0).getX(3), topY, Item.Type.SNOWMAN);
				}
			}
		}
		
		_items.update(deltaSpeed);
	}
	
	/**
	 * TODO: describe function
	 * @param batch
	 */
	public void draw(Batch batch) {
		for(IcewayRow row : _iceway)
			row.draw(batch);
		
		_items.draw(batch);
	}
	
	/**
	 * @return current iceway speed
	 */
	public float getSpeed() {
		return _speed;
	}
	
	/**
	 * TODO: describe function
	 * @param indexColumn
	 * @return
	 */
	public float getX(int indexColumn) {
		return _iceway.get(0).getX(indexColumn);
	}
	
	/**
	 * TODO: describe function
	 * @param indexColumn
	 * @return
	 */
	public float getY(int indexRow) {
		return _iceway.get(indexRow).getY();
	}
	
	/**
	 * TODO: describe function
	 * @return
	 */
	public List<IcewayRow> getIceway() {
		return _iceway;
	}
	
	/**
	 * TODO: describe function
	 * @return
	 */
	public List<Item> getItems() {
		return _items.getList();
	}
}
