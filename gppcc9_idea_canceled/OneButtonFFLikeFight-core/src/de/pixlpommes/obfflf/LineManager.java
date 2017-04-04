package de.pixlpommes.obfflf;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import de.pixlpommes.lib.Tuple;

/**
 * <p>A game line manager.</p>
 * 
 * <p>This class shows four lines on game screen with a marker at the
 * left side. Each line can spawn 'blocks' which represents (red) attack,
 * (blue) defense, (green) heal or (purple) support. They can be activated
 * by pressing a button when a block is below the marker.</p>
 * 
 * <p>Some of the blocks will be activated by just pressing the button shortly
 * and others will need to hold the button pressed. Although it is possible
 * that the input button will be blocked by some blocks.</p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public class LineManager {

	/** amount of lines */
	private final int LINE_COUNT = 4;
	
	/** amount of lines each player has */
	private final int LINES_PER_PLAYER = 2;
	
	/** TODO: the LINE_HEIGHT */
	private final int LINE_HEIGHT = 20;
	
	/** ids for each line */
	public final int RED = 0, BLUE = 2, GREEN = 1, PURPLE = 3;
	
	/** each line */
	private List<List<Block>> _line;
	
	/** position and with of marker area */
	private int _markerPos, _markerWidth;
	
	/** TODO: the _spawnTimer */
	private float _spawnTimer, _spawnDelay=1.0f;

	/** is this round a two player game */
	private boolean _isTwoPlayer = false;
	
	
	/** TODO: the _lineMiddle */
	private Texture _lineEnd, _lineMiddle;
	
	
	/**
	 * Creates a new line manager.
	 */
	public LineManager() {
		
		// load textures
		_lineEnd = new Texture(Gdx.files.internal("lines_border_0.png"));
		_lineMiddle = new Texture(Gdx.files.internal("lines_border_1.png"));
		
		// create the four input lines
		_line = new ArrayList<List<Block>>(LINE_COUNT);
		for(int i = 0; i < LINE_COUNT; i++)
			_line.add(new ArrayList<Block>());
		
		// setup marker
		_markerPos = LINE_HEIGHT;
		_markerWidth = LINE_HEIGHT;
		
		// setup spawn timer
		_spawnTimer = (float) (_spawnDelay * Math.random() + 0.5f);
	}
	
	/**
	 * <p>Add a new block to manager.</p>
	 * 
	 * <p><i>PRECOND: type element of [0, LINE_COUNT]</i></p>
	 * @param type
	 */
	public void spawnBlock(int type) {
		// select color by type
		Color c;
		switch(type) {
			case RED:   c = Color.RED; break;
			case BLUE:  c = Color.BLUE; break;
			case GREEN: c = Color.GREEN; break;
			default:    c = Color.PURPLE; break;
		}
		
		// create new block
		Block b = new Block(
				Config.WIDTH,
				(LINE_COUNT - type - 1) * LINE_HEIGHT + 2,
				LINE_HEIGHT - 4,
				LINE_HEIGHT - 4,
				c);
		
		// add new block to the manager
		_line.get(type).add(b);
	}
	
	/**
	 * Hit the button and get hit item.
	 * @return null or tuple of type and % of precision
	 */
	public Tuple<Integer,Float> hitButton() {
		Block block = null;
		int type = -1;
		
		// get all blocks currently under the marker
		// TODO: can be two blocks for two players!
		for(List<Block> l : _line) {
			for(Block b : l) {
				// TODO: do better
				// check if block and marker overlapping
				if(b.getX() >= _markerPos && 
				   b.getX() <= _markerPos+_markerWidth) {
					block = b;
					type = _line.indexOf(l);
					break;
				}
			}
		}
		
		// check hit blocks
		if(block != null) {
			// hits block with precision in %
			float percent = (float)(block.getX()-_markerPos) / (float)(_markerWidth);
			percent = 1.0f - percent;
			
			// TODO: do some fancy things on hit block
			
			// return type and percentage
			return new Tuple<Integer,Float>(type, percent);
		}
		
		// nothing to return
		return null;
	}
	
	/**
	 * @param dt
	 */
	public void update(float dt) {
		
		// update block position
		float diffX = dt * -100;
		for(List<Block> l : _line) {
			for(Block b : l) {
				b.update(diffX);
			}
		}
		
		// update spawn timer
		if(_spawnTimer < 0) {
			// spawn random blocks for player 1 & 2
			int type1 = (int) (Math.random() * (LINE_COUNT-LINES_PER_PLAYER));
			spawnBlock(type1);

			if(_isTwoPlayer) {
				int type2 = (int) (Math.random() * (LINE_COUNT-LINES_PER_PLAYER)) +
						LINES_PER_PLAYER;
				spawnBlock(type2);
			}
			
			// setup spawn timer
			_spawnTimer = (float) (_spawnDelay * Math.random() + 0.5f);
		} else {
			_spawnTimer -= dt;
		}
		
		// delete blocks left from screen
		for(List<Block> l : _line) {
			for(int i=0; i<l.size(); i++) {
				if(l.get(i).getX() < -LINE_HEIGHT) {
					l.remove(i);
				}
			}
		}
	}
	
	/**
	 * @param batch
	 */
	public void draw(Batch batch) {
		// draw background
		// TODO: draw 'real' background
		ShapeRenderer sr = new ShapeRenderer();
			// solid background
			sr.begin(ShapeType.Filled);
				sr.setColor(Color.DARK_GRAY);
				sr.rect(0, 0, Config.WIDTH, LINE_COUNT*LINE_HEIGHT);
			sr.end();
			// marker position
			sr.begin(ShapeType.Filled);
				sr.setColor(Color.GRAY);
				sr.rect(_markerPos, 0, _markerWidth, LINE_COUNT*LINE_HEIGHT);
			sr.end();
			// lines
			batch.begin();
			for(int y = 0; y < LINE_COUNT; y++) {
				batch.draw(_lineEnd, 0, y * LINE_HEIGHT, LINE_HEIGHT, LINE_HEIGHT);
				for(int x = 1; x < Config.WIDTH/LINE_HEIGHT; x++) {
					batch.draw(_lineMiddle, x * LINE_HEIGHT, y * LINE_HEIGHT, LINE_HEIGHT, LINE_HEIGHT);
				}
			}
			batch.end();
//			sr.begin(ShapeType.Line);
//				sr.setColor(Color.BLUE);
//				for(int i=0; i<=LINE_COUNT; i++)
//					sr.line(0, i*LINE_HEIGHT, Config.WIDTH, i*LINE_HEIGHT);
//			sr.end();
			
		
		// draw blocks
		for(List<Block> l : _line) {
			for(Block b : l) {
				b.draw(batch);
			}
		}
		
		sr.dispose();
	}
}
