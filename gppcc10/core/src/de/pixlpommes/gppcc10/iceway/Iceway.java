package de.pixlpommes.gppcc10.iceway;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

import de.pixlpommes.gppcc10.Gppcc10;
import de.pixlpommes.gppcc10.iceway.IcewayRow.Tile;
import de.pixlpommes.gppcc10.iceway.Player.State;

/**
 * The iceway and all it's components.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 1.0
 */
public class Iceway {
	
	/** tiles count in width */
	public final static int COLS = 7;

	/** tiles count in height */
	public final static int ROWS = 15;

	/** tile size in pixel */
	public final static int TILESIZE = 66;

	/** texture including all tiles for ice way */
	public final static Texture TILESET = new Texture(
			Gdx.files.internal("graphics/iceway.png"));
	
	/** background texture: clouds */
	public final static Texture TEXTURE_CLOUDS = new Texture(
			Gdx.files.internal("graphics/clouds.png"));
	
	/** background texture: woods */
	public final static Texture TEXTURE_WOODS = new Texture(
			Gdx.files.internal("graphics/woods.png"));

	// general
	/** iceway's bottom left corner on screen */
	private int _offsetX, _offsetY;
	
	/** current iceway configuration */
	private Tile[][] _config;
	
	/** next added tile row will get the configuration at this index */
	private int _configNextRow;
	
	/** the player's position in the level */
	private int _levelLength, _levelCurrentPosition;

	/** the player */
	private Player _player;
	
	/** the bad, ice-melting blow-flyers */
	private BlowFlyer _flyer;

	// iceway
	/** the iceway */
	private List<IcewayRow> _iceway;

	/** moves or stops iceway */
	private boolean _icewayIsMoving;
	
	/** moving speed of the player on the iceway */
	private float _speed, _maxSpeed;
	
	/** acceleration per second */
	private float _accSpeed;
	
	// items and more
	/** powerups and -downs */
	private Items _items;

	
	/**
	 * Create new iceway.
	 * 
	 * @param posX position x for the iceway
	 * @param posY position y for the iceway
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
				
		// load a level, TODO: select level before
		_config = getConfig("level/000.lvl");
		_configNextRow = 0;
		
		// setup maximum level length
		_levelLength = _config.length + ROWS - 2;
		_levelCurrentPosition = 0;
		
		/// TODO: start game by user input
		_icewayIsMoving = true;
	}
	
	/**
	 * Update iceway's logic.
	 * 
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
				
				
				// TODO: select a random visible tile or something like config
				if(Math.random() < 0.2) {
					_items.add(_iceway.get(0).getX(3), topY, Item.Type.SNOWMAN);
				}
			}
		}
		
		_player.update(delta);
		_flyer.update(delta, _speed);
		_items.update(deltaSpeed);
		
		
		// do collisions
		if(_player.getX() < _offsetX
				|| _player.getX() > _offsetX + COLS*TILESIZE) {
			playerFallsOff();
		}
		for(IcewayRow row : _iceway) {
			// melt each row reached by the blow-flyers
			if(!row.isMolten() && row.getY() <= _flyer.getY()) {
				row.setMolten();
			}
			
			// check if player walks on hole
			if(row.getY() < _player.getY() + TILESIZE) {
				// TODO: check step on hole, not overlapping with hole
				for(int i=0; i<COLS; i++) {
					Rectangle tile = new Rectangle(row.getX(i), row.getY(), 64, 64);
					if(!_player.isSwitching()
							&& checkCollision(tile, _player.getBounds())
							&& row.getTile(i) == Tile.NONE) {
						playerFallsOff();
					}
				}
			}
		}
		
		// check collisions between items <-> player
		if(!_player.isSwitching()) {
			for(Item item : _items.getList()) {
				if(checkCollision(item.getBounds(), _player.getBounds())) {
					/// TODO: check for item type
					_speed -= 100; // TODO: calculate speed reduce
					item.kill();
				}
			}
		}
	}
	
	/**
	 * Draw the iceway.
	 * 
	 * @param batch
	 */
	public void draw(Batch batch) {
		for(IcewayRow row : _iceway)
			row.draw(batch);
		
		_items.draw(batch);
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
	 * Let the player fall of the iceway.
	 */
	public void playerFallsOff() {
		// TODO: slow down iceway (not simply stop)
		_icewayIsMoving = false;
		// TODO: animate fall down
		_player.changeState(State.FALL);
		// TODO: special effect for 'death'
	}
	
	
	/**
	 * @return current iceway speed
	 */
	public float getSpeed() {
		return _speed;
	}
	
	/**
	 * Get screen x position of specified column.
	 * 
	 * @param indexColumn
	 * @return
	 */
	public float getX(int indexColumn) {
		return _iceway.get(0).getX(indexColumn);
	}
	
	/**
	 * Get screen y position of specified row.
	 * 
	 * @param indexColumn
	 * @return
	 */
	public float getY(int indexRow) {
		return _iceway.get(indexRow).getY();
	}
	
	
	/**
	 * @return true if player currently moving
	 */
	public boolean isMoving() {
		return _icewayIsMoving;
	}
	
	
	/**
	 * Let the player switch/jump to a new position.
	 * 
	 * @param pixels
	 */
	public void movePlayerBy(float pixels) {
		float moveTo = _player.getX() + pixels;
		_player.switchPos(moveTo);
	}
	
	/**
	 * Loads a configuration file.
	 * 
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
