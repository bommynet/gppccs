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
	
	
	/** TODO: describe 'PADDING' */
	private final static float PADDING = 20;
	
	
	/** TODO: describe '_yPlate' */
	private float _xPlate, _yPlate;
	
	/** TODO: describe '_score' */
	private TextureRegion[] _score;
	
	
	public IcewayUI() {
		_xPlate = Gppcc10.HALF_WIDHT - UI_PLATE.getWidth() - PADDING;
		_yPlate = Gppcc10.HALF_HEIGHT - UI_PLATE.getHeight() - PADDING;
		
		_score = new TextureRegion[5];
		for(int i=0; i<_score.length; i++) {
			_score[i] = ATLAS_NUMBERS_BIG.findRegion("numbers_big", 0);
		}
	}
	
	
	/**
	 * TODO: describe function
	 * @param batch
	 */
	public void draw(Batch batch) {
		// plate
		batch.draw(UI_PLATE, _xPlate, _yPlate);
		
		// points
		batch.draw(_score[0], _xPlate + 50, _yPlate + 76);
		batch.draw(_score[1], _xPlate + 81, _yPlate + 76);
		batch.draw(_score[2], _xPlate + 112, _yPlate + 76);
		batch.draw(_score[3], _xPlate + 143, _yPlate + 76);
		batch.draw(_score[4], _xPlate + 174, _yPlate + 76);
	}


	@Override
	public void update(Observable obs, Object arg) {
		if(!(obs instanceof Iceway)) return;
		
		Integer temp = (int)((Iceway)obs).getSpeed();
		String code = temp.toString();
		for(int i=0; i<code.length(); i++) {
			int charId = 1; //Integer.getInteger(""+code.charAt(i));
			_score[i] = ATLAS_NUMBERS_BIG.findRegion("numbers_big", charId);
		}
	}
}
