package sigma.utils;

/**
 * Simple enum to implement option sides (CALL/PUT)
 * 
 * @author Peeter Meos
 * @version 1.0
 *
 */
public enum OptSide {
	CALL, PUT, NONE;
	
	public String toString() {
		switch (this) {
		case CALL:
			return ("CALL");
		case PUT:
			return ("PUT");
		default:
			return ("NONE");
		}
	}
}
