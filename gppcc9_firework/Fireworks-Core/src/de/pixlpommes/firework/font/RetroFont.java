package de.pixlpommes.firework.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * <p></p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public class RetroFont {

	/** characters as a texture */
	private final Texture _font;
	
	/** all mappable characters */
	private final String ALLOWED = " ABCDEFGHIJK" +
	                               "LMNOPQRSTUVW" +
			                       "XYZabcdefghi" +
	                               "jklmnopqrstu" +
			                       "vwxyz0123456" +
	                               "789,.;:-?!()" +
			                       "§$%"; // § = clock, $ = sum, % = screen
	
	/** amount of characters per row */
	private final int CHARSPERLINE = 12;
	
	/** size of origin characters in pixel */
	private final int CHARSIZE = 48;
	
	/**
	 * A new retro font object.
	 */
	public RetroFont() {
		_font = new Texture(Gdx.files.internal("tilefont.png"));
	}
	
	/**
	 * Draw a given text to screen at position (x,y).
	 * @param batch
	 * @param text
	 * @param x
	 * @param y
	 */
	public void draw(Batch batch, String text, int x, int y) {
		int[] origin = getPosByString(text);
		int curX = x;
		
		if(origin[0] == -1) {
			System.out.println("RetroFont:draw('" + text + "') contains invalid chars.");
			return;
		}
		
		for(int i = 0; i < text.length(); i++) {
			batch.draw(_font, 
					curX, y, // screen (x,y)
					CHARSIZE, CHARSIZE, // char size
					origin[i*2 + 0], origin[i*2 + 1], // char (x,y)
					CHARSIZE, CHARSIZE, // char size
					false,
					false);
			
			curX += CHARSIZE;
		}
	}
	
	/**
	 * Draw a given text to screen at position (x,y).
	 * @param batch
	 * @param text
	 * @param x
	 * @param y
	 * @param sizeX
	 * @param sizeY
	 */
	public void draw(Batch batch, String text, int x, int y, int sizeX, int sizeY) {
		int[] origin = getPosByString(text);
		int curX = x;
		
		if(origin[0] == -1) {
			System.out.println("RetroFont:draw('" + text + "') contains invalid chars.");
			return;
		}
		
		for(int i = 0; i < text.length(); i++) {
			batch.draw(_font, 
					curX, y, // screen (x,y)
					sizeX, sizeY, // screen size
					origin[i*2 + 0], origin[i*2 + 1], // char (x,y)
					CHARSIZE, CHARSIZE, // char size
					false,
					false);
			
			curX += sizeX;
		}
	}
	
	/**
	 * Draw a given text to screen at position (x,y).
	 * @param batch
	 * @param text
	 * @param x
	 * @param y
	 * @param centerX
	 * @param centerY
	 */
	public void draw(Batch batch, String text, int x, int y, boolean centerX, boolean centerY) {
		int cenX = centerX ? (x - text.length() * CHARSIZE / 2) : x;
		int cenY = centerY ? (y - CHARSIZE / 2) : y;
		
		draw(batch, text, cenX, cenY);
	}
	
	/**
	 * Draw a given text to screen at position (x,y).
	 * @param batch
	 * @param text
	 * @param x
	 * @param y
	 * @param sizeX
	 * @param sizeY
	 * @param centerX
	 * @param centerY
	 */
	public void draw(Batch batch, String text, int x, int y, int sizeX, int sizeY, boolean centerX, boolean centerY) {
		int cenX = centerX ? (x - text.length() * sizeX / 2) : x;
		int cenY = centerY ? (y - sizeY / 2) : y;
		
		draw(batch, text, cenX, cenY, sizeX, sizeY);
	}
	
	/**
	 * TODO: describe behavior
	 * @param c
	 * @return
	 */
	public int[] getPosByChar(char c) {
		int index = ALLOWED.indexOf(c);
		
		// if char is not a valid one
		if(index == -1) {
			return new int[]{-1};
		}
		
		// get position of c on texture
		int x = index % CHARSPERLINE;
		int y = index / CHARSPERLINE;
		
		return new int[]{x * CHARSIZE, y * CHARSIZE};
	}
	
	/**
	 * TODO: describe behavior
	 * @param text
	 * @return
	 */
	public int[] getPosByString(String text) {
		// x & y position for all chars of this text
		int[] result = new int[text.length() * 2];
		
		for(int i = 0; i < text.length(); i++) {
			int[] pos = getPosByChar(text.charAt(i));
			
			// invalid chars -> invalid text
			if(pos[0] == -1) return new int[]{-1};
			
			// valid chars -> put into result array
			result[i*2 + 0] = pos[0];
			result[i*2 + 1] = pos[1];
		}
		
		return result;
	}
}
