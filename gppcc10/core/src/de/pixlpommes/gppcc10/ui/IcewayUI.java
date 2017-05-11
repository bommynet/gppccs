package de.pixlpommes.gppcc10.ui;

import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.pixlpommes.gppcc10.Gppcc10;
import de.pixlpommes.gppcc10.iceway.Iceway;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.1
 */
public class IcewayUI implements Observer {

	/** TODO: describe 'UI_PLATE' */
	public final static Texture UI_PLATE = new Texture(Gdx.files.internal("graphics/plate.png"));
	
	/** TODO: describe 'UI_OVERLAY' */
	public final static Texture UI_OVERLAY = new Texture(Gdx.files.internal("graphics/overlay.png"));
	
	
	/** TODO: describe 'ATLAS_NUMBERS_BIG' */
	public final static TextureAtlas ATLAS_NUMBERS_BIG = new TextureAtlas(Gdx.files.internal("font/numbers_big.atlas"));
	
	/** TODO: describe 'ATLAS_CHARS_SMALL' */
	public final static TextureAtlas ATLAS_CHARS_SMALL = new TextureAtlas(Gdx.files.internal("font/chars_small.atlas"));

	/** TODO: describe 'ATLAS_SPEED' */
	public final static TextureAtlas ATLAS_SPEED = new TextureAtlas(Gdx.files.internal("graphics/plate_speed.atlas"));

	/** TODO: describe 'ATLAS_SPEED' */
	public final static TextureAtlas ATLAS_MARKER = new TextureAtlas(Gdx.files.internal("graphics/marker.atlas"));
	
	
	/** TODO: describe 'PADDING' */
	public final static float PADDING = 20;
	
	
	/** TODO: describe 'SCORE_DIGITS' */
	private final static int SCORE_DIGITS = 5;
	
	
	/** TODO: describe '_yPlate' */
	private float _xPlate, _yPlate;
	
	/** TODO: describe '_score' */
	private TextureRegion[] _regionScore;
	
	/** TODO: describe '_regionSpeed' */
	private TextureRegion _regionSpeed;
	
	
	/** TODO: describe '_regionMarkerPlayer' */
	private TextureRegion _regionMarkerPlayer, _regionMarkerFly;
	
	/** time elapsed 'til the next animation frame */
	private float _markerTimer, _markerDelay;
	
	/** current animation frame */
	private int _markerId, _markerIds;
	
	/** position range for x value */
	private float _markerMinX, _markerMaxX;
	
	/** current position of the marker */
	private float _markerPlayerPosX, _markerFlyPosX;
	
	
	/**
	 * 
	 */
	public IcewayUI() {
		// setup panel position
		_xPlate = Gppcc10.HALF_WIDHT - UI_PLATE.getWidth() - PADDING;
		_yPlate = Gppcc10.HALF_HEIGHT - UI_PLATE.getHeight() - PADDING;
		
		// setup score
		_regionScore = new TextureRegion[SCORE_DIGITS];
		for(int i=0; i<_regionScore.length; i++) {
			_regionScore[i] = ATLAS_NUMBERS_BIG.findRegion("numbers_big", 0);
		}
		
		// setup speed
		_regionSpeed = ATLAS_SPEED.findRegion("plate_speed", 0);
		
		// setup marker
		_markerMinX = 3;
		_markerMaxX = 103;
		_markerPlayerPosX = _markerFlyPosX = _markerMinX;
		_markerTimer = _markerDelay = 0.2f;
		_markerIds = 3;
		_markerId = 0;
		_regionMarkerPlayer = ATLAS_MARKER.findRegion("marker_banana", 0);
		_regionMarkerFly = ATLAS_MARKER.findRegion("marker_fly", 0);
	}
	
	
	/**
	 * TODO: describe function
	 * @param delta
	 */
	public void update(float delta) {
		if(_markerTimer > 0)
			_markerTimer -= delta;
		else {
			_markerTimer = _markerDelay;
			
			_markerId++;
			if(_markerId >= _markerIds)
				_markerId = 0;
			
			_regionMarkerPlayer = ATLAS_MARKER.findRegion("marker_banana", _markerId);
			_regionMarkerFly = ATLAS_MARKER.findRegion("marker_fly", _markerId);
		}
	}
	
	/**
	 * TODO: describe function
	 * @param batch
	 */
	public void draw(Batch batch) {
		// plate
		batch.draw(UI_PLATE, _xPlate, _yPlate);
		
		// points - TODO: create dynamically
		batch.draw(_regionScore[0], _xPlate + 50, _yPlate + 76);
		batch.draw(_regionScore[1], _xPlate + 81, _yPlate + 76);
		batch.draw(_regionScore[2], _xPlate + 112, _yPlate + 76);
		batch.draw(_regionScore[3], _xPlate + 143, _yPlate + 76);
		batch.draw(_regionScore[4], _xPlate + 174, _yPlate + 76);
		
		// speed
		batch.draw(_regionSpeed, _xPlate + 136, _yPlate + 14);
		
		// marker
		// TODO dynamically positioning
		batch.draw(_regionMarkerPlayer, _xPlate + _markerPlayerPosX, _yPlate + 8);
		// TODO batch.draw(_regionMarkerFly, _xPlate + 103, _yPlate + 8);
	}


	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable obs, Object arg) {
		if(!(obs instanceof Iceway)) return;
		
		Iceway iceway = (Iceway)obs;
		
		// update score value
		int number = (int)iceway.getScore();
		for(int i=SCORE_DIGITS-1; i>=0; i--) {
			// use last digit for region id
			int charId = number % 10;
			_regionScore[i] = ATLAS_NUMBERS_BIG.findRegion("numbers_big", charId);
			
			// move to next digit
			number = number / 10;
		}
		
		// TODO update progress
		float progress = iceway.getPositionRelative();
		if(progress <= 0) progress = 0.0001f;
		float playerX = (_markerMaxX - _markerMinX) * progress + _markerMinX;
		_markerPlayerPosX = playerX;
		
		// update speed
		float speed = iceway.getSpeedRelative();
		if(speed > 1.1f)
			_regionSpeed = ATLAS_SPEED.findRegion("plate_speed", 4);
		else if(speed > 0.67f)
			_regionSpeed = ATLAS_SPEED.findRegion("plate_speed", 3);
		else if(speed > 0.34f)
			_regionSpeed = ATLAS_SPEED.findRegion("plate_speed", 2);
		else if(speed > 0.01f)
			_regionSpeed = ATLAS_SPEED.findRegion("plate_speed", 1);
		else
			_regionSpeed = ATLAS_SPEED.findRegion("plate_speed", 0);
	}
}
