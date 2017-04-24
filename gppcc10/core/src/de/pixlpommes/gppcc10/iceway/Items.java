package de.pixlpommes.gppcc10.iceway;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.pixlpommes.gppcc10.Gppcc10;
import de.pixlpommes.gppcc10.iceway.Item.Type;

/**
 * @author Thomas Borck - http://www.pixlpommes.de
 * @version 0.0
 */
public class Items {

	/** TODO: describe '_items' */
	private List<Item> _items;
	
	
	
	/**
	 * 
	 */
	public Items() {
		_items = new ArrayList<Item>();
	}
	
	/**
	 * TODO: describe function
	 * @param deltaSpeed
	 */
	public void update(float deltaSpeed) {
		for(int i=_items.size()-1; i>=0; i--) {
			Item item = _items.get(i);
			// update positions
			item.update(deltaSpeed);
			
			// TODO: removed killed items
			// remove killed/used items
			if(item.getY() < -Gppcc10.HALF_HEIGHT - 2*Iceway.TILESIZE) {
				_items.remove(item);
			}
		}
		
		System.out.println("Items: " + _items.size());
	}
	
	/**
	 * TODO: describe function
	 * @param batch
	 */
	public void draw(Batch batch) {
		// draw reverse to show correct overlapping
		for(int i=_items.size()-1; i>=0; i--) {
			_items.get(i).draw(batch);
		}
	}

	/**
	 * TODO: describe function
	 * @param x
	 * @param y
	 * @param type
	 */
	public void add(float x, float y, Type type) {
		_items.add(new Item(x, y, type));
	}
}
