package de.pixlpommes.lib;

/**
 * <p>A tuple of two elements.</p>
 * 
 * @author Thomas Borck
 */
public class Tuple<TYPEA, TYPEB> {
	
	/** tuple element a */
	private final TYPEA _a;
	
	/** tuple element b */
	private final TYPEB _b;

	/**
	 * Create a result tuple.
	 * @param a
	 * @param b
	 */
	public Tuple(TYPEA a, TYPEB b) {
		_a = a;
		_b = b;
	}
	
	/**
	 * Get element a.
	 * @return
	 */
	public TYPEA getA() {
		return _a;
	}
	
	/**
	 * Get element b.
	 * @return
	 */
	public TYPEB getB() {
		return _b;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override public String toString() {
		return "(" + _a + ", " + _b + ")";
	}
}