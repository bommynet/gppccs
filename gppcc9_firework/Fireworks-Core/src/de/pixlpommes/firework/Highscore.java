package de.pixlpommes.firework;

import java.util.ArrayList;
import java.util.List;

import net.bommy.libs.checksum.Fletcher;

/**
 * <p>Save and load highscores.</p>
 * 
 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
 * @author Thomas Borck
 */
public class Highscore {

	/** highscore entries */
	private List<Entry> _entries;
	
	/** checksum to 'prevent' entries from data errors */
	private long _checksum;
	
	/**
	 * Create a new highscore list.
	 */
	public Highscore() {
		// init as empty highscore list
		_entries = new ArrayList<Entry>();
		for(int i=0; i<10; i++) _entries.add(new Entry());
		
		// init highscore checksum
		_checksum = 0;
	}
	
	/**
	 * Adds a new highscore to the list.
	 * @param entry
	 * @return index of new entry or -1 if not in the list
	 */
	public int add(Entry entry) {
		// check if entry is valid
		if(entry == null) return -1;
		
		// 1. add entry to list
		// 2. sort list
		// 3. cut off entries after index 10
		_entries.add(entry);
		_entries.sort(null);
		_entries.remove(_entries.size() - 1);
		
		// highscore has changed, so recalculate checksum
		byte[] data = toString().getBytes();
		_checksum = Fletcher.buildChecksum16(data);
		
		return _entries.indexOf(entry);
	}

	/**
	 * TODO: describe behavior
	 * @return
	 */
	public int lenght() {
		return _entries.size();
	}
	
	/**
	 * TODO: describe behavior
	 * @param index
	 * @return
	 */
	public Entry getEntry(int index) {
		return _entries.get(index);
	}
	
	
	
	/**
	 * <p>Highscore entry.</p>
	 * 
	 * <p><i>This class was created as part of my contribution to the #gppcc9.</i></p>
	 * @author Thomas Borck
	 */
	public static class Entry implements Comparable<Entry>{
		
		/** player name */
		private String _name;
		
		/** sum of fireworks */
		private int _overall;
		
		/** sum of fireworks that are on screen at the same time */
		private int _atonce;
		
		/** fireworks per second */
		private float _fps;
		
		/**
		 * An specific highscore entry.
		 * @param name
		 * @param overall
		 * @param atonce
		 * @param fps
		 */
		public Entry(String name, int overall, int atonce, float fps) {
			_name = name;
			_overall = overall;
			_atonce = atonce;
			_fps = fps;
		}
		
		/**
		 * An empty highscore entry.
		 */
		public Entry() {
			this("", 0, 0, 0f);
		}

		
		/**
		 * @return player name 
		 */
		public String getName() {
			return _name;
		}

		/**
		 * @param name new player name
		 */
		public void setName(String name) {
			_name = name;
		}

		/**
		 * @return sum of fireworks of this entry
		 */
		public int getOverall() {
			return _overall;
		}

		/**
		 * @param overall new sum of fireworks
		 */
		public void setOverall(int overall) {
			_overall = overall;
		}

		/**
		 * @return fireworks at once of this entry
		 */
		public int getAtonce() {
			return _atonce;
		}

		/**
		 * @param atonce new fireworks at once
		 */
		public void setAtonce(int atonce) {
			_atonce = atonce;
		}

		/**
		 * @return fps of this entry
		 */
		public float getFps() {
			return _fps;
		}

		/**
		 * @param fps new fps
		 */
		public void setFps(float fps) {
			_fps = fps;
		}

		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(Entry o) {
			if(_overall < o.getOverall())
				return 1;
			else if(_overall > o.getOverall())
				return -1;
			
			return 0;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return "("
					+ "name:" + _name + ", "
					+ "overall:" + _overall + ", "
					+ "atonce:" + _atonce + ", "
					+ "fps:" + _fps
					+ ")";
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String res = "";
		
		for(int i=0; i<_entries.size(); i++) {
			res += i + " => " + _entries.get(i).toString() + '\n';
		}
		
		return res;
	}
	
	/**
	 * TODO: describe behavior
	 * @return
	 */
	public String toJson() {
		String res = "";
		
		// open JSON object
		res += "{" + '\n';
		
		// add entries
		for(int i=0; i<_entries.size(); i++) {
			Highscore.Entry e = _entries.get(i);
			
			res += "  \"entry_" + i + "\": {" + '\n';
				res += "    \"name\": \""+ e.getName() +"\"," + '\n';
				res += "    \"all\": \""+ e.getOverall() +"\"," + '\n';
				res += "    \"once\": \""+ e.getAtonce() +"\"," + '\n';
				res += "    \"fps\": \""+ e.getFps() + "\"" + '\n';
			res += "  }," + '\n';
		}
		
		// add checksum as hex
		res += "  \"checksum\": \""+ Long.toHexString(_checksum) +"\"" + '\n'; 
		
		// close JSON object
		res += "}" + '\n';
		return res;
	}
}
