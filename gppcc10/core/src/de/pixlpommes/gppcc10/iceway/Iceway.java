package de.pixlpommes.gppcc10.iceway;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

import de.pixlpommes.gppcc10.Gppcc10;
import de.pixlpommes.gppcc10.Player;
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

	/** TODO: describe 'CONFIG_5' */
	public final static Tile[] CONFIG_5 = new Tile[] {
			Tile.NONE, Tile.NORMAL,
			Tile.NORMAL, Tile.NORMAL, Tile.NORMAL,
			Tile.NORMAL, Tile.NONE
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
	
	/** TODO: describe '_config' */
	private Tile[][] _config;
	
	/** TODO: describe '_configNextRow' */
	private int _configNextRow;
	
	/** TODO: describe '_levelCurrentPosition' */
	private int _levelLength, _levelCurrentPosition;

	/** the player */
	private Player _player;
	
	/** the bad, ice-melting blow-flyers */
	private BlowFlyer _flyer;

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
	
	/** progress bar */
	private ProgressBar _progress;

	
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
		
		// setup player
		_player = new Player();
		_player.setPosition(
				getX((int)(Iceway.COLS / 2)),
				getY(2));
		
		// setup blow-flyer
		_flyer = new BlowFlyer(posX, -Gppcc10.HEIGHT);
		
		// setup item and object manager
		_items = new Items();
		
		// setup progress bar
		_progress = new ProgressBar(
				_offsetX + (COLS+1)*TILESIZE,
				-Gppcc10.HALF_HEIGHT + TILESIZE);
				
		// load a level, TODO: select level before
		_config = getConfig("level/000.lvl");
		_configNextRow = 0;
		
		// setup maximum level length
		_levelLength = _config.length + ROWS - 2;
		_levelCurrentPosition = 0;
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
				Tile[] config = _config[_configNextRow];
				IcewayRow newRow = new IcewayRow(_offsetX, topY, config);
				_iceway.add(newRow);
				
				// select next config row
				_configNextRow++;
				if(_configNextRow >= _config.length)
					_configNextRow = _config.length-1;
				
				// increase level position
				_levelCurrentPosition++;
				if(_levelCurrentPosition >= _levelLength)
					_levelCurrentPosition = _levelLength;
				// TODO: remove sysout
				System.out.println("Level progress: " +
				(float)((float)_levelCurrentPosition / (float)_levelLength));
				
				
				// TODO: select a random visible tile or something like config
				if(Math.random() < 0.2) {
					_items.add(_iceway.get(0).getX(3), topY, Item.Type.SNOWMAN);
				}
			}
		}
		
		_player.update(delta);
		_flyer.update(delta, _speed);
		_items.update(deltaSpeed);
		_progress.update(delta);
		
		
		// do collisions
		// melt each row reached by the blow-flyers
		for(IcewayRow row : _iceway) {
			if(!row.isMolten() && row.getY() <= _flyer.getY()) {
				row.setMolten();
			}
		}
		
		// check collisions between items <-> player
		if(!_player.isSwitching()) {
			for(Item item : _items.getList()) {
				if(checkCollision(item.getBounds(), _player.getBounds())) {
					/// TODO: check for item type
					item.kill();
				}
			}
		}
	}
	
	/**
	 * TODO: describe function
	 * @param batch
	 */
	public void draw(Batch batch) {
		for(IcewayRow row : _iceway)
			row.draw(batch);
		
		_items.draw(batch);
		_progress.draw(batch);
		_flyer.draw(batch);
		_player.draw(batch);
	}
	
	/**
	 * Check for collisions between two rectangles.
	 * 
	 * @param rect1
	 * @param rect2
	 * @return
	 */
	public boolean checkCollision(Rectangle rect1, Rectangle rect2) {
		return rect1.overlaps(rect2);
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
	 * @param pixels
	 */
	public void movePlayerBy(float pixels) {
		float moveTo = _player.getX() + pixels;
		_player.switchPos(moveTo);
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
	
	/**
	 * TODO: describe function
	 * @param levelFile
	 * @return
	 */
	public Tile[][] getConfig(String levelFile) {
		// load file
		String raw = Gdx.files.internal(levelFile).readString("UTF-8");
		String[] rows = raw.split("\n");
		
		// create config array
		Tile[][] config = new Tile[rows.length][COLS];
		
		// transfer string to config array
		for(int row=0; row<rows.length; row++) {
			String s = rows[row].trim();
			
			for(int col=0; col<COLS; col++) {
				switch(s.charAt(col)) {
					case 'X':
						config[row][col] = Tile.NORMAL;
						break;
						
					default:
						config[row][col] = Tile.NONE;
				}
			}
		}
		
		return config;
	}
}
