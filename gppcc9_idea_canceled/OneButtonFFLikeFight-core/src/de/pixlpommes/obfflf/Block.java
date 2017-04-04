package de.pixlpommes.obfflf;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * <p></p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public class Block {

	private int _x, _y, _w, _h;
	
	private Color _color;
	
	public Block(int x, int y, int width, int height, Color color) {
		_x = x;
		_y = y;
		_w = width;
		_h = height;
		_color = color;
	}
	
	public void update(float diffX) {
		_x += diffX;
	}
	
	public void draw(Batch batch) {
		ShapeRenderer sr = new ShapeRenderer();
		// solid background
		sr.begin(ShapeType.Filled);
			sr.setColor(_color);
			sr.rect(_x, _y, _w, _h);
		sr.end();
		
		sr.dispose();
	}
	
	public int getX() { return _x; }
	public int getY() { return _y; }
}
