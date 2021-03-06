package de.pixlpommes.gppcc10.iceway;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import de.pixlpommes.gppcc10.Gppcc10;
import de.pixlpommes.gppcc10.iceway.IcewayRow.Tile;
import de.pixlpommes.gppcc10.iceway.Player.State;

/**
 * The iceway and all it's components.
 * 
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 1.0
 */
public class Iceway extends Observable {
	
	/** tiles count in width */
	public final static int COLS = 7;

	/** tiles count in height */
	public final static int ROWS = 15;

	/** tile size in pixel */
	public final static int TILESIZE = 66;
	
	
	/** TODO: describe 'SCORE_LEVEL_PROGRESS' */
	public final static int SCORE_LEVEL_PROGRESS = 10;
	

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
	/** TODO: describe '_winLostHandler' */
	private WinLost _winLostHandler;
	
	/** iceway's bottom left corner on screen */
	private int _offsetX, _offsetY;
	
	/** current iceway configuration */
	private Tile[][] _config;
	
	/** next added tile row will get the configuration at this index */
	private int _configNextRow;
	
	/** the player's position in the level */
	private int _levelLength, _levelCurrentPosition;
	
	/** player's score */
	private int _score;

	
	// player
	/** the player */
	private Player _player;
	
	/** TODO: describe '_playerMoveLeft' */
	private boolean _playerMoveLeft = false;
	
	/** TODO: describe '_playerMoveRight' */
	private boolean _playerMoveRight = false;
	
	/** player's acceleration in pixel per second */
	private float _playerAcc;
	
	/** the player moves up out of the screen */
	private boolean _playerMovesUp;
	
	/** TODO: describe '_playerVerticalSpeed' */
	private float _playerVerticalSpeed, _playerVerticalMaxSpeed;
	
	
	// enemies
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
	
	/** TODO: describe '_gameStarted' */
	private boolean _gameStarted;
	
	
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
		_playerAcc = 4f;
		_playerVerticalSpeed = 0f;
		_playerVerticalMaxSpeed = 200f;
		
		// setup blow-flyer
		_flyer = new BlowFlyer(posX, -Gppcc10.HEIGHT);
		
		// setup item and object manager
		_items = new Items();
				
		// load a level, TODO: select level before
		_config = getConfig("level/000a.lvl");
		_configNextRow = 0;
		
		// setup maximum level length
		_levelLength = _config.length;
		_levelCurrentPosition = -ROWS+2;
		
		_score = 0;
		
