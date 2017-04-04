package de.pixlpommes.firework.stages;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.pixlpommes.firework.Highscore;
import de.pixlpommes.firework.Highscore.Entry;
import de.pixlpommes.firework.font.RetroFont;

/**
 * <p>Highscore screen.</p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public class Highscores extends Stage {

	/** TODO: the _highscore */
	private Highscore _highscore;
	
	/** TODO: the _highlightIndex */
	private int _highlightIndex;
	
	/** retro bitmap as a font */
	private RetroFont _rfont;

	/** TODO: the _countDown */
	private float _countDown;
	
	/**
	 * TODO
	 * @param hs
	 */
	public Highscores(Highscore hs) {
		this(hs, -1);
	}
	
	/**
	 * TODO
	 * @param hs
	 * @param highlightIndex
	 */
	public Highscores(Highscore hs, int highlightIndex) {
		// save references
		_highscore = hs;
		_highlightIndex = highlightIndex;
		
		// setup stage
		_rfont = new RetroFont();
		_countDown = 2f;
		
		// store highscore
		try {
			File file = new File("highscore.json");
			System.out.println(file.getAbsolutePath());
			FileOutputStream stream = new FileOutputStream(file, false);
			stream.write(_highscore.toJson().getBytes());
			stream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.scenes.scene2d.Stage#draw()
	 */
	@Override
	public void draw() {
		Batch batch = getBatch();
		batch.begin();
		
		int curY = Gdx.graphics.getHeight() - 100;
		
		_rfont.draw(batch,
				"HIGHSCORES",
				Gdx.graphics.getWidth()/2,
				curY,
				40, 40,
				true, false);
		
		curY -= 75;
		int diffY = 32;
		
		_rfont.draw(batch,
				"NO PLAYER      $   % ",
				Gdx.graphics.getWidth()/2,
				curY,
				30, 30,
				true, false);
		
		for(int i=0; i<_highscore.lenght(); i++) {
			curY -= diffY;
			_rfont.draw(batch,
					stringify(i, _highscore.getEntry(i)),
					Gdx.graphics.getWidth()/2,
					curY,
					30, 30,
					true, false);
		}
		
		batch.end();
		
		_countDown -= Gdx.graphics.getDeltaTime();
		if(_countDown < 0) _countDown = 0;
	}
	
	/**
	 * TODO: describe behavior
	 * @param index
	 * @param entry
	 * @return
	 */
	private String stringify(int index, Entry entry) {
		String res = "";
		
		// prepare index
		String sIndex = "" + (index + 1);
		while(sIndex.length() < 2) sIndex = "0" + sIndex;
		
		// prepare name (lengthen or cut)
		String sName = entry.getName();
		while(sName.length() < 10) sName = sName + ".";
		if(sName.length() > 10) sName = sName.substring(0, 9);
		
		// prepare overall
		String sAll = ""+entry.getOverall();
		while(sAll.length() < 3) sAll = "0" + sAll;
		
		// prepare at once
		String sOnce = ""+entry.getAtonce();
		while(sOnce.length() < 3) sOnce = "0" + sOnce;
		
		res += sIndex + " " + sName + " " + sAll + " " + sOnce;
		
		return res;
	}
	
	/**
	 * handle user input
	 * @return true if game ended
	 */
	public boolean pushedOneButton() {
		return _countDown == 0;
	}
}
