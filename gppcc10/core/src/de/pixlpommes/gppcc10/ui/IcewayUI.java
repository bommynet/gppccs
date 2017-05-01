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
	
	
	/** TODO: describe 'ATLAS_NUMBERS_BIG' */
	public final static TextureAtlas ATLAS_NUMBERS_BIG = new TextureAtlas(Gdx.files.internal("font/numbers_big.atlas"));

	/** TODO: describe 'ATLAS_SPEED' */
	public final static TextureAtlas ATLAS_SPEED = new TextureAtlas(Gdx.files.internal("graphics/plate_speed.atlas"));
	
	
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
	
	
	public IcewayUI() {
		_xPlate = Gppcc10.HALF_WIDHT - UI_PLATE.getWidth() - PADDING;
		_yPlate = Gppcc10.HALF_HEIGHT - UI_PLATE.getHeight() - PADDING;
		
		_regionScore = new TextureRegion[SCORE_DIGITS];
		for(int i=0; i<_regionScore.length; i++) {
			_regionScore[i] = ATLAS_NUMBERS_BIG.findRegion("numbers_big", 0);
		}
		
		_regionSpeed = ATLAS_SPEED.findRegion("plate_speed", 0);
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