		// start game by user input
		_icewayIsMoving = true;
		_gameStarted = false;
	}
	
	/**
	 * Update iceway's logic.
	 * 
	 * @param delta
	 */
	public void update(float delta) {
		// speed up to max speed
		_speed += _accSpeed * delta;
		
		if(_speed > _maxSpeed)
			_speed = _maxSpeed;
		else if(_speed < 0 && _accSpeed < 0) {
			_speed = 0;
			_icewayIsMoving = false;
		}
		
		// speed for all rows at current frame (negative for moving down)
		float deltaSpeed = _speed * delta * (-1);
		
		if(_icewayIsMoving) {
			// update positions
			for(int i=_iceway.size()-1; i>=0; i--) {
				IcewayRow row = _iceway.get(i);
				row.update(delta, deltaSpeed);
				
				// remove out-of-screen rows and add new ones
				if(row.getY() < -Gppcc10.HALF_HEIGHT - 2*TILESIZE) {
					// remove row
					_iceway.remove(row);
					
					// add row on top
					float topY = _iceway.get(_iceway.size()-1).getY() + TILESIZE;
					
					// standard row if game isn't started
					if(!_gameStarted) {
						IcewayRow basicRow = new IcewayRow(_offsetX, topY);
						_iceway.add(basicRow);
					}
					// get row from config and calculate level progress
					else {
						int configId = _configNextRow >= _config.length ? _config.length-1 : _configNextRow;
						Tile[] config = _config[configId];
						IcewayRow newRow;
						
						if(_configNextRow == _config.length - 1) 
							newRow = new IcewayRow(_offsetX, topY, config, IcewayRow.Type.GOAL);
						else if(_configNextRow == 0)
							newRow = new IcewayRow(_offsetX, topY, config, IcewayRow.Type.FIRST);
						else
							newRow = new IcewayRow(_offsetX, topY, config);
						_iceway.add(newRow);
						
						// select next config row
						_configNextRow++;
						
						// increase level position
						_levelCurrentPosition++;
						if(_levelCurrentPosition >= _levelLength) {
							_levelCurrentPosition = _levelLength;
							
							// set player has won!
							playerWins();
						} else {
							if(_levelCurrentPosition >= 0)
								_score += SCORE_LEVEL_PROGRESS;
						}
					}
				}
			}
			
			_flyer.update(delta, _speed);
			_items.update(deltaSpeed);
		} else if(!_icewayIsMoving && _playerMovesUp) {
			// move player out of the screen
			if(_player.getY() <= Gppcc10.HALF_HEIGHT)
				_player.setPosition(
						_player.getX(),
						_player.getY() - deltaSpeed * 2.0f);
		}
		
		// process user input
		if(_gameStarted) {
			if(_player.getState() == Player.State.RUN) {
				if(_playerMoveLeft) {
					_playerVerticalSpeed -= _playerAcc;
					if(_playerVerticalSpeed < -_playerVerticalMaxSpeed)
						_playerVerticalSpeed = -_playerVerticalMaxSpeed;
				} else if(_playerMoveRight) {
					_playerVerticalSpeed += _playerAcc;
					if(_playerVerticalSpeed > _playerVerticalMaxSpeed)
						_playerVerticalSpeed = _playerVerticalMaxSpeed;
				} else if(!(_playerMoveLeft || _playerMoveRight)) {
					// slide if player wants to stop
					if(_playerVerticalSpeed < 0) {
						_playerVerticalSpeed += _playerAcc;
						if(_playerVerticalSpeed > 0)
							_playerVerticalSpeed = 0;
					} else if(_playerVerticalSpeed > 0) {
						_playerVerticalSpeed -= _playerAcc;
						if(_playerVerticalSpeed < 0)
							_playerVerticalSpeed = 0;
					}
				}
			} else {
				// slide if player falls off
				if(_playerVerticalSpeed < 0) {
					_playerVerticalSpeed += _playerAcc;
					if(_playerVerticalSpeed > 0)
						_playerVerticalSpeed = 0;
				} else if(_playerVerticalSpeed > 0) {
					_playerVerticalSpeed -= _playerAcc;
					if(_playerVerticalSpeed < 0)
						_playerVerticalSpeed = 0;
				}
			}
			
			// update player speed and position
			_player.setPosition(
					_player.getX() + _playerVerticalSpeed*delta,
					_player.getY());
			_player.update(delta);
		} else {
			// let's start
			if(Gdx.input.isKeyPressed(Keys.ANY_KEY))
				_gameStarted = true;
		}
		

		/// TODO check for points and not for position
		setChanged();
		notifyObservers();
		
		
		// do collisions
		// melt each row reached by the blow-flyers
		for(IcewayRow row : _iceway) {
			if(row.getY() <= _flyer.getY() && !row.isMolten()) {
				row.setMolten();
			}
		}
		
		// check if player walks on hole
		/* Idea behind collision check:
		 * 	1. the player can fall down, if he is not jumping currently (!isSwitching)
		 * 	2. each tile has a position and a size (x,y,w,h)
		 * 	3. the player falls, if he overlaps more than 50% of it's size with a hole
		 *     3.1 this can be reached by checking against the player sprite's center point
		 *  4. left/right from the iceway everything counts as hole
		 */
		// check 1. - player's not switching
		if(!_player.isSwitching()) {
			
			// check 4. - fall off iceway on left/right side
			if(_player.getX() < _offsetX
				|| _player.getX() >= _offsetX + COLS*TILESIZE) {
				playerFallsOff();
			} 
			// check 3. against 2. - player's center point is over a hole
			else {
				Vector2 center = _player.getCenterPoint();
				
				for(IcewayRow row : _iceway) {
					// check that row, the player currently walks on, only
					if(row.getY() < center.y && row.getY()+TILESIZE > center.y) {
						
						// check that column, the player currently walks on, only 
						for(int i=0; i<COLS; i++) {
							if(row.getX(i) < center.x && row.getX(i)+TILESIZE > center.x) {
								
								// check if the tile represents a hole
								if(row.getTile(i) == Tile.NONE) {
										if(row.getTile(i) == Tile.MOLTEN) {
											System.out.println("molten");
										}
									
									// all checks are true -> the player is over a hole
									playerFallsOff();
								}
							}
						}
					}
				}
			}
		}
		
		// check collisions between items <-> player
		/* collide if:
		 *  1. player isn't switching between two columns currently
		 *  2. player's center is in between item bounds
		 */
		// check 1. - player not switching between columns
		if(!_player.isSwitching()) {
			for(Item item : _items.getList()) {
				
				// check 2. - center is in between item
				Rectangle bounds = item.getBounds();
				Vector2 center = _player.getCenterPoint();
				if(bounds.x < center.x && bounds.x+bounds.width > center.x
						&& bounds.y < center.y && bounds.y+bounds.height > center.y) {
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
	 * Let the player fall of the iceway.
	 */
	public void playerFallsOff() {
		// don't kill killed players...
		if(_player.getState() != Player.State.RUN) return;
		
		// TODO: slow down iceway (not simply stop)
		//_icewayIsMoving = false;
		_accSpeed = -150.0f;
		// TODO: animate fall down
		_player.changeState(State.FALL);
		// TODO: special effect for 'death'
		
		// notify handler
		if(_winLostHandler != null)
			_winLostHandler.gameLost();
	}
	
	/**
	 * Let the player win.
	 */
	public void playerWins() {
		// 1. stop the iceway
		_icewayIsMoving = false;
		// 2. let the player move out of the screen
		_playerMovesUp = true;
		// 3. send event "player win"
		if(_winLostHandler != null)
			_winLostHandler.gameWon();
	}
	
	/**
	 * TODO: describe function
	 * @param winLost
	 */
	public void setWinLostHandler(WinLost winLost) {
		_winLostHandler = winLost;
	}
	
	
	/**
	 * @return current iceway speed
	 */
	public float getSpeed() {
		return _icewayIsMoving ? _speed : 0;
	}
	
	/**
	 * @return current player speed in %
	 */
	public float getSpeedRelative() {
		return _speed / _maxSpeed;
	}
	
	/**
	 * @return current player position, relative to level length
	 */
	public float getPositionRelative() {
		return (float)_levelCurrentPosition / (float)_levelLength;
	}
	
	/**
	 * @return current player score
	 */
	public int getScore() {
		return _score;
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
	 * @return true if game is started
	 */
	public boolean isGameStarted() {
		return _gameStarted;
	}

	/**
	 * @return true if animation for player dead is finished
	 */
	public boolean isPlayerDeadAnimationFinished() {
		return _player.getState() == Player.State.DEATH;
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
	 * TODO: describe function
	 */
	public void movePlayerLeft() {
		_playerMoveLeft = true;
		_playerMoveRight = false;
	}
	
	/**
	 * TODO: describe function
	 */
	public void movePlayerRight() {
		_playerMoveLeft = false;
		_playerMoveRight = true;
	}
	
	/**
	 * TODO: describe function
	 */
	public void movePlayerNot() {
		_playerMoveLeft = false;
		_playerMoveRight = false;
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
